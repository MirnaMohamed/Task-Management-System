package banquemisr.challenge05.taskmanagementservice.dto.request;

import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Validated
public class TaskRequestDto {
    @NotBlank
    String title;
    String description;
    Status status;
    Priority priority;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm[:ss][.SSS][.SS][.S]")
    LocalDateTime dueDate;
}
