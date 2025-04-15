package org.pablos.backendgivingservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Класс для хранения в БД пары ссылок.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "FastLink", description = "Represents short and full links.")
@Entity
@Table(schema = "fast", name = "fast_links")
public class FastLink {
    /**
     * Краткая ссылка, она же будет использоваться в качестве идентификатора.
     */
    @Id
    @Column(name = "short_link")
    private String shortLink;

    @Column(name = "full_link")
    private String fullLink;
}