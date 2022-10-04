package net.qsef1256.dacobot.service.message.exception;

public class MessageApiException extends RuntimeException {

    public MessageApiException(String message) {
        super(message);
    }

    public MessageApiException(Throwable e) {
        super(e);
    }

}
