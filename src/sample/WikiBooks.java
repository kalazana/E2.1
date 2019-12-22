package sample;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class WikiBooks extends Medium implements Serializable {


    private static final long serialVersionUID = 1L;

    private String Verfasser = "";
    private String sAenderungsDatum = null;
    private ArrayList<String> arr_Kapitel = new ArrayList<String>();
    private String sRegal = null;
    private boolean bIP = false;

    @SuppressWarnings("null")
    public void setVerfasser(final String _Verfasser) {
        if((_Verfasser != null) || (!(_Verfasser.isEmpty())))
            this.Verfasser = _Verfasser;
        else
            throw new IllegalArgumentException();
    }

    //Deklaration

    public String getVerfasser() {
        return Verfasser;
    }





    // @return the bIP
    public boolean isIP() {
        return bIP;
    }

    // @param bIP the bIP to set

    public void setIP(boolean _bIP) {
        this.bIP = _bIP;
    }



    // @return dAenderungsDatum
    public String getAenderungsDatum() {
        return sAenderungsDatum;
    }



    //AenderungsDatum the AenderungsDatum to set
    public void setAenderungsDatum(String _sAenderungsDatum) {
        this.sAenderungsDatum = _sAenderungsDatum;
    }



    //@return the arr_Kaptil
    public ArrayList<String> getKapitel() {
        return arr_Kapitel;
    }



    //@param arr_Kaptil the arr_Kaptil to set
    public void setKapitel(ArrayList<String> _arr_Kapitel) {
        this.arr_Kapitel = _arr_Kapitel;
    }

    //@return the sRegal
    public String getRegal() {
        return sRegal;
    }

    //@param sRegal the sRegal to set
    public void setRegal(String _sRegal) {
        this.sRegal = _sRegal;
    }


    /* Konstruktor
     * Speicherung der übergebenen Daten mittels Setter-Methoden.
     * @_Titel
     * @_Erscheinungsjahr
     * @_Verlag
     * @_ISBN
     * @_Verfasser
     */
    public WikiBooks(String _Titel, String _sAenderungsdatum, String _sRegal, ArrayList<String> _arr_Kapitel, String _Verfasser) {
        super(_Titel);
        setRegal(_sRegal);
        setKapitel(_arr_Kapitel);
        setAenderungsDatum(_sAenderungsdatum);
        setVerfasser(_Verfasser);
    }

    public static String convertTime(String _sTime) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long ts = System.currentTimeMillis();
        Date localTime = new Date(ts);

        _sTime = _sTime.replace('T', ' ');
        _sTime = _sTime.replace('Z', ' ');

        try {
            Date utcTime = formatDate.parse(_sTime);

            Date gmtTime = new Date((utcTime.getTime() + TimeZone.getDefault().getOffset(localTime.getTime())));
            Date gmtDate = new Date((utcTime.getTime() + TimeZone.getDefault().getOffset(localTime.getTime())));

            return new SimpleDateFormat("dd.MM.yyyy").format(gmtDate) + " um "
                    + new SimpleDateFormat("HH:mm").format(gmtTime) + " Uhr";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Fehler";

    }

    /*
     * calculateRepraesentation
     * Ausgabe einer vordefinierten Stringkette
     *
     * @return StringKette <String> (Titel+ Regal + Kapitel + Änderungungsdatum + Verfasser)
     */
    public String calculateRepraesentation() {
        StringBuilder str =new StringBuilder().append("Titel: \""+ Titel + "\" " +
                "\nRegal: " + sRegal + "\nKapitel:");
        for (String s : arr_Kapitel) {
            str.append("\n"+s);
        }

        str.append("\nLetzte Änderung am "+convertTime(sAenderungsDatum));
        if (bIP)
            str.append("\nIP: " + Verfasser);
        else
            str.append("\nUrheber: " + Verfasser);
        return str.toString();
    }



    @Override
    public String calculateRepresentation() {
        // TODO Auto-generated method stub
        return new String (Titel);
    }
}


