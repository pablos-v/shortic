package org.pablos.backendcountingservice.service.exception_handling.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {

    private final List<ViolationDTO> violations;

}
