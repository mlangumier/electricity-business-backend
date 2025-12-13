package fr.hb.mlang.electricitybusiness.modules.user.controller;

import fr.hb.mlang.electricitybusiness.modules.user.controller.dto.GetUserActivityResponseDto;
import fr.hb.mlang.electricitybusiness.modules.user.domain.User;
import fr.hb.mlang.electricitybusiness.modules.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/")
  public ResponseEntity<GetUserActivityResponseDto> getUserActivity(@AuthenticationPrincipal User user) {
    GetUserActivityResponseDto getUserActivityResponseDto = new GetUserActivityResponseDto(); //TODO: fetch in service
    return ResponseEntity.status(HttpStatus.OK).body(getUserActivityResponseDto);
  }
}
