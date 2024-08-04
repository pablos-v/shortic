package org.pablos.backendcountingservice.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий запись в таблице "link_units".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "shortic", name = "link_units")
public class LinkUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "short_link")
    private String shortLink;

    @Column(name = "full_link")
    private String fullLink;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "active")
    private boolean active;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_id")
    private List<Click> clicks;

    public LinkUnit(String shortLink, String fullLink) {
        this.shortLink = shortLink;
        this.fullLink = fullLink;
        this.createdAt = LocalDateTime.now();
        this.active = false;
        this.clicks = new ArrayList<>();
    }
}
