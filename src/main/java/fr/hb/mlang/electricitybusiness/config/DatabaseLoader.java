package fr.hb.mlang.electricitybusiness.config;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.modules.booking.BookingRepository;
import fr.hb.mlang.electricitybusiness.modules.location.LocationRepository;
import fr.hb.mlang.electricitybusiness.modules.location.domain.Location;
import fr.hb.mlang.electricitybusiness.modules.station.Station;
import fr.hb.mlang.electricitybusiness.modules.station.StationRepository;
import fr.hb.mlang.electricitybusiness.modules.tokens.email.EmailVerificationToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.password.PasswordResetToken;
import fr.hb.mlang.electricitybusiness.modules.tokens.refresh.RefreshToken;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import fr.hb.mlang.electricitybusiness.modules.user.repository.UserRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.security.jwt.VerificationToken;
import fr.hb.mlang.electricitybusiness.shared.utils.MoneyUtils;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DatabaseLoader implements ApplicationRunner {
  private final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);
  private final AppProperties.Jwt jwtProps;
  private final PasswordEncoder encoder;
  private final UserRepository userRepository;
  private final LocationRepository locationRepository;
  private final StationRepository stationRepository;
  private final BookingRepository bookingRepository;

  public DatabaseLoader(AppProperties jwtProps, PasswordEncoder encoder, UserRepository userRepository, LocationRepository locationRepository, StationRepository stationRepository, BookingRepository bookingRepository) {
    this.jwtProps = jwtProps.jwt();
    this.encoder = encoder;
    this.userRepository = userRepository;
    this.locationRepository = locationRepository;
    this.stationRepository = stationRepository;
    this.bookingRepository = bookingRepository;
  }

  @Override
  public void run(ApplicationArguments args) {
    log.info("Running data loader...");

    if (userRepository.count() == 0) {
      log.info("No user found. Generation data...");

      //--- User 1 (user, userAuth, userProfile, all 3 tokens)
      User user = userRepository.save(new User("matt@test.com",null));
      UserAuth userAuth = new UserAuth(encoder.encode("password"));
      user.setAuth(userAuth);
      UserProfile profile = new UserProfile("Mathieu", "Langumier", LocalDate.of(1992, 2, 24), "24 place Jean Jaurès, St-Etienne", null);
      user.setProfile(profile);
      user.setEmailVerificationToken(new EmailVerificationToken(VerificationToken.hashToken(VerificationToken.generateRawToken()), Instant.now().plus(jwtProps.verificationExpiration())));
      user.setPasswordResetToken(new PasswordResetToken(encoder.encode("token"), Instant.now().plus(jwtProps.passwordExpiration())));
      user.addRefreshToken(new RefreshToken(encoder.encode("token"), Instant.now().plus(jwtProps.refreshExpiration())));
      userRepository.save(user);

      //---- User 2
      User user2 = userRepository.save(new User("sam@test.com", "0600000000"));
      UserAuth userAuth2 = new UserAuth(encoder.encode("password"));
      user2.setAuth(userAuth2);
      UserProfile profile2 = new UserProfile("Sam", "Lang", LocalDate.of(1991, 1, 1), "24 place Jean Jaurès", null);
      user2.setProfile(profile2);
      user.setEmailVerificationToken(new EmailVerificationToken(encoder.encode("token"), Instant.now().plus(jwtProps.verificationExpiration())));
      user.setPasswordResetToken(new PasswordResetToken(encoder.encode("token"), Instant.now().plus(jwtProps.passwordExpiration())));
      user.addRefreshToken(new RefreshToken(encoder.encode("token"), Instant.now().plus(jwtProps.refreshExpiration())));
      userRepository.save(user2);

      //--- Location
      Location location = new Location("42 avenue Paul Kruger", "Villeurbanne", "69100", 45.75568327186586, 4.896008228863917);
      location.setUser(user);
      locationRepository.save(location);

      //--- Charging Stations
      Station station = new Station("New station", "Description of the charging station", 20, false, MoneyUtils.of(0.14));
      station.setLocation(location);
      stationRepository.save(station);

      //--- Bookings
      Booking booking = new Booking(Instant.now().plus(3, ChronoUnit.DAYS), Instant.now().plus(3, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS));
      booking.setCustomer(user2);
      booking.setStation(station);
      bookingRepository.save(booking);

      log.info("Data generated!");
    } else {
      log.info("Users found. Skipping data generation.");
    }
  }
}
