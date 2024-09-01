package banquemisr.challenge05.taskmanagementservice.dto.request;

import banquemisr.challenge05.taskmanagementservice.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserLoginRequestDto (
    @NotBlank String email,
    @NotBlank String password
){}
