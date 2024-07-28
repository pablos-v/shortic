package org.pablos.shortic.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.pablos.shortic.IShortLink;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public final class FastLinkDTO implements Serializable, IShortLink {

    private final String shortLink;

    private final String fullLink;

    // TODO нужен для org.pablos.frontendservice.controller.FrontController.mainPage() - или переделать?
    public FastLinkDTO() {
        this.shortLink = null;
        this.fullLink = null;
    }
}