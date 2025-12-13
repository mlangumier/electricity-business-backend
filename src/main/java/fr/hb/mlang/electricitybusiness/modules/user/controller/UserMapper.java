package fr.hb.mlang.electricitybusiness.modules.user.controller;

import fr.hb.mlang.electricitybusiness.modules.booking.Booking;
import fr.hb.mlang.electricitybusiness.modules.location.domain.Location;
import fr.hb.mlang.electricitybusiness.modules.user.controller.dto.GetUserActivityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  //TODO: adapt to map the entities correctly
  GetUserActivityResponseDto toGetActivityResponseDto(Location[] locations, Booking[] bookings);
}
