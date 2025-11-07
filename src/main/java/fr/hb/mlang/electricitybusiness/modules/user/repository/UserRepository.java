package fr.hb.mlang.electricitybusiness.modules.user.repository;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.refreshTokens WHERE u.email = :email")
  Optional<User> findByEmailWithRefreshToken(String email);
}
