package org.oka.javamessaging.service.kafka.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

/**
 * Configuration of Kafka consumers and producers
 */
@EnableKafka
@Configuration
@Profile("kafka")
public class Config {

    public static final String CREATE_EVENT_NOTIFICATION = "create-event-notification";
    public static final String UPDATE_EVENT_NOTIFICATION = "update-event-notification";
    public static final String DELETE_EVENT_NOTIFICATION = "delete-event-notification";
    public static final String CREATE_EVENT_REQUEST = "create-event_request";
    public static final String UPDATE_EVENT_REQUEST = "update-event_request";
    public static final String DELETE_EVENT_REQUEST = "delete-event_request";

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("org.oka.javamessaging.model");

        Map<String, Object> configProps = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                GROUP_ID_CONFIG, "app",
                AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress));
    }

    @Bean
    public NewTopic createEventNotification() {
        return new NewTopic(CREATE_EVENT_NOTIFICATION, 1, (short) 1);
    }

    @Bean
    public NewTopic updateEventNotification() {
        return new NewTopic(UPDATE_EVENT_NOTIFICATION, 1, (short) 1);
    }

    @Bean
    public NewTopic deleteEventNotification() {
        return new NewTopic(DELETE_EVENT_NOTIFICATION, 1, (short) 1);
    }

    @Bean
    public NewTopic createEventRequest() {
        return new NewTopic(CREATE_EVENT_REQUEST, 1, (short) 1);
    }

    @Bean
    public NewTopic updateEventRequest() {
        return new NewTopic(UPDATE_EVENT_REQUEST, 1, (short) 1);
    }

    @Bean
    public NewTopic deleteEventRequest() {
        return new NewTopic(DELETE_EVENT_REQUEST, 1, (short) 1);
    }
}
