package banquemisr.challenge05.taskmanagementservice.dto.request;

import banquemisr.challenge05.taskmanagementservice.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserRegisterRequestDto (
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
        String email,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
                message = "Password must meet this criteria: Not less than 8 characters with at least one special character and contains uppercase and lowercase characters")
        String password,
        @NotBlank Gender gender
) {
}
