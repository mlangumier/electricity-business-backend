package fr.hb.mlang.electricitybusiness.modules.location;

import fr.hb.mlang.electricitybusiness.modules.location.domain.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, UUID> {

}
