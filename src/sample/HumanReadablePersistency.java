package sample;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HumanReadablePersistency implements Persistency {

    public void save(Zettelkasten ZK,String Dateiname){
        BufferedWriter out;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Dateiname), StandardCharsets.UTF_8));

            for (Medium output : ZK) {
                out.write(output.calculateRepresentation());
            }
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            System.out.print("Error: File: \""+Dateiname+"\" Not Found:");
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Zettelkasten load(String Dateiname) throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }
}
