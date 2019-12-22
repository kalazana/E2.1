package sample;

import java.io.*;
import java.util.ArrayList;

public class BinaryPersistency implements Persistency, Serializable {


    private static final long serialVersionUID = 1L;
    private ArrayList<Zettelkasten> Binaere = new ArrayList<Zettelkasten>();;

    public void save (final Zettelkasten ZK,final String Dateiname){

        ObjectOutputStream OOu = null;
        FileOutputStream FOu = null;

        try{

            FOu=new FileOutputStream(Dateiname);
            OOu=new ObjectOutputStream(FOu);
            OOu.writeObject(ZK);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                assert OOu != null;
                OOu.flush();
                OOu.close();
                FOu.flush();
                FOu.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Zettelkasten load(final String Dateiname){

        Zettelkasten ZK = null;
        FileInputStream FIn = null;
        ObjectInputStream OIn = null;

        try
        {
            FIn=new FileInputStream(Dateiname);
            OIn=new ObjectInputStream(FIn);
            ZK=(Zettelkasten) OIn.readObject();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            //System.out.println("Class not found");
            c.printStackTrace();
        } finally {
            try {
                assert OIn != null;
                OIn.close();
                FIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ZK;
    }

    public ArrayList<Zettelkasten> getBinaere() {
        return Binaere;
    }

    public void setBinaere(ArrayList<Zettelkasten> binaere) {
        Binaere = binaere;
    }
}