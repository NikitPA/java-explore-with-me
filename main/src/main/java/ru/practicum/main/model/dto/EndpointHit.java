package ru.practicum.main.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {

    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

}
