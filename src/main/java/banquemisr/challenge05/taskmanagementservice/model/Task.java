package banquemisr.challenge05.taskmanagementservice.model;

import banquemisr.challenge05.taskmanagementservice.model.enums.Priority;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50)
    private String title;
    private String description;
    @Enumerated(value = EnumType.STRING)
    @Column(length = 25)
    private Status status;
    @Enumerated(value = EnumType.STRING)
    @Column(length = 30)
    private Priority priority;
    private LocalDateTime createdOn;
    private LocalDateTime dueDate;
}
