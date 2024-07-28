package org.pablos.backendcountingservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс, представляющий запись в таблице "clicks".
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "clicks")
public class Click {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "click_time")
    private LocalDateTime clickTime;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "referrer")
    private String referrer;

    @Column(name = "language")
    private String language;

    public Click(LocalDateTime clickTime, String ipAddress, String userAgent, String referrer, String language) {
        this.clickTime = clickTime;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.referrer = referrer;
        this.language = language;
    }
}

