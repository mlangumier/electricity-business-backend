package fr.hb.mlang.electricitybusiness.modules.userprofile.controller;

import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.modules.userprofile.controller.dto.GetProfileResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {

  GetProfileResponseDto toGetProfileResponseDto(UserProfile userProfile);
}
