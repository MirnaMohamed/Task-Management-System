package banquemisr.challenge05.taskmanagementservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/user")
public class UserController {
//    private final UserService userService;
//
//    @GetMapping("/allUsers")
//    public ResponseEntity<?> getAllUsers() {
//        List<UserInfoDto> users = userService.getAllUsers();
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(users);
//    }
}
