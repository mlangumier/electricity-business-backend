package fr.hb.mlang.electricitybusiness.modules.station;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, UUID> {

}
