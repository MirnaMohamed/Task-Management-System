package banquemisr.challenge05.taskmanagementservice.service.impl;

import banquemisr.challenge05.taskmanagementservice.dto.request.UserLoginRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.request.UserRegisterRequestDto;
import banquemisr.challenge05.taskmanagementservice.exception.UserAlreadyExistsException;
import banquemisr.challenge05.taskmanagementservice.mapper.UserMapper;
import banquemisr.challenge05.taskmanagementservice.model.User;
import banquemisr.challenge05.taskmanagementservice.model.enums.Role;
import banquemisr.challenge05.taskmanagementservice.repository.UserRepository;
import banquemisr.challenge05.taskmanagementservice.security.JwtService;
import banquemisr.challenge05.taskmanagementservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void existsByEmailThrowsUserAlreadyExistsException(String email) {
        if(userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException("Email address already exists.");
    }

    @Override
    public void registerUser(UserRegisterRequestDto requestDto) {
        existsByEmailThrowsUserAlreadyExistsException(requestDto.email());
        User user = userMapper.registerDtotoUser(requestDto);
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Override
    public void registerAdmin(UserRegisterRequestDto requestDto) {
        existsByEmailThrowsUserAlreadyExistsException(requestDto.email());
        User user = userMapper.registerDtotoUser(requestDto);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Override
    public String login(UserLoginRequestDto loginRequest) {
        User savedUser = findUserByEmail(loginRequest.email());
        String savedUserPassword = passwordEncoder.encode(savedUser.getPassword());
        checkPasswordsMatch(loginRequest.password(), savedUserPassword);
        String jwtToken = jwtService.generateToken(savedUser);
        savedUser.setAccessToken(jwtToken);
        savedUser = userRepository.save(savedUser);
        return savedUser.getAccessToken();
    }

    private void checkPasswordsMatch(String pass1,String pass2){
        if (!passwordEncoder.matches(pass1, pass2)) {
            throw new BadCredentialsException("Invalid password");
        }
    }

    private User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()->new NotFoundException("Email address not found."));
    }
}
