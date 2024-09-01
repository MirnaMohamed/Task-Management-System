package banquemisr.challenge05.taskmanagementservice.mapper;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.TaskResponseDto;
import banquemisr.challenge05.taskmanagementservice.model.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResponseDto toTaskResponseDto(Task task);
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "createdOn", ignore = true)
    Task toTask(TaskRequestDto taskDto);
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task updateTaskFromTaskRequestDto(TaskRequestDto taskDto, @MappingTarget Task task);
}
