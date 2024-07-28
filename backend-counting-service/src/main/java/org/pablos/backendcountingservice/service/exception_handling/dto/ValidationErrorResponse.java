package org.pablos.backendcountingservice.service.exception_handling.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pablos.shortic.dto.ViolationDTO;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {

    private final List<ViolationDTO> violations;

}
