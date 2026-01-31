package fr.hb.mlang.electricitybusiness.security.auth.service;

import fr.hb.mlang.electricitybusiness.config.AppProperties;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationTokenRepository;
import fr.hb.mlang.electricitybusiness.modules.tokens.refresh.RefreshToken;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.security.CookieUtil;
import fr.hb.mlang.electricitybusiness.security.auth.controller.AuthMapper;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.EmailAvailableRequest;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.LoginRequestDto;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.LoginResponseDto;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.RegisterRequest;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailAlreadyInUseException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailVerificationTokenException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.RefreshTokenException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.UserAlreadyVerifiedException;
import fr.hb.mlang.electricitybusiness.security.jwt.JwtService;
import fr.hb.mlang.electricitybusiness.security.jwt.VerificationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final AppProperties.Jwt jwtProps;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;
  private final PasswordEncoder encoder;
  private final Argon2PasswordEncoder refreshTokenEncoder;
  private final EmailVerificationTokenRepository emailTokenRepository;
  private final UserRepository userRepository;
  private final UserDetailsService userDetailsService;
  private final AuthMapper mapper;

  public AuthServiceImpl(
      AppProperties appProps,
      JwtService jwtService,
      AuthenticationManager authManager,
      PasswordEncoder encoder,
      Argon2PasswordEncoder refreshTokenEncoder,
      EmailVerificationTokenRepository emailTokenRepository,
      UserRepository userRepository,
      UserDetailsService userDetailsService,
      AuthMapper mapper
  ) {
    this.jwtProps = appProps.jwt();
    this.jwtService = jwtService;
    this.authManager = authManager;
    this.encoder = encoder;
    this.refreshTokenEncoder = refreshTokenEncoder;
    this.emailTokenRepository = emailTokenRepository;
    this.userRepository = userRepository;
    this.userDetailsService = userDetailsService;
    this.mapper = mapper;
  }

  @Override
  public void checkIsEmailAvailable(EmailAvailableRequest req) {
    if (userRepository.findByEmail(req.email()).isPresent()) {
      throw new EmailAlreadyInUseException(req.email());
    }
  }

  @Override
  public void register(RegisterRequest req) {
    if (userRepository.findByEmail(req.email()).isPresent()) {
      throw new EmailAlreadyInUseException(req.email());
    }

    // Create User
    User user = new User(req.email().toLowerCase(), this.encoder.encode(req.password()));

    // Create & set UserProfile
    UserProfile profile = new UserProfile(req.firstName(), req.lastName(), req.dateOfBirth());

    if (req.phoneNumber() != null) {
      profile.setPhoneNumber(req.phoneNumber());
    }

    user.setProfile(profile);

    // Create & set EmailVerificationToken
    String rawToken = VerificationToken.generateRawToken();
    String hashedToken = VerificationToken.hashToken(rawToken);

    EmailVerificationToken emailVerificationToken = new EmailVerificationToken(
        hashedToken,
        Instant.now().plus(jwtProps.verificationExpiration())
    );
    user.setEmailVerificationToken(emailVerificationToken);

    // Save user with default relationships
    userRepository.save(user);

    // TODO: on success -> Send email (with rawToken)
  }

  @Override
  @Transactional
  public void verifyAccount(String token) {
    String tokenHash = VerificationToken.hashToken(token);

    //TODO: replace the following by: extract userEmail, find user, check if user's token corresponds
    EmailVerificationToken tokenEntity = emailTokenRepository
        .findByTokenHash(tokenHash)
        .orElseThrow(() -> new EmailVerificationTokenException(
            "Could not find email verification token."));

    if (tokenEntity.getExpiresAt().isBefore(Instant.now())) {
      throw new EmailVerificationTokenException("Email verification token is expired.");
    }

    User user = (User) userDetailsService.loadUserByUsername(tokenEntity.getUser().getEmail());

    if (user.getEmailVerified()) {
      throw new UserAlreadyVerifiedException(user.getEmail());
    }

    // Verifications checks passed -> set user verified & delete email verification token
    user.setEmailVerified(true);
    user.setEmailVerificationToken(null);
    userRepository.save(user);

    //TODO: send welcome email with link to login page (& optional: short description of available features)
  }

  @Override
  @Transactional
  public LoginResponseDto authenticateUser(
      LoginRequestDto credentials,
      HttpServletResponse response
  ) {
    //TODO: find the user first using credentials.email & check if verified before allowing authentication
    //TODO: check if the user is already authenticated (check auth interceptor)

    Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
        credentials.email(),
        credentials.password()
    ));

    User user = (User) authentication.getPrincipal();
    //User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

    // Ideally: if the user has a token for the same device, remove it and add the new token instead.

    // Generate refresh token & set cookie in response headers
    String rawRefreshToken = jwtService.generateRefreshToken(user.getUsername());
    String hashRefreshtoken = refreshTokenEncoder.encode(rawRefreshToken);
    RefreshToken refreshToken = new RefreshToken(
        hashRefreshtoken,
        Instant.now().plus(jwtProps.refreshExpiration())
    );
    user.addRefreshToken(refreshToken);
    user.setLastLogin(Instant.now());
    userRepository.save(user);

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        CookieUtil
            .createRefreshTokenCookie(rawRefreshToken, jwtProps.refreshExpiration())
            .toString()
    );

    // Generate accessToken & return response
    String accessToken = jwtService.generateAccessToken(user.getUsername());

    return mapper.toLoginResponseDto(accessToken, user);
  }

  @Override
  @Transactional
  public LoginResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) {
    String cookieRefreshToken = CookieUtil.readRefreshTokenCookie(request.getCookies());
    if (cookieRefreshToken == null) {
      throw new RefreshTokenException("Refresh token missing from request cookies.");
    }

    jwtService.assertSignatureIsValid(cookieRefreshToken);

    if (jwtService.isTokenExpired(cookieRefreshToken)) {
      throw new RefreshTokenException("Refresh token is expired.");
    }

    String email = jwtService.extractUserEmail(cookieRefreshToken);
    if (email == null || email.isBlank()) {
      throw new RefreshTokenException("Couldn't extract email from refresh token.");
    }

    User user = (User) userDetailsService.loadUserByUsername(email);

    if (!user.getEmailVerified()) {
      throw new RefreshTokenException("User is not verified.");
    }

    String newAccessToken = jwtService.generateAccessToken(user.getUsername());

    return mapper.toLoginResponseDto(newAccessToken, user);
  }

  @Override
  @Transactional
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    String cookieRefreshToken = CookieUtil.readRefreshTokenCookie(request.getCookies());
    //TODO: handle refresh in frontend
    //if (cookieRefreshToken == null) {
    //  throw new RefreshTokenException("Refresh token missing from request cookies.");
    //}

    if (cookieRefreshToken != null) {
      jwtService.assertSignatureIsValid(cookieRefreshToken);

      if (jwtService.isTokenExpired(cookieRefreshToken)) {
        throw new RefreshTokenException("Refresh token is expired.");
      }

      String email = jwtService.extractUserEmail(cookieRefreshToken);
      if (email == null || email.isBlank()) {
        throw new RefreshTokenException("Couldn't extract email from refresh token.");
      }

      User user = (User) userDetailsService.loadUserByUsername(email);

      RefreshToken refreshToken = user.getRefreshTokens()
          .stream()
          .filter(token -> refreshTokenEncoder.matches(cookieRefreshToken, token.getTokenHash()))
          .findFirst().orElseThrow(() -> new RefreshTokenException("Couldn't find refresh token"));

      user.removeRefreshToken(refreshToken);
      userRepository.save(user);
    }

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        CookieUtil.cleanRefreshTokenCookie().toString()
    );
  }
}
