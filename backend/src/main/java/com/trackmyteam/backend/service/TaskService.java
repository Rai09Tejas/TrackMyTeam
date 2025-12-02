package com.trackmyteam.backend.service;

import com.trackmyteam.backend.model.Task;
import com.trackmyteam.backend.model.TaskStatus;
import com.trackmyteam.backend.model.User;
import com.trackmyteam.backend.repository.TaskRepository;
import com.trackmyteam.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Task createTask(Task task) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        User user = userRepository.findByUsername(username).orElseThrow();
        task.setAssignedUser(user);
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    public List<Task> getMyTasks() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        User user = userRepository.findByUsername(username).orElseThrow();
        return taskRepository.findByAssignedUserId(user.getId());
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setDeadline(taskDetails.getDeadline());
        task.setStatus(taskDetails.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
