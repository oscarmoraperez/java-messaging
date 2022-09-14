package org.oka.javamessaging.service.activemq;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.model.EventRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import static org.oka.javamessaging.service.activemq.configuration.Config.*;

/**
 * Consumes events from ActiveMQ queue.
 */
@Component
@RequiredArgsConstructor
@Profile("activemq")
@Slf4j
public class EventConsumer {
    private final EventRepository eventRepository;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = CREATE_EVENT_REQUEST, containerFactory = "myFactory")
    public void listenCreateEventRequest(Event event) {
        log.info("Event received: " + event + " (" + CREATE_EVENT_REQUEST + ") ");

        Event createEvent = eventRepository.save(event);

        jmsTemplate.convertAndSend(CREATE_EVENT_NOTIFICATION, createEvent);
    }

    @JmsListener(destination = UPDATE_EVENT_REQUEST)
    public void listenUpdateEventRequest(Event event) {
        log.info("Event received: " + event + " (" + UPDATE_EVENT_REQUEST + ") ");

        Event current = eventRepository.findById(event.getEventId()).orElseThrow();
        current.setEventType(event.getEventType());
        current.setPlace(event.getPlace());
        current.setSpeaker(event.getSpeaker());
        current.setDateTime(event.getDateTime());
        current.setTitle(event.getTitle());

        Event updateEvent = eventRepository.save(current);

        jmsTemplate.convertAndSend(UPDATE_EVENT_NOTIFICATION, updateEvent);
    }

    @JmsListener(destination = DELETE_EVENT_REQUEST)
    @SendTo(DELETE_EVENT_NOTIFICATION)
    public void listenDeleteEventRequest(Long eventId) {
        log.info("Event received: " + eventId + " (" + DELETE_EVENT_REQUEST + ") ");

        eventRepository.deleteById(eventId);
    }
}