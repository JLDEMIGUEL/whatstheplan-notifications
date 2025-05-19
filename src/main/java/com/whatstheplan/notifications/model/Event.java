package com.whatstheplan.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private Duration duration;
    private String location;
    private String organizerUsername;
}
