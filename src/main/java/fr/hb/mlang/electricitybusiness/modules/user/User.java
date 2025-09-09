package fr.hb.mlang.electricitybusiness.modules.user;

import java.time.Instant;
import java.util.UUID;

public class User {

  private UUID id;
  private String email;
  private String phoneNumber;
  private String passwordHash;
  private UserRole role;
  private Boolean verified;
  private Instant lastLogin;

  //private UserProfile profile; // OneToOne
  //private Set<Location> locations; //OneToMany
  //private Set<Booking> bookings; //OneToMany
  //private EmailVerificationToken emailVerificationToken; //OneToOne
  //private PasswordResetToken passwordResetToken; //OneToOne
  //private Set<RefreshToken> refreshTokens; //OneToMany

  //TODO: Split into:
  // - `User`: domain, only user info no credentials (remove password)
  // - `UserAuth` (new table): authentication only (user credentials), same `id`(PK) as User
  // - `SecurityUser`: implements UserDetails, adapts User-UserAuth
  // -> Separation of concerns, easy to scale
}
