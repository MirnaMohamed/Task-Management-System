package banquemisr.challenge05.taskmanagementservice.service.impl;

import banquemisr.challenge05.taskmanagementservice.dto.request.ChangePasswordRequestDto;
import banquemisr.challenge05.taskmanagementservice.dto.response.UserInfoDto;
import banquemisr.challenge05.taskmanagementservice.mapper.UserMapper;
import banquemisr.challenge05.taskmanagementservice.model.User;
import banquemisr.challenge05.taskmanagementservice.model.enums.Role;
import banquemisr.challenge05.taskmanagementservice.repository.UserRepository;
import banquemisr.challenge05.taskmanagementservice.security.JwtService;
import banquemisr.challenge05.taskmanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public List<UserInfoDto> getAllUsers() {
        if(this.getCurrentUser().getRole() == Role.ADMIN){
            List<User> users = userRepository.findAll();
            return users.stream().map(userMapper::toUserInfoResponseDto).toList();
        }
        else
            throw new BadCredentialsException("Unauthorized access");
    }

    @Override
    public UserInfoDto extractUserFromUserId(Long userId) {
        User currentUser = this.getCurrentUser();
        if(currentUser.getRole() == Role.ADMIN || currentUser.getId() == userId){
            Optional<User> user = userRepository.findById(userId);
            UserInfoDto userInfoDto;
            if(user.isPresent()){
                userInfoDto = userMapper.toUserInfoResponseDto(user.get());
                return userInfoDto;
            }
            else
                throw new NotFoundException("User not found");
        }
        else
            throw new BadCredentialsException("Unauthorized");
    }

    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        Optional<User> user = userRepository.findByEmail(changePasswordRequestDto.email());
        User currentUser = this.getCurrentUser();
        if(currentUser.getEmail().equals(changePasswordRequestDto.email()) || currentUser.getRole() == Role.ADMIN){
            if(user.isPresent()){
                if(!changePasswordRequestDto.newPassword().equals(changePasswordRequestDto.confirmationPassword())){
                    throw new BadCredentialsException("Passwords are not the same!");
                }
                User savedUser = user.get();
                savedUser.setPassword(passwordEncoder.encode(changePasswordRequestDto.newPassword())); //update the password
                userRepository.save(savedUser);
            }
            else
                throw new NotFoundException("User not found");
        }
        else
            throw new BadCredentialsException("Unauthorized");
    }

}
