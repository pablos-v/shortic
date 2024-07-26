package org.pablos.backendgivingservice.service;

import org.pablos.FastLinkDTO;
import org.pablos.backendgivingservice.domain.entity.FastLink;

public class FastLinkMapper {
    public static FastLinkDTO toDTO (FastLink fl) {
        return new FastLinkDTO(fl.getShortLink(), fl.getFullLink());
    }

    public static FastLink toEntity (FastLinkDTO fl) {
        return new FastLink(fl.shortLink(), fl.fullLink());
    }
}
