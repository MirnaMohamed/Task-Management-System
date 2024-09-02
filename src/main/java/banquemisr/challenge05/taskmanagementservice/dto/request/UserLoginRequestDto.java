package banquemisr.challenge05.taskmanagementservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserLoginRequestDto (
    @NotBlank String email,
    @NotBlank String password
){}
