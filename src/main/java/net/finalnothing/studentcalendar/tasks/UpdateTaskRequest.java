package net.finalnothing.studentcalendar.tasks;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UpdateTaskRequest implements Serializable {
    private String title;
    private boolean completed;
}
