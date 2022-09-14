package org.oka.javamessaging.service;

import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.model.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class where all the functionalities are gathered.
 */
@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventMessaging eventMessaging;
    @Autowired
    private EventRepository eventRepository;

    @Override
    public void createEvent(Event event) {
        eventMessaging.createEvent(event);
    }

    @Override
    public void updateEvent(Long id, Event event) {
        event.setEventId(id);
        eventMessaging.updateEvent(event);
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteEvent(Long id) {
        eventMessaging.deleteEvent(id);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        eventRepository.findAll().forEach(events::add);
        return events;
    }
}
