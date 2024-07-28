package org.pablos.shortic.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.shortic.IShortLink;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public final class LinkUnitDTO implements IShortLink {

    private final long id;
    private final String shortLink;
    private final String password;
    private final String fullLink;
    private final LocalDateTime createdAt;
    private final boolean active;
    private final List<?> clicks;

}


