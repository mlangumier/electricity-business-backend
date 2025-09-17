package fr.hb.mlang.electricitybusiness.modules.user.repository;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

}
