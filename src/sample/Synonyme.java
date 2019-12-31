package sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Synonyme {
    private String BasisUrl = "http://api.corpora.uni-leipzig.de/ws/similarity/";
    private String Corpus = "deu_news_2012_1M";
    private String Anfragetyp = "/coocsim/";
    private String Suchbegriff = "";
    private String Parameter = "?minSim=0.1&limit=50";

    public ObservableList<String> synonymList(String _wort) throws IOException, ParseException {
        Suchbegriff = _wort;
        ObservableList<String> ausgabe = FXCollections.observableArrayList();

        JSONArray jsonResponseArr = null;
        JSONObject wordContainer = null;
        String synonym = null;

        URL myURL = new URL(BasisUrl + Corpus + Anfragetyp + Suchbegriff + Parameter);
        JSONParser jsonParser = new JSONParser();
        String jsonResponse = streamToString(myURL.openStream());

        // den gelieferten String in ein Array parsen
        jsonResponseArr = (JSONArray) jsonParser.parse(jsonResponse);

        for (Object e1 : jsonResponseArr) {

            // aus dem Element den Container fuer das Synonym beschaffen
            wordContainer = (JSONObject) ((JSONObject) e1).get("word");

            // aus dem Container das Synonym beschaffen
            synonym = (String) wordContainer.get("word");

            ausgabe.add(synonym);
        }

        return ausgabe;
    }

    /**
     * Reads InputStream's contents into String
     *
     * @param is - the InputStream
     * @return String representing is' contents
     * @throws IOException
     */
    public static String streamToString(InputStream is) throws IOException {
        String result = "";

        // other options: https://stackoverflow.com/questions/309424/read-convert - an -
        // inputstream - to - a - string
        try (Scanner s = new Scanner(is)) {
            s.useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
            is.close();
        }
        return result;
    }
}