package com.trackmyteam.backend.service;

import com.trackmyteam.backend.model.Task;
import com.trackmyteam.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    // Check every hour
    @Scheduled(cron = "0 0 * * * *")
    public void sendDeadlineReminders() {
        List<Task> tasks = taskRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime warningTime = now.plusHours(24);

        for (Task task : tasks) {
            if (task.getDeadline() != null && task.getDeadline().isBefore(warningTime)
                    && task.getDeadline().isAfter(now)) {
                emailService.sendEmail(
                        task.getAssignedUser().getEmail(),
                        "Deadline Reminder: " + task.getTitle(),
                        "Your task '" + task.getTitle() + "' is due soon at " + task.getDeadline());
            }
        }
    }
}
