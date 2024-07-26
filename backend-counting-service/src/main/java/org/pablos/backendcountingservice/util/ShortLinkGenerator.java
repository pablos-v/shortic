package org.pablos.backendcountingservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@Component TODO а надо? мож конфиг и бинами сделать?
public class ShortLinkGenerator {

    @Value("${properties.shortLinkLength}")
    private static int LINK_LENGTH;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Генерирует случайную строку с длиной LINK_LENGTH.
     * В строке CHARACTERS 62 символа, длина LINK_LENGTH по умолчанию 6
     * 62^6 = 56 800 235 584 возможных комбинаций
     * @return Случайную строку длиной LINK_LENGTH.
     */
    public static String generateShortLink() {
        StringBuilder shortLink = new StringBuilder(LINK_LENGTH);
        for (int i = 0; i < LINK_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            shortLink.append(CHARACTERS.charAt(index));
        }
        return shortLink.toString();
    }
}
