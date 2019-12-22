package sample;

import java.io.Serializable;

public class Buch extends Medium implements Serializable {


    private static final long serialVersionUID = 1L;

    //Speicherung der übergebenen Daten mittels Setter-Methoden
    public Buch(final String _Titel,final int _Erscheinungsjahr,final String _Verlag,final String _ISBN,final String _Verfasser)
    {
        super(_Titel);
        setErscheinungsjahr(_Erscheinungsjahr);
        setVerlag(_Verlag);
        setISBN(_ISBN);
        setVerfasser(_Verfasser);
    }

    //Declaration
    protected static int Erscheinungsjahr = 0;
    private String Verlag = "";
    private String ISBN = "";
    protected String Verfasser = "";

    //Ausgabe einer vordefinierten Stringkette
    public String calculateRepresentation()
    {
        StringBuilder sb = new StringBuilder();

        if (!getTitel().equals(""))
        {
            sb.append("Titel: ").append(this.getTitel());
        }
        if (getErscheinungsjahr()!=0)
        {
            sb.append("\nErscheinungsjahr: ").append(this.getErscheinungsjahr());
        }
        if (!getVerlag().equals(""))
        {
            sb.append("\nVerlag: ").append(this.getVerlag());
        }
        if (getISBN()!="")
        {
            sb.append("\nISBN: ").append(this.getISBN());
        }
        if (!getVerfasser().equals(""))
        {
            sb.append("\nVerfasser: ").append(this.getVerfasser());
        }
        return sb.toString();
    }

    public int getErscheinungsjahr() {
        return Erscheinungsjahr;
    }

    public void setErscheinungsjahr(final int _Erscheinungsjahr) {
        if ((_Erscheinungsjahr > 0) & (_Erscheinungsjahr < 2018))
            this.Erscheinungsjahr = _Erscheinungsjahr;
        else
            throw new IllegalArgumentException();
    }

    public String getVerlag() {
        return Verlag;
    }
    @SuppressWarnings("null")
    public void setVerlag(final String _Verlag) {
        if((_Verlag != null)|| (!(_Verlag.isEmpty()))) {
            this.Verlag = _Verlag;}
        else
            throw new IllegalArgumentException();
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {

        String ISBN_copy=iSBN;

        ISBN_copy=iSBN.replace("-", "");

        if(ISBN_copy.length()==10)
        {
            int ISBN_array[] = new int[10];

            for(int i=0;i<ISBN_copy.length();i++){

                ISBN_array[i]=Integer.parseInt(ISBN_copy.substring(i, i+1));

            }

            if(checkISBN10(ISBN_array))
            {
                ISBN=iSBN;
            }
            else
            {
                System.err.println("Ungültige ISBN!");
            }


        }
        else if(ISBN_copy.length()==13)
        {
            int ISBN_array[] = new int[13];

            for(int i=0;i<ISBN_copy.length();i++){

                ISBN_array[i]=Integer.parseInt(ISBN_copy.substring(i, i+1));
            }

            if(checkISBN13(ISBN_array))

            {

                ISBN=iSBN;

            }
            else
            {
                System.err.println("Ungültige ISBN!");
            }
        }
        else
        {
            System.out.println(ISBN);
        }

    }

    public String getVerfasser() {
        return Verfasser;
    }

    @SuppressWarnings("null")
    public void setVerfasser(final String _Verfasser) {
        if((_Verfasser != null) || (!(_Verfasser.isEmpty())))
            this.Verfasser = _Verfasser;
        else
            throw new IllegalArgumentException();
    }

    public static boolean checkISBN10(int[] isbn) {
        int sum = 0;

        for (int i = 1; i <= isbn.length; i++) {
            sum += i * isbn[i - 1];

        }

        if (sum % 11 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkISBN13(int[] isbn) {
        int sum = 0;

        for (int i = 1; i < isbn.length; i++) {
            if (i % 2 == 0) {
                sum += isbn[i - 1] * 3;
            } else {
                sum += isbn[i - 1];
            }
        }

        int lastDigit = sum % 10;
        int check = (10 - lastDigit) % 10;

        if (isbn[isbn.length - 1] == check) {
            return true;
        } else {
            return false;
        }
    }
}