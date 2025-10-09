package fr.hb.mlang.electricitybusiness.security.auth.service;

import fr.hb.mlang.electricitybusiness.config.AppProperties;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationTokenRepository;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.security.auth.SecurityUserDetails;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.EmailAvailableRequest;
import fr.hb.mlang.electricitybusiness.security.auth.controller.dto.RegisterRequest;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailAlreadyInUseException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.ExpiredTokenException;
import fr.hb.mlang.electricitybusiness.security.auth.exception.UserAlreadyVerifiedException;
import fr.hb.mlang.electricitybusiness.security.jwt.VerificationToken;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final EmailVerificationTokenRepository emailTokenRepository;
  private final PasswordEncoder encoder;
  private final AppProperties.Jwt jwtProps;

  public AuthServiceImpl(
      UserRepository userRepository,
      EmailVerificationTokenRepository emailTokenRepository,
      PasswordEncoder encoder,
      AppProperties appProps
  ) {
    this.userRepository = userRepository;
    this.emailTokenRepository = emailTokenRepository;
    this.encoder = encoder;
    this.jwtProps = appProps.jwt();
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

    // TODO: on success -> Send email (with rawToken) & OK
  }

  @Override
  @Transactional
  public void verifyAccount(String token) {
    String tokenHash = VerificationToken.hashToken(token);

    EmailVerificationToken tokenEntity = emailTokenRepository
        .findByTokenHash(tokenHash)
        .orElseThrow(() -> new EntityNotFoundException("Couldn't find email verification token"));

    if (tokenEntity.getExpiresAt().isBefore(Instant.now())) {
      throw new ExpiredTokenException(
          "Email verification token is expired: " + tokenEntity.getExpiresAt());
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
}
