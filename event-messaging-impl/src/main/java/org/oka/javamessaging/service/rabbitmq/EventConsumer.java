package org.oka.javamessaging.service.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.model.EventRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.oka.javamessaging.service.kafka.configuration.Config.*;
import static org.oka.javamessaging.service.rabbitmq.configuration.Config.CREATE_EVENT_REQUEST_QUEUE;
import static org.oka.javamessaging.service.rabbitmq.configuration.Config.DELETE_EVENT_REQUEST_QUEUE;

/**
 * Consumes events from RabbitMQ queue.
 */
@Component
@Profile("rabbitmq")
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {
    private final EventRepository eventRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = {CREATE_EVENT_REQUEST_QUEUE})
    public void listenCreateEventRequest(Event event) {
        log.info("Event received: " + event + " (" + CREATE_EVENT_REQUEST + ") ");

        Event createEvent = eventRepository.save(event);

        rabbitTemplate.convertAndSend(CREATE_EVENT_NOTIFICATION, createEvent);
    }

    @RabbitListener(queues = {CREATE_EVENT_REQUEST_QUEUE})
    public void listenUpdateEventRequest(Event event) {
        log.info("Event received: " + event + " (" + UPDATE_EVENT_REQUEST + ") ");

        Event current = eventRepository.findById(event.getEventId()).orElseThrow();
        current.setEventType(event.getEventType());
        current.setPlace(event.getPlace());
        current.setSpeaker(event.getSpeaker());
        current.setDateTime(event.getDateTime());
        current.setTitle(event.getTitle());

        Event updatedEvent = eventRepository.save(current);

        rabbitTemplate.convertAndSend(UPDATE_EVENT_NOTIFICATION, updatedEvent);
    }

    @RabbitListener(queues = {DELETE_EVENT_REQUEST_QUEUE})
    public void listenDeleteEventRequest(Long eventId) {
        log.info("Event received: " + eventId + " (" + DELETE_EVENT_REQUEST + ") ");

        eventRepository.deleteById(eventId);

        rabbitTemplate.convertAndSend(DELETE_EVENT_NOTIFICATION, eventId);
    }
}