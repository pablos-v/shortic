package org.pablos.backendcountingservice.domain.exception;

/**
 * Исключение, которое выбрасывается при попытке сохранения быстрой ссылки.
 */
public class SavingFastLinkException extends RuntimeException{

    /**
     * Конструктор класса.
     *
     * @param message сообщение об ошибке.
     */
    public SavingFastLinkException(String message) {
        super(message);
    }
}

