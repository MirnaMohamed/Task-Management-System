package banquemisr.challenge05.taskmanagementservice.web.controller;

import banquemisr.challenge05.taskmanagementservice.dto.request.ChangePasswordRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.UserInfoDto;
import banquemisr.challenge05.taskmanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<UserInfoDto> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(users);
    }
    @GetMapping("/changePassword")
    public ResponseEntity<?> changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        userService.changePassword(changePasswordRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Password changed successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfoByUserId(@PathVariable long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.extractUserFromUserId(userId));
    }
}
