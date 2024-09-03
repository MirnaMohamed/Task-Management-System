package banquemisr.challenge05.taskmanagementservice.service;

import banquemisr.challenge05.taskmanagementservice.dto.request.ChangePasswordRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.UserInfoDto;
import java.util.List;

public interface UserService {
    List<UserInfoDto> getAllUsers();
    UserInfoDto extractUserFromUserId(Long userId);
    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);
}
