package org.pablos.backendgivingservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Класс, представляющий запись в таблице "fastlinks".
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "fastlinks") // к какой коллекции обращаемся, создаст если её нет
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

