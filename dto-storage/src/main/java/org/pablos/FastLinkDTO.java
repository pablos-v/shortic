package org.pablos;

import java.io.Serializable;

public record FastLinkDTO(String shortLink, String fullLink) implements Serializable {
}