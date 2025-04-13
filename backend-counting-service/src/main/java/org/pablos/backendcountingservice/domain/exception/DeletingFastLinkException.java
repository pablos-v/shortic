package org.pablos.backendcountingservice.domain.exception;

/**
 * Исключение, которое выбрасывается при попытке удаления быстрой ссылки.
 */
public class DeletingFastLinkException extends RuntimeException{

    /**
     * Конструктор класса.
     *
     * @param message сообщение об ошибке.
     */
    public DeletingFastLinkException(String message) {
        super(message);
    }
}
