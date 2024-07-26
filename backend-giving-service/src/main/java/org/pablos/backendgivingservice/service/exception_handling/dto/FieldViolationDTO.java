package org.pablos.backendgivingservice.service.exception_handling.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * DTO для передачи в удобном виде информации об ошибках валидации входных данных
 */
@Getter
@RequiredArgsConstructor
public class FieldViolationDTO {

    private final String fieldName;
    private final String message;

}
