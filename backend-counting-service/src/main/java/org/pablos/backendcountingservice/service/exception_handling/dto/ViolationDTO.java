package org.pablos.backendcountingservice.service.exception_handling.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ViolationDTO {

    private final String fieldName;
    private final String message;

}
