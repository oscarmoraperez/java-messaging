package org.oka.javamessaging.service.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.service.EventMessaging;
import org.oka.javamessaging.service.rabbitmq.configuration.Config;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static org.oka.javamessaging.service.rabbitmq.configuration.Config.*;

/**
 * Produces and sends events into a RabbitMQ queue.
 */
@RequiredArgsConstructor
@Service
@Profile("rabbitmq")
public class EventProducer implements EventMessaging {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void createEvent(Event event) {
        rabbitTemplate.convertAndSend(CREATE_EVENT_REQUEST_QUEUE, event);
    }

    @Override
    public void updateEvent(Event event) {
        rabbitTemplate.convertAndSend(UPDATE_EVENT_REQUEST_QUEUE, event);
    }

    @Override
    public void deleteEvent(Long id) {
        rabbitTemplate.convertAndSend(DELETE_EVENT_REQUEST_QUEUE, id);
    }
}
