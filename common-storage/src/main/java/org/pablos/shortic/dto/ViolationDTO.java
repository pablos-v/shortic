package org.pablos.shortic.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
@Data
@RequiredArgsConstructor
public final class ViolationDTO {

    private final String fieldName;
    private final String message;

}
