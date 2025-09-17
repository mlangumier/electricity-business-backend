package fr.hb.mlang.electricitybusiness.modules.tokens.email;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationTokenRepository extends
    JpaRepository<EmailVerificationToken, UUID> {

}
