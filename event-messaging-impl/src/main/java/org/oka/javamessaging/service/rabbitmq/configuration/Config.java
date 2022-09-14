package org.oka.javamessaging.service.rabbitmq.configuration;


import org.oka.javamessaging.service.rabbitmq.EventConsumer;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuration of RabbitMQ consumers and producers
 */
@EnableRabbit
@Configuration
@Profile("rabbitmq")
public class Config {
    public static final String EXCHANGE_EVENT = "exchange-direct";
    public static final String CREATE_EVENT_NOTIFICATION_QUEUE = "create-event-notification-queue";
    public static final String CREATE_EVENT_NOTIFICATION_EXCHANGE = "create-event-notification-exchange";
    public static final String UPDATE_EVENT_NOTIFICATION_QUEUE = "update-event-notification-queue";
    public static final String UPDATE_EVENT_NOTIFICATION_EXCHANGE = "update-event-notification-exchange";
    public static final String DELETE_EVENT_NOTIFICATION_QUEUE = "delete-event-notification-queue";
    public static final String DELETE_EVENT_NOTIFICATION_EXCHANGE = "delete-event-notification-exchange";
    public static final String CREATE_EVENT_REQUEST_QUEUE = "create-event_request-queue";
    public static final String CREATE_EVENT_REQUEST_EXCHANGE = "create-event_request-exchange";
    public static final String UPDATE_EVENT_REQUEST_QUEUE = "update-event_request-queue";
    public static final String UPDATE_EVENT_REQUEST_EXCHANGE = "update-event_request-exchange";
    public static final String DELETE_EVENT_REQUEST_QUEUE = "delete-event_request-queue";
    public static final String DELETE_EVENT_REQUEST_EXCHANGE = "delete-event_request-exchange";

    @Bean(name = "create-event-notification")
    Queue createEventNotification() {
        return new Queue(CREATE_EVENT_NOTIFICATION_QUEUE, false);
    }
//
//    @Bean
//    TopicExchange createEventNotificationExchange() {
//        return new TopicExchange(CREATE_EVENT_NOTIFICATION_EXCHANGE);
//    }
//
//    @Bean
//    Binding createEventNotification(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("create.event.notification.#");
//    }


    @Bean(name = "update-event-notification")
    Queue updateEventNotification() {
        return new Queue(UPDATE_EVENT_NOTIFICATION_QUEUE, false);
    }

//    @Bean
//    TopicExchange updateEventNotificationExchange() {
//        return new TopicExchange(UPDATE_EVENT_NOTIFICATION_EXCHANGE);
//    }
//
//    @Bean
//    Binding updateEventNotification(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("update.event.notification.#");
//    }

    @Bean(name = "delete-event-notification")
    Queue deleteEventNotification() {
        return new Queue(DELETE_EVENT_NOTIFICATION_QUEUE, false);
    }

//    @Bean
//    TopicExchange deleteEventNotificationExchange() {
//        return new TopicExchange(DELETE_EVENT_NOTIFICATION_EXCHANGE);
//    }
//
//    @Bean
//    Binding deleteEventNotification(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("delete.event.notification.#");
//    }


    @Bean(name = "create-event-request")
    Queue createEventRequest() {
        return new Queue(CREATE_EVENT_REQUEST_QUEUE, false);
    }

//    @Bean
//    TopicExchange createEventRequestExchange() {
//        return new TopicExchange(CREATE_EVENT_REQUEST_EXCHANGE);
//    }
//
//    @Bean
//    Binding createEventRequest(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("create.event.request.#");
//    }

    @Bean(name = "update-event-request")
    Queue updateEventRequest() {
        return new Queue(UPDATE_EVENT_REQUEST_QUEUE, false);
    }

//    @Bean
//    TopicExchange updateEventRequestExchange() {
//        return new TopicExchange(UPDATE_EVENT_REQUEST_EXCHANGE);
//    }
//
//    @Bean
//    Binding updateEventRequest(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("update.event.request.#");
//    }

    @Bean(name = "delete-event-request")
    Queue deleteEventRequest() {
        return new Queue(DELETE_EVENT_REQUEST_QUEUE, false);
    }

//    @Bean
//    TopicExchange deleteEventRequestExchange() {
//        return new TopicExchange(DELETE_EVENT_REQUEST_EXCHANGE);
//    }
//
//    @Bean
//    Binding deleteEventRequest(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("delete.event.request.#");
//    }

    @Bean
    public DirectExchange exchangeEvent() {
        return new DirectExchange(EXCHANGE_EVENT);
    }

    @Bean
    public Declarables directExchangeBindings(
            DirectExchange exchangeEvent,
            @Qualifier("create-event-notification") Queue createEventNotification,
            @Qualifier("update-event-notification") Queue updateEventNotification,
            @Qualifier("delete-event-notification") Queue deleteEventNotification,
            @Qualifier("create-event-request") Queue createEventRequest,
            @Qualifier("update-event-request") Queue updateEventRequest,
            @Qualifier("delete-event-request") Queue deleteEventRequest) {
        return new Declarables(
                BindingBuilder.bind(createEventNotification).to(exchangeEvent).with(CREATE_EVENT_NOTIFICATION_EXCHANGE),
                BindingBuilder.bind(updateEventNotification).to(exchangeEvent).with(UPDATE_EVENT_NOTIFICATION_EXCHANGE),
                BindingBuilder.bind(deleteEventNotification).to(exchangeEvent).with(DELETE_EVENT_NOTIFICATION_EXCHANGE),
                BindingBuilder.bind(createEventRequest).to(exchangeEvent).with(CREATE_EVENT_REQUEST_EXCHANGE),
                BindingBuilder.bind(updateEventRequest).to(exchangeEvent).with(UPDATE_EVENT_REQUEST_EXCHANGE),
                BindingBuilder.bind(deleteEventRequest).to(exchangeEvent).with(DELETE_EVENT_REQUEST_EXCHANGE)
        );
    }

    @Bean
    SimpleMessageListenerContainer listenerCreateEvent(ConnectionFactory connectionFactory,
                                                       @Qualifier("createEventListener") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(CREATE_EVENT_REQUEST_QUEUE);
        container.setMessageListener(listenerAdapter);

        return container;
    }

    @Bean
    SimpleMessageListenerContainer listenerUpdateEvent(ConnectionFactory connectionFactory,
                                                       @Qualifier("updateEventListener") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(UPDATE_EVENT_REQUEST_QUEUE);
        container.setMessageListener(listenerAdapter);

        return container;
    }


    @Bean
    SimpleMessageListenerContainer listenerDeleteEvent(ConnectionFactory connectionFactory,
                                                       @Qualifier("deleteEventListener") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(DELETE_EVENT_REQUEST_QUEUE);
        container.setMessageListener(listenerAdapter);

        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, Jackson2ObjectMapperBuilder builder) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    @Qualifier("createEventListener")
    MessageListenerAdapter createEventListenerAdapter(EventConsumer receiver) {
        return new MessageListenerAdapter(receiver, "createEvent");
    }

    @Bean
    @Qualifier("updateEventListener")
    MessageListenerAdapter updateEventListenerAdapter(EventConsumer receiver) {
        return new MessageListenerAdapter(receiver, "updateEvent");
    }

    @Bean
    @Qualifier("deleteEventListener")
    MessageListenerAdapter deleteEventListenerAdapter(EventConsumer receiver) {
        return new MessageListenerAdapter(receiver, "deleteEvent");
    }
}
