package org.meli.challenge.exception;

public class UnknownURLException extends ManagedException {
    public UnknownURLException() {
        super("No se encontró la url solicitada.");
    }
}
