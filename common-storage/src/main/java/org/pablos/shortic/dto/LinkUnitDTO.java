package org.pablos.shortic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.pablos.shortic.IFullLink;
import org.pablos.shortic.IShortLink;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private List<ClickDTO> clicks;

    public String getFormattedTime(){
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}


