package org.pablos.backendcountingservice.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, представляющий запись в таблице "link_units".
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "link_units")
public class LinkUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * Сокращённая ссылка.
     */
    @Column(name = "short_link")
    private String shortLink;
    /**
     * Пароль для доступа к статистике кликов.
     */
    @Column(name = "password")
    private String password;
    /**
     * Полная ссылка.
     */
    @Column(name = "full_link")
    private String fullLink;
    /**
     * Дата создания ссылки.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    /**
     * Статус ссылки (активна или нет).
     */
    @Column(name = "status")
    private boolean status;
    /**
     * Список кликов по ссылке.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_id")
    private List<Click> clicks;
}
