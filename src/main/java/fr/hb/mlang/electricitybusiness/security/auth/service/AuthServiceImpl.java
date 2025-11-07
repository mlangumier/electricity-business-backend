package fr.hb.mlang.electricitybusiness.security.auth.service;

import fr.hb.mlang.electricitybusiness.config.AppProperties;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationTokenRepository;
import fr.hb.mlang.electricitybusiness.modules.tokens.refresh.RefreshToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.refresh.RefreshTokenRepository;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.security.CookieUtil;
import fr.hb.mlang.electricitybusiness.security.auth.SecurityUserDetails;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.EmailAvailableRequest;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.LoginRequestDto;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.LoginResponseDto;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.RegisterRequest;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailAlreadyInUseException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailVerificationTokenException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.UserAlreadyVerifiedException;
import fr.hb.mlang.electricitybusiness.security.jwt.JwtService;
import fr.hb.mlang.electricitybusiness.security.jwt.VerificationToken;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final EmailVerificationTokenRepository emailTokenRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder encoder;
  private final Argon2PasswordEncoder argon2PasswordEncoder;
  private final AppProperties.Jwt jwtProps;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  public AuthServiceImpl(
      UserRepository userRepository,
      EmailVerificationTokenRepository emailTokenRepository,
      RefreshTokenRepository refreshTokenRepository,
      PasswordEncoder encoder,
      Argon2PasswordEncoder argon2PasswordEncoder,
      AppProperties appProps,
      JwtService jwtService,
      AuthenticationManager authManager
  ) {
    this.userRepository = userRepository;
    this.emailTokenRepository = emailTokenRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.encoder = encoder;
    this.argon2PasswordEncoder = argon2PasswordEncoder;
    this.jwtProps = appProps.jwt();
    this.jwtService = jwtService;
    this.authManager = authManager;
  }

  @Override
  public Boolean checkIsEmailAvailable(EmailAvailableRequest request) {
    //FIXME: Change to find().elseThrow() & void return
    return userRepository.findByEmail(request.email()).isPresent();
  }

  @Override
  public void register(RegisterRequest req) {
    if (userRepository.findByEmail(req.email()).isPresent()) {
      throw new EmailAlreadyInUseException(req.email());
    }

    // Create User
    User user = new User();
    user.setEmail(req.email().toLowerCase());
    if (req.phoneNumber() != null) {
      user.setPhoneNumber(req.phoneNumber());
    }

    // Create & set UserAuth password
    UserAuth userAuth = new UserAuth(this.encoder.encode(req.password()));
    user.setAuth(userAuth);

    // Create & set UserProfile
    UserProfile profile = new UserProfile(
        req.firstName(),
        req.lastName(),
        req.dateOfBirth(),
        req.homeAddress()
    );
    if (req.avatar() != null) {
      profile.setAvatar(req.avatar());
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

    EmailVerificationToken tokenEntity = emailTokenRepository
        .findByTokenHash(tokenHash)
        .orElseThrow(() -> new EmailVerificationTokenException(
            "Could not find email verification token."));

    if (tokenEntity.getExpiresAt().isBefore(Instant.now())) {
      throw new EmailVerificationTokenException("Email verification token is expired.");
    }

    // Use our adapter to handle data from both User & UserAuth
    SecurityUserDetails userDetails = SecurityUserDetails.from(tokenEntity.getUser());

    if (userDetails.auth().getEmailVerified()) {
      throw new UserAlreadyVerifiedException(userDetails.user().getEmail());
    }

    // Verifications checks passed -> set user verified & delete email verification token
    userDetails.auth().setEmailVerified(true);
    userDetails.user().setEmailVerificationToken(null);
    userRepository.save(userDetails.user());

    //TODO: send welcome email with link to login page (& optional: short description of available features)
  }

  @Transactional
  @Override
  public LoginResponseDto authenticateUser(
      LoginRequestDto credentials,
      HttpServletResponse response
  ) {
    Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
        credentials.email(),
        credentials.password()
    ));
    System.out.println(">> Authentication: " + authentication.toString());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
    System.out.println(">> SecurityUserDetails: " + userDetails.toString());
    User user = getUser(userDetails);
    // Ideally: if the user has a token for the same device, remove it and add the new token instead.

    // Generate refresh token & set cookie in response headers
    String rawRefreshToken = jwtService.generateRefreshToken(userDetails.getUsername());
    String hashRefrestoken = argon2PasswordEncoder.encode(rawRefreshToken);
    RefreshToken refreshToken = new RefreshToken(
        hashRefrestoken,
        Instant.now().plus(jwtProps.refreshExpiration())
    );
    refreshToken.setUser(user);
    //user.getRefreshTokens().add(refreshToken);
    user.getAuth().setLastLogin(Instant.now());
    userRepository.save(user);

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        CookieUtil
            .createRefreshTokenCookie(rawRefreshToken, jwtProps.refreshExpiration())
            .toString()
    );

    // Generate accessToken & return response
    String accessToken = jwtService.generateAccessToken(userDetails.getUsername());

    // Move to AuthMapper if reused later
    return new LoginResponseDto(
        accessToken,
        new LoginResponseDto.UserAuth(
            user.getId(),
            userDetails.getUsername(),
            user.getRole(),
            user.getProfile().getFirstName(),
            user.getProfile().getLastName(),
            user.getProfile().getAvatar(),
            user.getProfile().getPreferences()
        )
    );
  }

  private static User getUser(SecurityUserDetails userDetails) {
    return userDetails.user();
  }
}
