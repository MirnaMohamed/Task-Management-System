package banquemisr.challenge05.taskmanagementservice.dataloader;

import banquemisr.challenge05.taskmanagementservice.dto.request.UserLoginRequestDto;
import banquemisr.challenge05.taskmanagementservice.model.Task;
import banquemisr.challenge05.taskmanagementservice.model.User;
import banquemisr.challenge05.taskmanagementservice.model.enums.Gender;
import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Role;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import banquemisr.challenge05.taskmanagementservice.repository.TaskRepository;
import banquemisr.challenge05.taskmanagementservice.repository.UserRepository;
import banquemisr.challenge05.taskmanagementservice.service.AuthService;
import banquemisr.challenge05.taskmanagementservice.service.TaskService;
import banquemisr.challenge05.taskmanagementservice.service.UserService;
import banquemisr.challenge05.taskmanagementservice.web.controller.AuthController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUsersTable();
        seedTaskTable();
    }

    private void seedUsersTable() {
        List<User> userList = userRepository.findAll();
        userList.add(User.builder().email("miromirna00@gmail.com")
                .id(1L).password(encode("Mirna610!"))
                .firstName("Mirna")
                .lastName("Mohamed").gender(Gender.MALE)
                .role(Role.USER).build());
        userList.add(User.builder().email("miromirna@gmail.com")
                .id(2L).password(encode("Mirna000!"))
                .firstName("Mirna")
                .lastName("Mohamed").gender(Gender.FEMALE)
                .role(Role.USER).build());
        userList.add(User.builder().email("mirnaattallah00@gmail.com")
                .id(3L).password(encode("NewPassword123!"))
                .firstName("Mirna")
                .lastName("Attallah").gender(Gender.FEMALE)
                .role(Role.ADMIN).build());
        userRepository.saveAll(userList);
    }

    private void seedTaskTable() {


        List<Task> taskList = new ArrayList<>();
        User user1 = userRepository.findById(1L).orElseThrow(()->new RuntimeException("User Not Found"));
        User user2 = userRepository.findById(2L).orElseThrow(()->new RuntimeException("User Not Found"));
        User admin = userRepository.findById(3L).orElseThrow(()->new RuntimeException("User Not Found"));
        taskList.add(Task.builder().id(1L).title("Task 1")
                .status(Status.TODO)
                .dueDate(LocalDateTime.now().plusHours(2))
                .priority(Priority.HIGH)
                .createdOn(LocalDateTime.now())
                .user(user1)
                .description("Task description").build());
        taskList.add(Task.builder().id(2L).title("Task 2")
                .status(Status.IN_PROGRESS)
                .dueDate(LocalDateTime.now().plusDays(2))
                .priority(Priority.MEDIUM).user(user1)
                .createdOn(LocalDateTime.now())
                .description("Task description").build());
        taskList.add(Task.builder().id(3L).title("Task 3")
                .status(Status.DONE)
                .dueDate(LocalDateTime.now().plusMinutes(50))
                .priority(Priority.LOW).user(user2)
                .createdOn(LocalDateTime.now())
                .description("Task description").build());
        taskRepository.saveAll(taskList);
    }

    private String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
