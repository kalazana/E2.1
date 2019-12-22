package sample;

import java.io.IOException;

public class DuplicateException extends IOException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DuplicateException(String _sMsg) {
        super(_sMsg);
    }

    public String getMessage() {
        return "HINWEIS: Mehrfache Eintraege gefunden!!!";
    }
}
