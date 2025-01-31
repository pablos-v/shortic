package org.pablos.common.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public final class ViolationDTO {

    private final String fieldName;
    private final String message;

}
