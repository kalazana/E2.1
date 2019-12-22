package sample;

public class Duplicate extends Exception {

    private static final long serialVersionUID = 1L;

    public Duplicate() {
        super();
    }

    public Duplicate(final String message) {
        super(message);
    }

    public Duplicate(final String message, final Throwable cause) {
        super(message, cause);
    }

    public Duplicate(final Throwable cause) {
        super(cause);
    }
}

