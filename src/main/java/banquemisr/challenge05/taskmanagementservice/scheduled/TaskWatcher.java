package banquemisr.challenge05.taskmanagementservice.scheduled;

import banquemisr.challenge05.taskmanagementservice.model.Task;
import banquemisr.challenge05.taskmanagementservice.model.enums.Status;
import banquemisr.challenge05.taskmanagementservice.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class TaskWatcher {
    private final TaskRepository taskRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public TaskWatcher(final TaskRepository taskRepository, final JavaMailSender mailSender) {
        this.taskRepository = taskRepository;
        this.mailSender = mailSender;
    }
    @Scheduled(cron = "59 * * * * *")
    public void sendAlertEmails(){
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(task ->  {
            if(checkDueDate(task.getDueDate()) && !task.getStatus().equals(Status.DONE))
                sendMail(task);
            log.info("Sending alerts for the following task user:{}", task.getUser().getUsername());
        });
    }

    private boolean checkDueDate(LocalDateTime dueDate){
        if(!dueDate.minusHours(1).isAfter(LocalDateTime.now())){
            return true; //The due date is in less than one hour
        }
        else
            return false;
    }

    @Async
    void sendMail(Task task){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(task.getUser().getUsername());
        message.setSubject("Task Deadline is coming up.");
        message.setText("Try to finish Task number: " + task.getId() + " with title: " + task.getTitle() +
                " as it appears that it's not finished yet and it's dueDate in an hour so hurry up!! ");

        mailSender.send(message);
    }
}
