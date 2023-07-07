package de.quoss.quarkus.narayana.helper;

public class NarayanaHelperException extends RuntimeException {

    public NarayanaHelperException(final String s) {
        super(s);
    }

    public NarayanaHelperException(final Throwable t) {
        super(t);
    }

    public NarayanaHelperException(final String s, final Throwable t) {
        super(s, t);
    }

}
