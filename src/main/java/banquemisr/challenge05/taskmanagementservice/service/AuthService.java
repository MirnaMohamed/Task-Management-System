package banquemisr.challenge05.taskmanagementservice.service;

import banquemisr.challenge05.taskmanagementservice.dto.request.UserLoginRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.request.UserRegisterRequestDto;

public interface AuthService {
    void existsByEmailThrowsUserAlreadyExistsException(String email);
    void registerUser(UserRegisterRequestDto requestDto);
    void registerAdmin(UserRegisterRequestDto requestDto);
    String login(UserLoginRequestDto loginRequest);
}
