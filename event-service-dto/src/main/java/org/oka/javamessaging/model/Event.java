package org.oka.javamessaging.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@ToString
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long eventId;
    @NonNull
    private String title;
    @NonNull
    private String place;
    @NonNull
    private String speaker;
    @NonNull
    private EventType eventType;
    @NonNull
    private LocalDate dateTime;

    public enum EventType {
        WORKSHOP, TECH_TALK
    }
}
