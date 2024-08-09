package org.pablos.shortic.util;

import org.pablos.shortic.IFullLink;
import org.pablos.shortic.IShortLink;
import org.pablos.shortic.dto.FastLinkDTO;
import org.pablos.shortic.exception.FullLinkNotProvidedException;
import org.pablos.shortic.exception.FullLinkSizeException;
import org.pablos.shortic.exception.LinkProcessingException;
import org.pablos.shortic.exception.ObjectNotProvidedException;

import java.security.SecureRandom;

public class CommonUtil {
    /**
     * Здесь задаётся длина сокращённой ссылки.
     */
    public static final String EXISTS = "This short link already exists";
    public static final int SHORT_LINK_LENGTH = 6;
    private static final int FULL_LINK_MAX_LENGTH = 4096;
    private static final String NOT_PROVIDED = "Link was not provided";
    private static final String BAD_SIZE = "Link length is wrong";
    private static final String CONTAINS_SPACES = "Link contains spaces";
    private static final String INVALID_CHARS = "Link contains invalid characters";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Генерирует случайную строку с длиной SHORT_LINK_LENGTH.
     * В строке CHARACTERS 62 символа, длина SHORT_LINK_LENGTH по умолчанию 6.
     * Получается всего 62^6 = 56 800 235 584 возможных ссылок.
     * @return Случайную строку длиной SHORT_LINK_LENGTH.
     */
    public static String generateShortLink() {
        StringBuilder shortLink = new StringBuilder(SHORT_LINK_LENGTH);
        for (int i = 0; i < SHORT_LINK_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            shortLink.append(CHARACTERS.charAt(index));
        }
        return shortLink.toString();
    }

    /**
     * Валидирует короткую ссылку. Выбрасывает исключения, которые обрабатываются дальше в сервисах.
     * @param dto короткая ссылка.
     * @throws LinkProcessingException если ссылка не валидна.
     */
    public static void validateDTOShortLink(IShortLink dto) throws LinkProcessingException, ObjectNotProvidedException{
        if (dto == null) {
            throw new ObjectNotProvidedException();
        }
        validateShortLink(dto.getShortLink());
    }

    public static void validateShortLink(String link) throws LinkProcessingException {
        if (link == null || link.isEmpty()) {
            throw new LinkProcessingException(NOT_PROVIDED);
        }
        if (link.length() > SHORT_LINK_LENGTH) {
            throw new LinkProcessingException(BAD_SIZE);
        }
        if (link.contains(" ")) {
            throw new LinkProcessingException(CONTAINS_SPACES);
        }
        if (!link.matches("^[a-zA-Z0-9]+$")) {
            throw new LinkProcessingException(INVALID_CHARS);
        }
    }

    public static void validateDTOFullLink(IFullLink dto) throws FullLinkNotProvidedException, FullLinkSizeException{
        if (dto == null || dto.getFullLink() == null || dto.getFullLink().isEmpty()) {
            throw new FullLinkNotProvidedException();
        }
        if (dto.getFullLink().length() > FULL_LINK_MAX_LENGTH) {
            throw new FullLinkSizeException();
        }
    }
}
