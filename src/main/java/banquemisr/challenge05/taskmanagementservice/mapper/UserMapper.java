package banquemisr.challenge05.taskmanagementservice.mapper;

import banquemisr.challenge05.taskmanagementservice.dto.request.UserLoginRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.request.UserRegisterRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.UserLoginResponseDto;
import banquemisr.challenge05.taskmanagementservice.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserLoginResponseDto usertoUserLoginResponseDto(User user);
    User registerDtotoUser(UserRegisterRequestDto registerRequestDto);
    User loginDtotoUser(UserLoginRequestDto loginRequestDto);
}
