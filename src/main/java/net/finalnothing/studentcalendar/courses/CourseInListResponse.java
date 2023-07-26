package net.finalnothing.studentcalendar.courses;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.finalnothing.studentcalendar.model.Course;
import net.finalnothing.studentcalendar.model.User;
import net.finalnothing.studentcalendar.users.UserResponse;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CourseInListResponse {
    private Long id;
    private String name;
    private String color;
    private UserResponse createdBy;
    private String weekday;
    private LocalTime fromTime;
    private LocalTime toTime;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private String location;

    public CourseInListResponse(Course course) {
        id = course.getId();
        name = course.getName();
        color = course.getColor();
        createdBy = new UserResponse(course.getCreatedBy());
        weekday = course.getWeekday();
        fromTime = course.getFromTime();
        toTime = course.getToTime();
        validFrom = course.getValidFrom();
        validUntil = course.getValidUntil();
        location = course.getLocation();
    }
}
