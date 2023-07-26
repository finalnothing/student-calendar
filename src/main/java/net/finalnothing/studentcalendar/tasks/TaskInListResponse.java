package net.finalnothing.studentcalendar.tasks;

import lombok.Data;
import net.finalnothing.studentcalendar.model.Task;

@Data
public class TaskInListResponse {
    private Long id;

    private String title;
    private Boolean completed;

    public TaskInListResponse(Task task) {
        id = task.getId();
        title = task.getTitle();
        completed = task.getCompleted();
    }
}
