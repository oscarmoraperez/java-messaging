package org.oka.javamessaging.service;

import org.oka.javamessaging.model.Event;

import java.util.List;

public interface EventService {
    void createEvent(Event event);

    void updateEvent(Long id, Event event);

    Event getEvent(Long id);

    void deleteEvent(Long id);

    List<Event> getAllEvents();
}
