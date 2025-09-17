package fr.hb.mlang.electricitybusiness.modules.user.repository;

import fr.hb.mlang.electricitybusiness.modules.user.domain.UserAuth;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, UUID> {

  Optional<UserAuth> findByEmailOrPhoneNumber(String email, String phone);
}
