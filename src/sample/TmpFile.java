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
    public File createTmpFile(ArrayList<String> _tmp) throws IOException {
        File f = File.createTempFile("temp", ".xml");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF8"));
        f.deleteOnExit();

        for (String line : _tmp) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();

        BufferedReader br = new BufferedReader(new FileReader(f));
        br.close();
        return f;
    }
}


