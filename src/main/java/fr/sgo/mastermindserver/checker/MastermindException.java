package fr.sgo.mastermindserver.checker;

public class MastermindException extends RuntimeException {
    private final String message;

    public MastermindException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
