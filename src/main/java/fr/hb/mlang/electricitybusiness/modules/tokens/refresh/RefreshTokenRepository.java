package fr.hb.mlang.electricitybusiness.modules.tokens.refresh;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

}
