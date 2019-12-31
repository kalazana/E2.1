package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class TmpFile {

    /*
     * createTmpFile
     * Methode zum erstellen einer TmpFile
     * @param _tmp
     * @return
     * @throws IOException
     */

    //erstellt ein temporäres File aus dem Daten ausgelesen werden um den Stream dann wieder zu schließen, geht dabei jede Line einzlen durch und gibt eine Datei zurück
    public File createTmpFile(ArrayList<String> _tmp) throws IOException {
        File datei = File.createTempFile("temp", ".xml");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(datei), "UTF8"));
        datei.deleteOnExit();

        for (String line : _tmp) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();

        BufferedReader br = new BufferedReader(new FileReader(datei));
        br.close();
        return datei;
    }
}


