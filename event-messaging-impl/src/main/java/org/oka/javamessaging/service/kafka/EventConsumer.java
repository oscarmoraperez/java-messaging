package org.oka.javamessaging.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.model.EventRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.oka.javamessaging.service.kafka.configuration.Config.*;

/**
 * Consumes events from Kafka queue.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("kafka")
public class EventConsumer {
    private final EventRepository eventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = CREATE_EVENT_REQUEST, groupId = "app")
    public void listenCreateEventRequest(Event event) {
        log.info("Event received: " + event + " (" + CREATE_EVENT_REQUEST + ") ");

        Event createEvent = eventRepository.save(event);

        kafkaTemplate.send(CREATE_EVENT_NOTIFICATION, createEvent).addCallback(new FutureCallback());
    }

    @KafkaListener(topics = UPDATE_EVENT_REQUEST, groupId = "app")
    public void listenUpdateEventRequest(Event event) {
        log.info("Event received: " + event + " (" + UPDATE_EVENT_REQUEST + ") ");

        Event current = eventRepository.findById(event.getEventId()).orElseThrow();
        current.setEventType(event.getEventType());
        current.setPlace(event.getPlace());
        current.setSpeaker(event.getSpeaker());
        current.setDateTime(event.getDateTime());
        current.setTitle(event.getTitle());

        eventRepository.save(current);

        kafkaTemplate.send(CREATE_EVENT_NOTIFICATION, current).addCallback(new FutureCallback());
    }

    @KafkaListener(topics = DELETE_EVENT_REQUEST, groupId = "app")
    public void listenDeleteEventRequest(Long eventId) {
        log.info("Event received: " + eventId + " (" + DELETE_EVENT_REQUEST + ") ");

        eventRepository.deleteById(eventId);

        kafkaTemplate.send(DELETE_EVENT_NOTIFICATION, eventId).addCallback(new FutureCallback());
    }
}
