package org.pablos.backendgivingservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Класс, для хранения в БД пары ссылок.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "fastLinks") // к какой коллекции обращаемся, создаст если её нет
@Schema(name = "FastLink", description = "Represents short and full links.")
public class FastLink {
    /**
     * Краткая ссылка, она же будет использоваться в качестве идентификатора.
     */
    @Id
    @Indexed(unique = true)
    private String shortLink;
    /**
     * Полная ссылка.
     */
    private String fullLink;
}