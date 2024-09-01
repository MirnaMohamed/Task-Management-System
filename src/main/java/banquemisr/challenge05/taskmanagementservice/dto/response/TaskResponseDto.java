package banquemisr.challenge05.taskmanagementservice.dto.response;

import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponseDto(
        long id,
        String title,
        String description,
        Status status,
        Priority priority,
        LocalDateTime createdOn,
        LocalDateTime dueDate
) {
}
