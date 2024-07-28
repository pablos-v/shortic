package org.pablos.shortic.dto;

import java.io.Serializable;

public record FastLinkDTO(String shortLink, String fullLink) implements Serializable {
}