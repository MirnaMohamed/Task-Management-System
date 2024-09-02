package banquemisr.challenge05.taskmanagementservice.dto.response;

import banquemisr.challenge05.taskmanagementservice.model.enums.Gender;
import banquemisr.challenge05.taskmanagementservice.model.enums.Role;
import lombok.Builder;

@Builder
public record UserInfoDto (
     long id,
     String firstName,
     String lastName,
     String email,
     Role role,
     Gender gender,
     int tasksNum
){ }
