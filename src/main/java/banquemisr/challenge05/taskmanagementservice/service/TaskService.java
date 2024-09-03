package banquemisr.challenge05.taskmanagementservice.service;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.TaskResponseDto;
import banquemisr.challenge05.taskmanagementservice.filter.TaskSearchDto;
import banquemisr.challenge05.taskmanagementservice.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TaskService {
    void addTask(TaskRequestDto taskDto);
    void updateTask(long id, TaskRequestDto taskDto);
    List<TaskResponseDto> getAllTasks(int pageNo, int pageSize);
    List<TaskResponseDto> getAllTasks();
    List<TaskResponseDto> findAllTasksByUser(long userId, int pageNo, int pageSize);
    List<TaskResponseDto> findAllTasksByUser(int pageNo, int pageSize);
    List<TaskResponseDto> findBySearchCriteria(int pageNo, int pageSize, List<TaskSearchDto> taskFilter);
    TaskResponseDto getTaskById(long id);
    void deleteTaskById(long id);
    void assignTaskToUser(long userId, TaskRequestDto taskRequestDto);
    void assignTaskToUser(long userId, long taskId);
    List<TaskResponseDto> filterTasksByTitle(String title, int pageNo, int pageSize);
}
