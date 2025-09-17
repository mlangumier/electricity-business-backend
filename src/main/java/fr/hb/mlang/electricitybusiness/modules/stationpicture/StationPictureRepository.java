package fr.hb.mlang.electricitybusiness.modules.stationpicture;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationPictureRepository extends JpaRepository<StationPicture, UUID> {

}
