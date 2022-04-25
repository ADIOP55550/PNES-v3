package pl.edu.ur.pnes.petriNet.simulator;

public class TransitionCannotBeActivatedException extends Exception {
    public TransitionCannotBeActivatedException() {
    }

    public TransitionCannotBeActivatedException(String message) {
        super(message);
    }

    public TransitionCannotBeActivatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransitionCannotBeActivatedException(Throwable cause) {
        super(cause);
    }

    public TransitionCannotBeActivatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
