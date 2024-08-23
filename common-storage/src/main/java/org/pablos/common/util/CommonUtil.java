package org.pablos.common.util;

import org.pablos.common.IFullLink;
import org.pablos.common.IShortLink;
import org.pablos.common.exception.*;

import java.security.SecureRandom;
import java.util.Base64;

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
    static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    public static final String PASSWORD_NOT_MATCHES_PATTERN = "Password must be exactly 5 digits";

    /**
     * Генерирует случайную строку с длиной SHORT_LINK_LENGTH.
     * В строке CHARACTERS 62 символа, длина SHORT_LINK_LENGTH по умолчанию 6.
     * Получается всего 62^6 = 56 800 235 584 возможных ссылок.
     * @return Случайную строку длиной SHORT_LINK_LENGTH.
     */
    public static String generateShortLink() {
        StringBuilder sb = new StringBuilder(SHORT_LINK_LENGTH);
        for (int i = 0; i < SHORT_LINK_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
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

    public static void validateDTOFullLink(IFullLink dto) throws FullLinkNotProvidedException, FullLinkSizeException,
            FullLinkFormatException {
        if (dto == null || dto.getFullLink() == null || dto.getFullLink().isEmpty()) {
            throw new FullLinkNotProvidedException();
        }
        validateFullLink(dto.getFullLink());
    }

    public static void validateFullLink(String link) throws FullLinkSizeException, FullLinkFormatException {
        if (link.length() > FULL_LINK_MAX_LENGTH) {
            throw new FullLinkSizeException();
        }
        if (!link.matches("^(http://|https://)[^\\s_]+(\\.[^\\s_]+)+$")) {
            throw new FullLinkFormatException();
        }
    }

    public static void validatePassword(String password) throws WrongInputException {
        if (password == null || !password.matches("\\d{5}")) {
            throw new WrongInputException(PASSWORD_NOT_MATCHES_PATTERN);
        }
    }

    public static String encodePassword(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String decodePassword(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }

    public static String clearShortLink(String shortLink) {
        shortLink = shortLink.trim();
        int length = shortLink.length();
        return shortLink.substring(length - SHORT_LINK_LENGTH, length);
    }
}

