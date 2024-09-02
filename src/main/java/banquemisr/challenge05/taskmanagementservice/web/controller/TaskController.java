package banquemisr.challenge05.taskmanagementservice.web.controller;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.TaskResponseDto;
import banquemisr.challenge05.taskmanagementservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.version}/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable long taskId) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskById(taskId));
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks(@RequestParam(defaultValue = "0") int pageNumber,
                                         @RequestParam(defaultValue = "5") int pageSize ) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks(pageNumber, pageSize));
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<?> getAllTasksNoPagination() {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks());
    }

    @GetMapping("/admin/{userId}")
    public ResponseEntity<?> getAllTasksByUser(@PathVariable Optional<Long> userId,
                                               @RequestParam(defaultValue = "0") int pageNo,
                                               @RequestParam(defaultValue = "5") int pageSize) {
        List<TaskResponseDto> taskList;
        if(userId.isPresent()) {
            taskList = taskService.findAllTasksByUser(userId.get(), pageNo, pageSize);
        }
        else
            taskList = taskService.findAllTasksByUser(pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(taskList);
    }

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody @Valid TaskRequestDto taskDto) {
        taskService.addTask(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskRequestDto taskDto) {
        taskService.updateTask(id, taskDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Task updated successfully ");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    @GetMapping("/priority")
//    public ResponseEntity<?> filterTasksByPriority(@RequestParam(defaultValue = "0") int pageNumber,
//                                                 @RequestParam(defaultValue = "5") int pageSize,
//                                                 @RequestParam Priority priority) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(taskService.filterTasksByPriority(priority, pageNumber, pageSize));
//    }
//
//    @GetMapping("/status")
//    public ResponseEntity<?> filterTasksByStatus(@RequestParam(defaultValue = "0") int pageNumber,
//                                                 @RequestParam(defaultValue = "5") int pageSize,
//                                                 @RequestParam Status status) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(taskService.filterTasksByStatus(status, pageNumber, pageSize));
//    }
//
//    @GetMapping("/title")
//    public ResponseEntity<?> filterTasksByTitle(@RequestParam(defaultValue = "0") int pageNumber,
//                                                 @RequestParam(defaultValue = "5") int pageSize,
//                                                 @RequestParam String title) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(taskService.filterTasksByTitle(title, pageNumber, pageSize));
//    }
}
