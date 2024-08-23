package org.pablos.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pablos.common.IFullLink;
import org.pablos.common.IShortLink;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class LinkUnitDTO implements IShortLink, IFullLink {

    private long id;
    private String shortLink;
    private String password;
    private String fullLink;
    private LocalDateTime createdAt;
    private boolean active;

    public String getFormattedTime(){
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}


