package org.pablos.backendgivingservice.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, для хранения в БД пары ссылок.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
//TODO заменить Table на @Document(collection = "fastLinks") и убрать @Entity // к какой коллекции обращаемся, создаст если её нет
@Table(name = "fast_links")
@Schema(name = "FastLink", description = "Represents short and full links.")
public class FastLink {
    /**
     * Краткая ссылка, она же будет использоваться в качестве идентификатора.
     */
    @Id
// TODO   @Indexed(unique = true)
    @Column(name = "short_link")
    private String shortLink;
    /**
     * Полная ссылка.
     */
    @Column(name = "full_link")
    private String fullLink;
}

