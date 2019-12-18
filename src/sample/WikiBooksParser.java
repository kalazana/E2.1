package sample;

/* @author Fuad Aliev
 *  02.12.2018
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import javafx.scene.control.Button;

public class WikiBooksParser {

    /*
     * readFromURL</p> Methode fuer die Verbindung zu einer URL + einlesen des
     * Inputstreams, dabei wird der InputStream in eine ArrayList gespeichert.
     * _sSearch return throws IOException
     */
    public ArrayList<String> readFromURL(String _sSearch) {
        ArrayList<String> tmp = new ArrayList<String>();

        try {
            URL url = new URL("https://de.wikibooks.org/wiki/Spezial:Exportieren/" + _sSearch);
            URLConnection connection = url.openConnection();

            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent",
                    "Mozilla/4.0" + "(compatible; MSIE 6.0; Windows NT 5.0; .NET CLR 1.1.4322)");
            InputStream inStream = connection.getInputStream();

            BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
            String line = "";
            while ((line = input.readLine()) != null) {
                tmp.add(line);
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tmp;

    }

    /*
     * parse Methode zum Parsen eine XML Datei -> auf basis von StaxParser param
     * _sSearch throws IOException
     */
    public WikiBooks parse(String search) {

        boolean bIP = false, isbook = false, bUsername = false, bTimestamp = false, bText = false;
        String sUsername = null, sTimestamp = null, sTitel = null, sRegal = null;

        ArrayList<String> arr_Kapitel = new ArrayList<String>();
        StringBuilder sText = new StringBuilder();
        TmpFile tmp = new TmpFile();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory
                    .createXMLEventReader(new FileReader(tmp.createTmpFile(readFromURL(search))));
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                switch (event.getEventType()) {

                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();

                        if (qName.equalsIgnoreCase("mediawiki")) {
                            sTitel = search.replace('_', ' ');
                        } else if (qName.equalsIgnoreCase("timestamp")) {
                            bTimestamp = true;
                        } else if (qName.equalsIgnoreCase("ip")) {
                            bIP = true;
                        } else if (qName.equalsIgnoreCase("username")) {
                            bUsername = true;
                        } else if (qName.equalsIgnoreCase("text")) {
                            bText = true;
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if (bTimestamp) {
                            sTimestamp = characters.getData();
                            bTimestamp = false;
                        }
                        if (bIP) {
                            sUsername = characters.getData();
                            bIP = false;
                        }
                        if (bUsername) {

                            sUsername = characters.getData();
                            bUsername = false;
                        }
                        if (bText) {
                            sText.append(characters.getData());
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();

                        if (endElement.getName().getLocalPart().equalsIgnoreCase("text")) {
                            bText = false;
                            if (sText.toString().contains("[[Kategorie:Buch]]")) {
                                sRegal = stringParserRegal(sText);
                                arr_Kapitel = stringParserKapitel(sText);
                                isbook = true;
                            }
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isbook) {
            //return new WikiBooks(sTitel, sTimestamp, sRegal, arr_Kapitel, sUsername);
        } else {
            return null;
        }
        return null;
    }

    /*
     * stringParserRegal Methode zum auslesen des Regals eines Text Tags
     *
     * @param _sText
     *
     * @return
     */
    public String stringParserRegal(StringBuilder _sText) {
        int iPos = 0;
        String sRegal = null;
        while (true) {
            if (_sText.indexOf("Regal|ort=", iPos) != -1) {
                sRegal = _sText.substring(_sText.indexOf("Regal|ort=", iPos) + 10, _sText.indexOf("}", iPos));
                iPos = _sText.indexOf("Regal|ort=", iPos) + 10;
            } else if (_sText.indexOf("Regal|", iPos) != -1) {
                sRegal = "Regal: " + _sText.substring(_sText.indexOf("Regal", iPos) + 6, _sText.indexOf("}", iPos));
                iPos = _sText.indexOf("Regal|", iPos) + 6;
            } else {
                break;
            }
        }
        return sRegal;
    }

    /*
     * stringParserKapitel</p> Methode zum auslesen der Kapitel eines Text Tags
     *
     * @param _sText
     *
     * @return
     */
    public ArrayList<String> stringParserKapitel(StringBuilder _sText) {
        int iPos = 0, iCount = 0, iCount2 = 1;
        ArrayList<String> arr_Kapitel = new ArrayList<String>();
        while (true) {

            if ((_sText.indexOf("\n== ", iPos) != -1)) {
                if (_sText.indexOf(" ==", iPos) != -1) {
                    iCount++;
                    arr_Kapitel.add(iCount + ".\t"
                            + _sText.substring(_sText.indexOf("== ", iPos) + 3, _sText.indexOf(" ==", iPos)));

                    iPos = _sText.indexOf(" ==", iPos) + 3;
                }
            } else if ((_sText.indexOf("\n=== ", iPos) != -1)) {
                if (_sText.indexOf(" ===", iPos) != -1) {
                    arr_Kapitel.add(iCount + "." + iCount2 + ".\t\t"
                            + _sText.substring(_sText.indexOf("=== ", iPos) + 4, _sText.indexOf(" ===", iPos)));
                    iCount2++;
                    iPos = _sText.indexOf(" ==", iPos) + 4;
                }
            } else {
                break;
            }
        }
        return arr_Kapitel;

    }

}
