package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_map")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UrlMapping {

    @Id
    @Column(length = 16, nullable = false)
    private String shortKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String longUrl;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiry;

    private long clicks = 0L;
}

