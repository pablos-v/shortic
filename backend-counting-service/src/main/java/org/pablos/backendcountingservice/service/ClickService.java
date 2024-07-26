package org.pablos.backendcountingservice.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pablos.ClickDTO;
import org.pablos.backendcountingservice.repository.ClickRepository;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service
public class ClickService {

    private final ClickRepository clickRepository;

    public void createClick(ClickDTO click) {
        validateClick(click); //TODO: validate
        /**
         *                 "link", link,
         *                 "ipAddress", ipAddress,
         *                 "userAgent", userAgent,
         *                 "referer", referer,
         *                 "language", language
         */
    }

    private void validateClick(ClickDTO click) {

    }
}
