package banquemisr.challenge05.taskmanagementservice.web.controller;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import banquemisr.challenge05.taskmanagementservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
//    @Value("${page.size}")
//    private int pageSize;

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable long taskId) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskById(taskId));
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks(@RequestParam(defaultValue = "0") int pageNumber,
                                         @RequestParam(defaultValue = "5") int pageSize ) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks(pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody @Valid TaskRequestDto taskDto) {
        taskService.addTask(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDto);
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

    @GetMapping("/priority")
    public ResponseEntity<?> filterTasksByPriority(@RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "5") int pageSize,
                                                 @RequestParam Priority priority) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.filterTasksByPriority(priority, pageNumber, pageSize));
    }

    @GetMapping("/status")
    public ResponseEntity<?> filterTasksByStatus(@RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "5") int pageSize,
                                                 @RequestParam Status status) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.filterTasksByStatus(status, pageNumber, pageSize));
    }

    @GetMapping("/title")
    public ResponseEntity<?> filterTasksByTitle(@RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "5") int pageSize,
                                                 @RequestParam String title) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.filterTasksByTitle(title, pageNumber, pageSize));
    }
}
