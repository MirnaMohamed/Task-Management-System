package banquemisr.challenge05.taskmanagementservice.service;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.TaskResponseDto;
import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;

import java.util.List;

public interface TaskService {
    void addTask(TaskRequestDto taskDto);
    void updateTask(long id, TaskRequestDto taskDto);
    List<TaskResponseDto> getAllTasks(int pageNo, int pageSize);
    TaskResponseDto getTaskById(long id);
    void deleteTaskById(long id);
    List<TaskResponseDto> filterTasksByPriority(Priority priority, int pageNo, int pageSize);
    List<TaskResponseDto> filterTasksByStatus(Status status, int pageNo, int pageSize);
    List<TaskResponseDto> filterTasksByTitle(String title, int pageNo, int pageSize);
}
