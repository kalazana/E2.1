package sample;

public class myWebExceptions extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public myWebExceptions () {
        super("Konnte Wikibooks nicht erreichen!");
    }
}
