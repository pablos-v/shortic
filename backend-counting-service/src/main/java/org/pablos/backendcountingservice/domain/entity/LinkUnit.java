package org.pablos.backendcountingservice.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, представляющий запись в таблице "link_units".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "link_units")
public class LinkUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "short_link")
    private String shortLink;

    @Column(name = "password")
    private String password;

    @Column(name = "full_link")
    private String fullLink;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private boolean status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_id")
    private List<Click> clicks;
}
