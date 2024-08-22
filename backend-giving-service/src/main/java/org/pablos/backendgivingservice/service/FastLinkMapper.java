package org.pablos.backendgivingservice.service;

import org.pablos.backendgivingservice.entity.FastLink;

public class FastLinkMapper {
    public static org.pablos.shortic.dto.FastLinkDTO toDTO (FastLink fl) {
        return new org.pablos.shortic.dto.FastLinkDTO(fl.getShortLink(), fl.getFullLink());
    }

    public static FastLink toEntity (org.pablos.shortic.dto.FastLinkDTO fl) {
        return new FastLink(fl.getShortLink(), fl.getFullLink());
    }
}
