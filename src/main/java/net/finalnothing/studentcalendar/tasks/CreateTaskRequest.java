package net.finalnothing.studentcalendar.tasks;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateTaskRequest implements Serializable {
    private long eventId;
    private String title;
}
