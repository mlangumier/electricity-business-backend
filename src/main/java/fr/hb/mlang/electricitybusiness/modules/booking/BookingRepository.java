package fr.hb.mlang.electricitybusiness.modules.booking;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

}
