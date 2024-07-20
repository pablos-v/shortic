package org.pablos;

import java.time.LocalDateTime;

public record LinkUnitDTO(String shortLink, String fullLink, boolean status, LocalDateTime createdAt, String password) {
}
