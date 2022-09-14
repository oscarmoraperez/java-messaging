package org.oka.javamessaging.service.activemq;

import lombok.RequiredArgsConstructor;
import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.service.EventMessaging;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static org.oka.javamessaging.service.activemq.configuration.Config.*;

/**
 * Produces and sends events into a ActiveMQ queue.
 */
@Component
@RequiredArgsConstructor
@Profile("activemq")
public class EventProducer implements EventMessaging {
    private final JmsTemplate jmsTemplate;

    @Override
    public void createEvent(Event event) {

        jmsTemplate.convertAndSend(CREATE_EVENT_REQUEST, event);
    }

    @Override
    public void updateEvent(Event event) {

        jmsTemplate.convertAndSend(UPDATE_EVENT_REQUEST, event);
    }

    @Override
    public void deleteEvent(Long id) {

        jmsTemplate.convertAndSend(DELETE_EVENT_REQUEST, id);
    }
}