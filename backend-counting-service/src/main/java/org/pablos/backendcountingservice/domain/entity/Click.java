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
    /**
     * Идентификатор ссылки, к которой относится клик.
     */
    @Column(name = "link_id")
    private Long linkId;
    /**
     * Дата клика.
     */
    @Column(name = "click_date")
    private LocalDateTime clickDate;
    /**
     * IP-адрес пользователя, который произвел клик.
     */
    @Column(name = "ip_address")
    private String ipAddress;
    /**
     * User-Agent пользователя, который произвел клик.
     */
    @Column(name = "user_agent")
    private String userAgent;
    /**
     * Реферер пользователя, который произвел клик.
     */
    @Column(name = "referrer")
    private String referrer;
    /**
     * Язык пользователя, который произвел клик.
     */
    @Column(name = "language")
    private String language;
}

