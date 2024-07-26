package org.pablos.backendgivingservice.service.exception_handling.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * DTO для передачи в удобном виде информации об ошибках валидации входных данных
 */
@Getter
@RequiredArgsConstructor
public class ObjectViolationDTO {

    private final String object;
    private final String message;

}
