package org.pablos.backendcountingservice.domain.exception;

/**
 * Исключение, которое выбрасывается при попытке обновления быстрой ссылки.
 */
public class UpdatingFastLinkException extends RuntimeException{

    /**
     * Конструктор класса.
     *
     * @param message сообщение об ошибке.
     */
    public UpdatingFastLinkException(String message) {
        super(message);
    }
}
