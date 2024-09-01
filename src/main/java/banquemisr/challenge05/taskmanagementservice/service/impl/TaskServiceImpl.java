package banquemisr.challenge05.taskmanagementservice.service.impl;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.TaskResponseDto;
import banquemisr.challenge05.taskmanagementservice.exception.InvalidDueDateException;
import banquemisr.challenge05.taskmanagementservice.mapper.TaskMapper;
import banquemisr.challenge05.taskmanagementservice.model.Task;
import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import banquemisr.challenge05.taskmanagementservice.repository.TaskRepository;
import banquemisr.challenge05.taskmanagementservice.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public void addTask(TaskRequestDto taskDto) {
        Task task = taskMapper.toTask(taskDto);
        task.setCreatedOn(LocalDateTime.now());
        validateTask(task);
        taskRepository.save(task);
    }

    @Override
    public void updateTask(long id, TaskRequestDto taskDto) {
        Optional<Task> taskData = taskRepository.findById(id);
        if(taskData.isPresent()) {
            Task task = taskData.get();
            Task updatedTask = taskMapper.updateTaskFromTaskRequestDto(taskDto, task);
            taskRepository.save(updatedTask);
        }
        else
            throw new NotFoundException("Task with id: " + id +" not found. Try adding a new Task.");
        }

    @Override
    public List<TaskResponseDto> getAllTasks(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);

        Page<Task> pagedResult = taskRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            List<Task> tasks= pagedResult.getContent();
            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public TaskResponseDto getTaskById(long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Task with id " + id + " not found")
        );
        return taskMapper.toTaskResponseDto(task);
    }

    @Override
    public void deleteTaskById(long id) {

        Optional<Task> taskData = taskRepository.findById(id);
        if(taskData.isPresent())
            taskRepository.delete(taskData.get());
        else
            throw new NotFoundException("Task with id " + id + " not found in database. Could not delete task");
    }

    @Override
    public List<TaskResponseDto> filterTasksByPriority(Priority priority, int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Task> pagedResult = taskRepository.findByPriority(priority, paging);

        if(pagedResult.hasContent()) {
            List<Task> tasks= pagedResult.getContent();
            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
        } else {
            return new ArrayList<>();
        }
    }

    public List<TaskResponseDto> filterTasksByStatus(Status status, int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Task> pagedResult = taskRepository.findByStatus(status, paging);

        if(pagedResult.hasContent()) {
            List<Task> tasks= pagedResult.getContent();
            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<TaskResponseDto> filterTasksByTitle(String title, int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Task> pagedResult = taskRepository.findByTitleContainingIgnoreCase(title, paging);

        if(pagedResult.hasContent()) {
            List<Task> tasks= pagedResult.getContent();
            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
        } else {
            return new ArrayList<>();
        }
    }

    private void validateTask(Task task) {
        if(task.getStatus() == null)
            task.setStatus(Status.TODO);
        if(task.getPriority() == null)
            task.setPriority(Priority.LOW);
        checkDueDate(task);
    }

    private void checkDueDate(Task task) {
        if(task.getDueDate().isBefore(task.getCreatedOn()))
            throw new InvalidDueDateException("Due Date must be after the Creation Date");
    }
}
