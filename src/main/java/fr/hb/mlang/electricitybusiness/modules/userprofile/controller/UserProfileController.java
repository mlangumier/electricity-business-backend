package fr.hb.mlang.electricitybusiness.modules.userprofile.controller;

import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.userprofile.controller.dto.GetProfileResponseDto;
import fr.hb.mlang.electricitybusiness.modules.userprofile.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserProfileController {

  private final UserProfileService profileService;

  public UserProfileController(UserProfileService profileService) {
    this.profileService = profileService;
  }

  @GetMapping("/profile")
  public ResponseEntity<GetProfileResponseDto> getProfile(@AuthenticationPrincipal User user) {
    GetProfileResponseDto profileDto = profileService.getProfile(user.getId());
    return ResponseEntity.status(HttpStatus.OK).body(profileDto);
  }

  ;
}
