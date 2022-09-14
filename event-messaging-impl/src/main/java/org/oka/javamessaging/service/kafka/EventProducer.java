package org.oka.javamessaging.service.kafka;

import lombok.RequiredArgsConstructor;
import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.service.EventMessaging;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.oka.javamessaging.service.kafka.configuration.Config.*;

/**
 * Produces and sends events into a Kafka queue.
 */
@RequiredArgsConstructor
@Service
@Profile("kafka")
public class EventProducer implements EventMessaging {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void createEvent(Event event) {

        kafkaTemplate.send(CREATE_EVENT_REQUEST, event).addCallback(new FutureCallback());
    }

    @Override
    public void updateEvent(Event event) {

        kafkaTemplate.send(UPDATE_EVENT_REQUEST, event).addCallback(new FutureCallback());
    }

    @Override
    public void deleteEvent(Long id) {

        kafkaTemplate.send(DELETE_EVENT_REQUEST, id).addCallback(new FutureCallback());
    }
}
