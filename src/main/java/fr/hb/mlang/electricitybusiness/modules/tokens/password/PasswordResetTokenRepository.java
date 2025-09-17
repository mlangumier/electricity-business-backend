package fr.hb.mlang.electricitybusiness.modules.tokens.password;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

}
