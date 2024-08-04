package org.pablos.shortic.dto;

import lombok.*;
import org.pablos.shortic.IFullLink;
import org.pablos.shortic.IShortLink;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class FastLinkDTO implements Serializable, IShortLink, IFullLink {

    private String shortLink;

    private String fullLink;

    // TODO нужен для org.pablos.frontendservice.controller.FrontController.mainPage() - или переделать?
//    public FastLinkDTO() {
//        this.shortLink = null;
//        this.fullLink = null;
//    }
}