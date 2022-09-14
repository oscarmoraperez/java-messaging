package org.oka.javamessaging.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.model.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.awaitility.Awaitility.await;
import static org.oka.javamessaging.model.Event.EventType.TECH_TALK;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.profiles.active=kafka"})
@Testcontainers
@DirtiesContext
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class EventController_deleteEvent_InKafka_IT {
    static KafkaContainer KAFKA;

    static {
        KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        KAFKA.start();
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventRepository eventRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("kafka.bootstrapAddress", KAFKA::getBootstrapServers);
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        // Given
        String randomTitle = randomAlphabetic(10);
        Event event = Event.builder().eventType(TECH_TALK).title(randomTitle).place("a place")
                .speaker("a speaker").dateTime(LocalDate.now()).build();
        createEvent(event);
        await().atMost(10, SECONDS).until(() -> eventRepository.findByTitle(randomTitle).isPresent());
        Event savedEvent = eventRepository.findByTitle(randomTitle).orElseThrow();

        // When
        this.mockMvc
                .perform(delete("/events/" + savedEvent.getEventId())
                        .header("content-type", "application/json"))
                .andDo(print());

        // Then
        await().atMost(10, SECONDS).until(() -> eventRepository.findByTitle(randomTitle).isEmpty());
    }

    private void createEvent(final Event event) throws Exception {
        this.mockMvc
                .perform(post("/events")
                        .header("content-type", "application/json")
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print());
    }
}