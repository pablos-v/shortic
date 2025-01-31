package org.pablos.common.dto;

import org.pablos.common.IFullLink;
import org.pablos.common.IShortLink;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class FastLinkDTO implements Serializable, IShortLink, IFullLink {

    private String shortLink;

    private String fullLink;

}