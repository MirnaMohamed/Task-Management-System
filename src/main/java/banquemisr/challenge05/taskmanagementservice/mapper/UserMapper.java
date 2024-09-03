package banquemisr.challenge05.taskmanagementservice.mapper;

import banquemisr.challenge05.taskmanagementservice.dto.request.UserRegisterRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.UserInfoDto;
import banquemisr.challenge05.taskmanagementservice.model.Task;
import banquemisr.challenge05.taskmanagementservice.model.User;
import org.mapstruct.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mapping(source = "tasks", target = "tasksNum", qualifiedByName = "convertTasksToTasksNum")
    UserInfoDto toUserInfoResponseDto(User user);
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    User registerDtotoUser(UserRegisterRequestDto registerRequestDto);

    @Named("convertTasksToTasksNum")
    default int convertTasksToTasksNum(List<Task> taskList){
        return taskList.size();
    }

    @Named("encodePassword")
    default String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
