package org.pablos.backendgivingservice.domain.entity;

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
//TODO заменить Table на @Document(collection = "fastLinks") // к какой коллекции обращаемся, создаст если её нет
@Table(name = "fast_links")
public class FastLink {
    /**
     * Краткая ссылка, она же будет использоваться в качестве идентификатора.
     */
    @Id
    @Column(name = "short_link")
    private String shortLink;
    /**
     * Полная ссылка.
     */
    @Column(name = "full_link")
    private String fullLink;
}

