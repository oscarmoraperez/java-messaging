package org.oka.javamessaging.service;

import org.oka.javamessaging.model.Event;

public interface EventMessaging {
    void createEvent(Event event);

    void updateEvent(Event event);

    void deleteEvent(Long id);
}
