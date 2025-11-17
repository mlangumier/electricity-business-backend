package fr.hb.mlang.electricitybusiness.modules.userprofile.service;

import fr.hb.mlang.electricitybusiness.modules.userprofile.controller.dto.GetProfileResponseDto;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserProfileService {

  GetProfileResponseDto getProfile(UUID userId);
}
