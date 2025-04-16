package org.pablos.common.util;

import org.pablos.common.IFullLink;
import org.pablos.common.IShortLink;
import org.pablos.common.exception.*;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Вспомогательный класс, который содержит общие методы.
 */
public class CommonUtil {

    public static final String EXISTS = "This short link already exists";
    public static int SHORT_LINK_LENGTH;
    public static int FULL_LINK_MAX_LENGTH;
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
    public static void validateDTOShortLink(IShortLink dto) throws LinkProcessingException, ObjectNotProvidedException {
        if (dto == null) {
            throw new ObjectNotProvidedException();
        }
        validateShortLink(dto.getShortLink());
    }

    /**
     * Валидирует короткую ссылку и выбрасывает исключение с соответствующим сообщением.
     */
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

    /**
     * Валидирует полную ссылку и выбрасывает исключения, которые обрабатываются дальше в сервисах.
     * @param dto полная ссылка.
     */
    public static void validateDTOFullLink(IFullLink dto) throws FullLinkNotProvidedException, FullLinkSizeException, FullLinkFormatException {
        if (dto == null || dto.getFullLink() == null || dto.getFullLink().isEmpty()) {
            throw new FullLinkNotProvidedException();
        }
        validateFullLink(dto.getFullLink());
    }

    /**
     * Валидирует полную ссылку и выбрасывает исключения, которые обрабатываются выше.
     * @param link полная ссылка.
     */
    public static void validateFullLink(String link) throws FullLinkSizeException, FullLinkFormatException {
        if (link.length() > FULL_LINK_MAX_LENGTH) {
            throw new FullLinkSizeException();
        }
        if (!link.matches("^(http://|https://)[^\\s_]+(\\.[^\\s_]+)+$")) {
            throw new FullLinkFormatException();
        }
    }

    /**
     * Валидирует пароль пользователя. Пароль должен состоять из 5 цифр.
     */
    public static void validatePassword(String password) throws WrongInputException {
        if (password == null || !password.matches("\\d{5}")) {
            throw new WrongInputException(PASSWORD_NOT_MATCHES_PATTERN);
        }
    }

    /**
     * Кодирует пароль пользователя в строку в формате Base64.
     */
    public static String encodePassword(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    /**
     * Декодирует пароль пользователя из строки в формате Base64.
     */
    public static String decodePassword(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }

    /**
     * Получая ссылку вида https://shortL возвращает только shortL
     */
    public static String clearShortLink(String shortLink) {
        shortLink = shortLink.trim();
        int length = shortLink.length();
        return shortLink.substring(length - SHORT_LINK_LENGTH, length);
    }
}

