package org.pablos.shortic.dto;

import java.time.LocalDateTime;
import java.util.List;

public record LinkUnitDTO(long id, String shortLink, String password, String fullLink, LocalDateTime createdAt, boolean status, List<?> clicks) {}


