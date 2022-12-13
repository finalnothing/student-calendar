package net.finalnothing.studentcalendar.courses;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class CreateCourseRequest implements Serializable {
    private String name;
    private String color;
    private String weekday;
    private LocalTime fromTime;
    private LocalTime toTime;
    private LocalDate validUntil;
    private String location;
}
