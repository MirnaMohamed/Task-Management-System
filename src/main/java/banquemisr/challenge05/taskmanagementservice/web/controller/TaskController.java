package banquemisr.challenge05.taskmanagementservice.web.controller;

import banquemisr.challenge05.taskmanagementservice.dto.request.TaskRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.TaskResponseDto;
import banquemisr.challenge05.taskmanagementservice.filter.TaskSearchDto;
import banquemisr.challenge05.taskmanagementservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/findByUserId")
    public ResponseEntity<?> getAllTasksByUser(@RequestParam(defaultValue = "0") long userId,
                                               @RequestParam(defaultValue = "0") int pageNo,
                                               @RequestParam(defaultValue = "5") int pageSize) {
        List<TaskResponseDto> taskList;
        if(userId == 0) {
            taskList = taskService.findAllTasksByUser(pageNo, pageSize);
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

    @PostMapping("assignTask/{userId}")
    public ResponseEntity<?> assignTaskToUser(@PathVariable Long userId, @RequestBody TaskRequestDto taskDto) {
        taskService.assignTaskToUser(userId, taskDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Task assigned to user successfully");
    }

    @PostMapping("assignTask/{userId}/{taskId}")
    public ResponseEntity<?> assignTaskToUserByTaskId(@PathVariable Long userId, @PathVariable long taskId) {
        taskService.assignTaskToUser(userId, taskId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Task assigned to user successfully");
    }

    @PostMapping("/search")
    public ResponseEntity<?> getStudentsByFilter(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int pageSize, @RequestBody List<TaskSearchDto> taskFilter)
    {
        return ResponseEntity.ok().body(taskService.findBySearchCriteria(page, pageSize, taskFilter));
        //Do not forget to add try-catch
    }

        @GetMapping("/title")
    public ResponseEntity<?> filterTasksByTitle(@RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "5") int pageSize,
                                                 @RequestParam String title) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.filterTasksByTitle(title, pageNumber, pageSize));
    }
}

