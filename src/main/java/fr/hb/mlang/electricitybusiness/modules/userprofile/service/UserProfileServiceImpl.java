package fr.hb.mlang.electricitybusiness.modules.userprofile.service;

import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfile;
import fr.hb.mlang.electricitybusiness.modules.userprofile.UserProfileRepository;
import fr.hb.mlang.electricitybusiness.modules.userprofile.controller.UserProfileMapper;
import fr.hb.mlang.electricitybusiness.modules.userprofile.controller.dto.GetProfileResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

  private final UserProfileRepository profileRepository;
  private final UserProfileMapper mapper;

  public UserProfileServiceImpl(UserProfileRepository profileRepository, UserProfileMapper mapper) {
    this.profileRepository = profileRepository;
    this.mapper = mapper;
  }

  @Override
  public GetProfileResponseDto getProfile(UUID userId) {
    UserProfile profile = profileRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("UserProfile not found"));

    return mapper.toGetProfileResponseDto(profile);
  }
}
