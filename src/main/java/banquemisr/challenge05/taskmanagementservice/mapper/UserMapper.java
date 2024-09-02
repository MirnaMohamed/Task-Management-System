package banquemisr.challenge05.taskmanagementservice.mapper;

import banquemisr.challenge05.taskmanagementservice.dto.request.UserRegisterRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.UserInfoDto;
import banquemisr.challenge05.taskmanagementservice.model.Task;
import banquemisr.challenge05.taskmanagementservice.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "tasks", target = "tasksNum", qualifiedByName = "convertTasksToTasksNum")
    UserInfoDto toUserInfoResponseDto(User user);
    User registerDtotoUser(UserRegisterRequestDto registerRequestDto);

    @Named("convertTasksToTasksNum")
    default int convertTasksToTasksNum(List<Task> taskList){
        return taskList.size();
    }
}
