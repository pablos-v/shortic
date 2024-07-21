package org.pablos.backendcountingservice.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

//@Component TODO а надо?
public class ShortLinkGenerator {
    public static final int LENGTH = 6; // Длина идентификатора TODO перенести в environment или брать с КлаудКонфига

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, LENGTH);
    }
}
