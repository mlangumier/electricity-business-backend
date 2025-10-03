package fr.hb.mlang.electricitybusiness.security.auth.service;

import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.security.auth.exception.EmailAlreadyInUseException;
import fr.hb.mlang.electricitybusiness.security.auth.web.dto.RegisterRequest;
import fr.hb.mlang.electricitybusiness.security.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final JwtService jwtService;

  public AuthServiceImpl(UserRepository userRepository, PasswordEncoder encoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.jwtService = jwtService;
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
    UserProfile profile = new UserProfile(req.firstName(), req.lastName(), req.dateOfBirth(), req.homeAddress(), null);
    if (req.avatar() != null) {
      profile.setAvatar(req.avatar());
    }
    user.setProfile(profile);

    // Create & set verification token
    String token = jwtService.generateVerificationToken(req.email());
    EmailVerificationToken emailVerificationToken = new EmailVerificationToken(token, jwtService.extractExpiration(token).toInstant());
    user.setEmailVerificationToken(emailVerificationToken);

    // Save user with default relationships (OneToOne)
    userRepository.save(user);

    // TODO: on success -> Send email & OK
  }
}
