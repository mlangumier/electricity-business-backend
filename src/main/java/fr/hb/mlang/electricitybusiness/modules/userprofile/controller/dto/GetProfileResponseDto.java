package fr.hb.mlang.electricitybusiness.modules.userprofile.controller.dto;

import java.time.LocalDate;

public record GetProfileResponseDto(
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String preferences,
    String avatar
) {

}
