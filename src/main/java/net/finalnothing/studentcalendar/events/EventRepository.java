package net.finalnothing.studentcalendar.events;

import net.finalnothing.studentcalendar.model.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long> {
    Optional<Event> findById(long id);
    List<Event> findAllByCourse_CreatedBy_Id(long createdBy);
}
