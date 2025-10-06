package fr.hb.mlang.electricitybusiness.security.auth.service;

import fr.hb.mlang.electricitybusiness.config.AppProperties;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailAlreadyInUseException;
import fr.hb.mlang.electricitybusiness.security.auth.web.dto.RegisterRequest;
import fr.hb.mlang.electricitybusiness.security.jwt.VerificationToken;
import java.time.Instant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final AppProperties.Jwt jwtProps;

  public AuthServiceImpl(
      UserRepository userRepository,
      PasswordEncoder encoder,
      AppProperties appProps
  ) {
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.jwtProps = appProps.jwt();
  }

  @Override
  public void register(RegisterRequest req) {
    if (userRepository.findByEmail(req.email()).isPresent()) {
      throw new EmailAlreadyInUseException(req.email());
    }

    User user = new User();
    user.setEmail(req.email());
    if (req.phoneNumber() != null) {
      user.setPhoneNumber(req.phoneNumber());
    }
    //userRepository.save(user);

    // Create & set authentication password
    UserAuth userAuth = new UserAuth(this.encoder.encode(req.password()));
    user.setAuth(userAuth);

    // Create & set user profile
    UserProfile profile = new UserProfile(
        req.firstName(),
        req.lastName(),
        req.dateOfBirth(),
        req.homeAddress(),
        null
    );
    if (req.avatar() != null) {
      profile.setAvatar(req.avatar());
    }
    user.setProfile(profile);

    // Create & set verification token
    String rawToken = VerificationToken.generateRawToken();
    String hashedToken = VerificationToken.hashToken(rawToken);

    EmailVerificationToken emailVerificationToken = new EmailVerificationToken(
        hashedToken,
        Instant.now().plus(jwtProps.verificationExpiration())
    );
    user.setEmailVerificationToken(emailVerificationToken);

    // Save user with default relationships (OneToOne)
    userRepository.save(user);

    // TODO: on success -> Send email (with rawToken) & OK
  }
}
