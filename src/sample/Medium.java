package sample;

import java.io.Serializable;

public abstract class Medium implements Comparable<Medium>, Serializable {


    private static final long serialVersionUID = 1L;

    //Feld Definition
    protected String Titel = "";

    public Medium(final String title) {
        setTitel(title);
    }

    public String getTitel() {
        return Titel;
    }

    @SuppressWarnings("null")
    public void setTitel(final String _Titel) {
        if((_Titel != null) || (!(_Titel.isEmpty())))
            this.Titel = _Titel;
        else
            throw new IllegalArgumentException("Geben Sie den Titel des Mediums an!");
    }

    //abstrakte Ausgabe Methode
    public abstract String calculateRepresentation();

    @Override
    public int compareTo(final Medium x) {
        if (this.getTitel().equals(x.getTitel())) {
            return this.getClass().getName().compareTo(x.getClass().getName());
        }
        return this.getTitel().compareTo(x.getTitel());
    }
}

