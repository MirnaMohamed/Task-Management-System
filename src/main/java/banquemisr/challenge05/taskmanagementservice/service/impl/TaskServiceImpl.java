package banquemisr.challenge05.taskmanagementservice.service.impl;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.TaskResponseDto;
import banquemisr.challenge05.taskmanagementservice.exception.InvalidDueDateException;
import banquemisr.challenge05.taskmanagementservice.mapper.TaskMapper;
import banquemisr.challenge05.taskmanagementservice.model.Task;
import banquemisr.challenge05.taskmanagementservice.model.User;
import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Role;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import banquemisr.challenge05.taskmanagementservice.repository.TaskRepository;
import banquemisr.challenge05.taskmanagementservice.repository.UserRepository;
import banquemisr.challenge05.taskmanagementservice.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void addTask(TaskRequestDto taskDto) {
        Task task = taskMapper.toTask(taskDto);
        task.setCreatedOn(LocalDateTime.now());
        task.setUser(this.getCurrentUser());
        validateTask(task);
        taskRepository.save(task);
    }

    @Override
    public void updateTask(long id, TaskRequestDto taskDto) {
        Optional<Task> taskData = taskRepository.findById(id);
        if(taskData.isPresent()) {
            Task task = taskData.get();
            User user = this.getCurrentUser();
            if(task.getUser().getId() == user.getId()) {
                Task updatedTask = taskMapper.updateTaskFromTaskRequestDto(taskDto, task);
                taskRepository.save(updatedTask);
            }
            else
                throw new BadCredentialsException("Unauthorized user");
        }
        else
            throw new NotFoundException("Task with id: " + id +" not found. Try adding a new Task.");
        }

    @Override
    public List<TaskResponseDto> getAllTasks(int pageNo, int pageSize) {
        User user = this.getCurrentUser();
        if(user.getRole() == Role.ADMIN) {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<Task> pagedResult = taskRepository.findAll(paging);
            List<Task> tasks = pagedResult.getContent();
            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
        }
        else
            throw new BadCredentialsException("Unauthorized user");
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        User user = this.getCurrentUser();
        if (user.getRole() == Role.ADMIN) {
            List<Task> tasks = taskRepository.findAll();
            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
        }
        else
            throw new BadCredentialsException("Unauthorized user");
    }

    @Override
    public List<TaskResponseDto> findAllTasksByUser(int pageNo, int pageSize) {
        User user = this.getCurrentUser();
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Task> taskPage = taskRepository.findByUserOrderByDueDate(user, paging);
        List<Task> tasks = taskPage.getContent();
        return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
    }

    @Override
    public List<TaskResponseDto> findAllTasksByUser(long userId, int pageNo, int pageSize) {
        User currentUser = this.getCurrentUser();
        if(currentUser.getRole() == Role.ADMIN || userId == currentUser.getId()) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                Pageable paging = PageRequest.of(pageNo, pageSize);
                Page<Task> taskPage = taskRepository.findByUserOrderByDueDate(user.get(), paging);
                List<Task> tasks = taskPage.getContent();
                return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
            } else
                throw new NotFoundException("User with id: " + userId + "  is not found.");
        }
        else
            throw new BadCredentialsException("Unauthorized user");
    }

    @Override
    public TaskResponseDto getTaskById(long id) {
        User user = this.getCurrentUser();
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Task with id " + id + " not found")
        );

        if(task.getUser().getId() == user.getId() || user.getRole() == Role.ADMIN)
            return taskMapper.toTaskResponseDto(task);
        else
            throw new BadCredentialsException("Unauthorized user");
    }

    @Override
    public void deleteTaskById(long id) {
        User user = this.getCurrentUser();
        Optional<Task> taskData = taskRepository.findById(id);
        if(taskData.isPresent()){
            if(user.getRole().equals(Role.ADMIN) || taskData.get().getUser().equals(user))
                taskRepository.deleteById(id);
            else
                throw new BadCredentialsException("You do not have permission to delete this task.");
        }
        else
            throw new NotFoundException("Task with id " + id + " not found in database. Could not delete task");
    }

    @Override
    public void assignTaskToUser(long userId, long taskId) {
        User currentUser = this.getCurrentUser();
        if(currentUser.getRole() == Role.ADMIN) {
            Optional<Task> task = taskRepository.findById(taskId);
            if (task.isPresent()) {
                Optional<User> user = userRepository.findById(userId);
                user.ifPresent(value -> task.get().setUser(value));
                }
            }
        else
            throw new BadCredentialsException("You do not have permission to assign this task to a user");
    }

    @Override
    public void assignTaskToUser(long userId, TaskRequestDto taskRequestDto) {
        User currentUser = this.getCurrentUser();
        if(currentUser.getRole() == Role.ADMIN) {
            Task task = taskMapper.toTask(taskRequestDto);
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()) {
                task.setUser(user.get());
                task.setCreatedOn(LocalDateTime.now());
                validateTask(task);
                taskRepository.save(task);
            }
            else
                throw new NotFoundException("User with id: " + userId + " is not found.");
        }
        else
            throw new BadCredentialsException("You do not have permission to assign this task to a user");
    }
//    @Override
//    public List<TaskResponseDto> filterTasksByPriority(Priority priority, int pageNo, int pageSize) {
//        Pageable paging = PageRequest.of(pageNo, pageSize);
//        Page<Task> pagedResult = taskRepository.findByPriority(priority, paging);
//
//        if(pagedResult.hasContent()) {
//            List<Task> tasks= pagedResult.getContent();
//            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
//        } else {
//            return new ArrayList<>();
//        }
//    }
//
//    public List<TaskResponseDto> filterTasksByStatus(Status status, int pageNo, int pageSize) {
//        Pageable paging = PageRequest.of(pageNo, pageSize);
//        Page<Task> pagedResult = taskRepository.findByStatus(status, paging);
//
//        if(pagedResult.hasContent()) {
//            List<Task> tasks= pagedResult.getContent();
//            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
//        } else {
//            return new ArrayList<>();
//        }
//    }
//
//    @Override
//    public List<TaskResponseDto> filterTasksByTitle(String title, int pageNo, int pageSize) {
//        Pageable paging = PageRequest.of(pageNo, pageSize);
//        Page<Task> pagedResult = taskRepository.findByTitleContainingIgnoreCase(title, paging);
//
//        if(pagedResult.hasContent()) {
//            List<Task> tasks= pagedResult.getContent();
//            return tasks.stream().map(taskMapper::toTaskResponseDto).toList();
//        } else {
//            return new ArrayList<>();
//        }
//    }

    private void validateTask(Task task) {
        if(task.getStatus() == null)
            task.setStatus(Status.TODO);
        if(task.getPriority() == null)
            task.setPriority(Priority.LOW);
        checkDueDate(task);
    }

    private void checkDueDate(Task task) {
        if(!task.getDueDate().isAfter(task.getCreatedOn()))
            throw new InvalidDueDateException("Due Date must be after the Creation Date");
    }
}
