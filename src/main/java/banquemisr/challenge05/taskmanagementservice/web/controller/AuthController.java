package banquemisr.challenge05.taskmanagementservice.web.controller;

import banquemisr.challenge05.taskmanagementservice.dto.request.UserLoginRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.request.UserRegisterRequestDto;
import banquemisr.challenge05.taskmanagementservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDto registerRequestDto) {
        authService.registerUser(registerRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("User registered successfully ");
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRegisterRequestDto registerRequestDto) {
        authService.registerAdmin(registerRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Admin registered successfully ");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.login(loginRequestDto));
    }
}
