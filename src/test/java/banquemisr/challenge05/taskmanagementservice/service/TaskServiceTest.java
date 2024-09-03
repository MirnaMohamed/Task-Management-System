package banquemisr.challenge05.taskmanagementservice.service;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.TaskResponseDto;
import banquemisr.challenge05.taskmanagementservice.mapper.TaskMapper;
import banquemisr.challenge05.taskmanagementservice.model.Task;
import banquemisr.challenge05.taskmanagementservice.model.User;
import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Role;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import banquemisr.challenge05.taskmanagementservice.repository.TaskRepository;
import banquemisr.challenge05.taskmanagementservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskServiceTest {
    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private SecurityContext securityContext;
    @MockBean
    private Authentication authentication;
    @MockBean
    private User userMock;
//    @MockBean
    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @BeforeEach
    void init() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userMock);
        when(userMock.getId()).thenReturn(1L);
    }

    @Autowired
    public TaskServiceTest(TaskService taskService,
                           TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper= taskMapper;
    }

    @Test
    public void whenfindAllTasksByUser_thenReturnListOfTasks(){
        // Task list Mockup
        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userMock));
        Task task1 = Task.builder()
                .title("Task 1")
                .description("task description").priority(Priority.MEDIUM)
                .dueDate(LocalDateTime.now().plusHours(1))
                .status(Status.IN_PROGRESS).user(userMock).build();
        Task task2 = Task.builder()
                .title("Task 2")
                .description("task 2 description").priority(Priority.HIGH)
                .dueDate(LocalDateTime.now().plusHours(1))
                .status(Status.TODO).user(userMock).build();
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(task1, task2));
        List<TaskResponseDto> tasks = new ArrayList<>();
        //convert paged content to list
        taskPage.getContent().forEach(task -> tasks.add(taskMapper.toTaskResponseDto(task)));
        Pageable paging = PageRequest.of(0, 5);

        //Repository mock
        when(taskRepository.findByUserOrderByDueDate(userMock, paging)).thenReturn(taskPage);

        // Assertion Test
        Assertions.assertEquals(taskService.findAllTasksByUser(1L, 0, 5),
                tasks);
    }

    @Test
    public void whenDeleteTask_thenReturnVoid() {
        Task task = Task.builder()
                .title("Task 1")
                .description("task description").priority(Priority.MEDIUM)
                .dueDate(LocalDateTime.now().plusHours(1))
                .status(Status.IN_PROGRESS).user(userMock).build();
        when(userMock.getRole()).thenReturn(Role.ADMIN);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        //Assertion Test
        taskService.deleteTaskById(1L);
        Mockito.verify(taskRepository).deleteById(1L);
    }
}
