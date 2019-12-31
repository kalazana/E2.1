package sample;
//2.4


import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalEngine;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {
    // Alle benoetigten Buttons für die Aufgabenstellungen hinzugefügt
    @FXML
    private Button Suchen, Speichern, Loeschen, Laden, Hinzufuegen, Sortieren, zurueck, vor, suchenSynonyme;
    //TextFeld für die Suchleiste
    @FXML
    private TextField Suchleiste;
    //WebView
    @FXML
    private WebView webView;
    //Anchor Pane
    @FXML
    private AnchorPane anchorPane;
    //List View als String hinzugefügt da Strings eingegeben werden
    @FXML
    private ListView<String> synonymliste;
    //Combobox als String eingefügt da wir auch hier String eingegeben werden
    @FXML
    private ComboBox<String> synonymBox;
    //Das Fragezeichen Symbol für Aufgaben 2.9 hinzufügen
    @FXML
    private MenuItem fragezeichen;
    //List View als Medium, da wir es als Medium hibzufügen müssen wegen Zettelkasten
    @FXML
    private ListView<Medium> medienListe;
    //Label
    @FXML
    private Label aenderung, bearbeiter;

    //Initialisierung
    @FXML
    private void initialize() {
        //Lädt die Website Wikibooks.org
        webView.getEngine().load("https://de.wikibooks.org/wiki");

        //Loeschen Button permanent deaktiviert
        Loeschen.setDisable(true);

        //sobald der Suchen Button betätigt wird wird die suchen Funktion ausgeführt
        Suchen.setOnAction((event) -> {
            suchen();

        });

        //wenn in die Suchleiste geklickt wird, wird der gesamte Inhalt der Suchleiste markiert
        Suchleiste.setOnMouseClicked(event -> {
            Suchleiste.selectAll();
        });

        //wenn Enter gedrückt wird, wird die Suche ausgeführt mit dem Begriff der in der Suchleiste zum aktuellen Zeitpunkt steht
        Suchleiste.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                suchen();

            }
        });

        //wenn F1 gedrückt wird, wird das Über dieses Programm Fenster geöfffnet
        anchorPane.setOnKeyPressed(event -> {
            F1Zeug(event);
        });

        //Hinzufuegen Button mit entsprechender Methode
        Hinzufuegen.setOnAction(event -> {
            hinzufuegen();
        });

        //holt sich den State des WorkLoader von der Engine von Webview
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            //Worker State alle Worker fangen in Ready an, perfomt Aufgaben im Hintergrund, ermöglicht einen "Blick" in die "Zukunft"
            if (Worker.State.FAILED.equals(newValue)) {
                errorWikiBooks();
                synonymBoxClearen();
            }
            //Das hier ist für die Umleitung zuständig, sobald ein Link gefunden wird der außerhalb von Wikibooks führt und gedrückt wurde, wird man sofort zur Startseite wieder umgeleitet
            if (Worker.State.SCHEDULED.equals(newValue)) {
                if (!webView.getEngine().getLocation().contains("https://de.wikibooks.org/")) {
                    webView.getEngine().load("https://de.wikibooks.org/wiki/");
                }
            }
            //Wenn die Verbindung hergestellt wurde guckt er setzt er den aktuellen Namen in die Suchleiste
            if (Worker.State.SUCCEEDED.equals(newValue)) {
                if (webView.getEngine().getLocation().contains("https://de.wikibooks.org/wiki/")) {
                    urlName = webView.getEngine().getLocation();
                    urlName = urlName.replace("https://de.wikibooks.org/wiki/", "");
                    Suchleiste.setText(urlName);

                  //setzt die Suche und den Titel in die Sucheliste falls er als Buchtitel erkannt wird
                } else {
                    urlName = webView.getEngine().getLocation();
                    urlName = urlName.replace("https://de.wikibooks.org/w/index.php?search=", "");

                    if (urlName.contains("&title")) {
                        urlName = urlName.substring(0, urlName.indexOf("&title"));
                    }
                    Suchleiste.setText(urlName);
                    suchen();
                    Suchleiste.clear();
                }
            }
        });

        //sucht die Synonyme wenn der Button gedrückt wird
        suchenSynonyme.setOnAction(e -> {
            KnoepfeClearen();
            synonymeSuchen();
            Suchleiste.setText(String.valueOf(synonymBegriff));
        });


        // beim selektieren eines Items in der Liste für die Synonyme (das mit dem Doppelklick
        synonymliste.setOnMouseClicked(e -> {
            synonymBegriff = synonymliste.getSelectionModel().getSelectedItems().get(0);
            if (e.getClickCount() == 2) {
                Suchleiste.setText(synonymBegriff);
                synonymBoxClearen();
            }
        });

        // wenn die SynonymBox selektiert wird
        synonymBox.setOnAction(e -> {
            synonymBegriff = synonymBox.getSelectionModel().getSelectedItem();
            Suchleiste.setText(String.valueOf(synonymBegriff));
            synonymeSuchen();
            synonymBox.getSelectionModel().select(synonymBegriff);
            KnoepfeClearen();
            webView.getEngine().load("https://de.wikibooks.org/wiki/" + synonymBegriff);
        });

        //setzt den Index wenn man auf den Vor Button um einen niedriger, ansonsten macht er genau das was er auch im Browser macht
        vor.setOnAction(event -> {
            synonymBox.getSelectionModel().select(synonymBox.getSelectionModel().getSelectedIndex() - 1);
        });
        //setzt den Index um einen höher
        zurueck.setOnAction(event -> {
            synonymBox.getSelectionModel().select(synonymBox.getSelectionModel().getSelectedIndex() + 1);
        });

        //wenn man auf den Fragezeichen Button drückt zeigt er das Rechtszeug an
        fragezeichen.setOnAction(event -> {
            rechtszeugzeigen();
        });
        //wenn man hinzufuegen Button drückt ruft er die Methode auf
        Hinzufuegen.setOnAction(e -> {
            hinzufuegen();
        });
    }
    //
    private Synonyme synonyme = new Synonyme();
    //sucht die Synonyme
    private void synonymeSuchen() {
        try {
            ObservableList<String> liste = synonyme.synonymList(Suchleiste.getText());
            liste.sort(String::compareTo);
            synonymliste.getItems().clear();
            if (liste.size() < 1) {
                synonymliste.getItems().add("<keine>");
                synonymliste.setDisable(true);
                suchenSynonyme.setDisable(true);

            } else {
                synonymliste.setDisable(false);
                suchenSynonyme.setDisable(false);
                synonymliste.getItems().addAll(liste);
            }
        } catch (Exception e) {
            errorSynonyme();
        }
    }
    //resettet die Knoepfe
    private void KnoepfeClearen() {

        try {
            int wortIndex = synonymBox.getSelectionModel().getSelectedIndex();

            if (wortIndex < 1) {
                vor.setDisable(true);
            } else {
                vor.setDisable(false);
            }
            if (wortIndex >= synonymBox.getItems().size() - 1) {
                zurueck.setDisable(true);
            } else {
                zurueck.setDisable(false);
            }
        } catch (Exception e) {
            KnopError();

        }
    }
    //resettet die Synonymbox
    private void synonymBoxClearen() {
        try {
            int wortIndex = synonymBox.getSelectionModel().getSelectedIndex();
            if (synonymBox.getItems().contains(Suchleiste.getText())) {
                synonymBox.getItems().remove(Suchleiste.getText());
            }
            if (wortIndex > 0) {
                synonymBox.getItems().remove(0, wortIndex);

            }
            synonymBox.getItems().add(0, Suchleiste.getText());
            synonymBox.getSelectionModel().select(0);
        } catch (Exception e) {
            KnopError();
        }
    }

    private String synonymBegriff = null;
    private String urlName;
    private WikiBooks wikiBooks = null;
    private ArrayList<Medium> test = new ArrayList<>();
    private Zettelkasten zettelkasten = new Zettelkasten();
    private Medium selectedItemBuch = null;
    private String richtung = "auf";

    private void hinzufuegen() {
        try {

            zettelkasten.addMedium(wikiBooks);
            for (Medium medium : test) {
                if (!test.contains(medium)) {
                    zettelkasten.addMedium(medium);
                } else {
                    System.out.println("Fehler: 'hinzufuegen'");
                }
            }

        } catch (Exception e) {
            errorWikiBooks();
        }
    }
    //eh deaktiviert
    private void loeschen() {
        try {
            // zettelkasten.dropMedium("w", selectedItemBuch.getTitel());
            medienliste();

        } catch (Exception e) {
            errorWikiBooks();
        }
    }
    //Funtkion für auf und abwaärts sortieren der Mediein im zettelkasten
    public void sortieren() {
        try {
            zettelkasten.sort(richtung);
            medienliste();
            if (!richtung.equals("ab")) {
                richtung = "ab";
            } else {
                richtung = "auf";
            }
        } catch (Exception e) {
            sortierError();
        }
    }
    //funktion suchen, das Suchergebnis wird erstaml getrimmt und leerzeuichen werden aufgefüllt
    private void suchen() {
        try {
            String suchergebnis = Suchleiste.getText().trim().replace(" ", "_");

            //Falls dass aktuell gesuchte nicht etwass neuen entspricht lade es nucht neu
            if (!urlName.equals(suchergebnis)) {
                webView.getEngine().load("https://de.wikibooks.org/wiki/" + suchergebnis);
                synonymeSuchen();
                synonymBoxClearen();
                wikiBooks.calculateRepraesentation();
            }
            //setzt Informationen wie Verfasser (theoretisch zumindest)
            WikiBooksParser books = new WikiBooksParser();
            wikiBooks = (WikiBooks) books.parse(suchergebnis);
            setBookInformation(wikiBooks);
        } catch (NullPointerException e) {
            System.out.println("Fehler beim suchen aufgetreten!!!");
        }
    }
    // Speichert die Medien im Zettelkasten
    public void speichern() {
        try {

        } catch (Exception e) {
            System.out.println("Speichern fehlgeschlagen!!!");
        }
    }
    //Fehlermeldung wenn man Wikibooks nicht erreichen kann
    private void errorWikiBooks() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(200);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Konnte Wikibooks nicht erreichen.");
        alert.showAndWait();
    }
    //Fehlermeldung bei den Synonymen für den Zugriff auf den Wortschatz zugreifen
    public void errorSynonyme() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(200);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Fehler beim Zugriff auf den Wortschatzserver.");
        alert.showAndWait();

    }
    //Fehlermeldung fürs sortieren
    public void sortierError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(200);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Fehler beim sortieren.");
        alert.showAndWait();

    }

    //Fehler beim drücken eines Buttons
    public void KnopError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(200);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Fehler bei Aktion mit Knopf.");
        alert.showAndWait();

    }
        //zeitg den ewig langen Text wenn man F1 drückt oder auf das Fragezeichen drückt
    public void rechtszeugzeigen() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(600);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Alle redaktionellen Inhalte stammen von den Internetseiten der Projekte Wikibooks und Wortschatz.\n\nDie von Wikibooks bezogenen Inhalte unterliegen der GNU Free Documentation License und damit auch" +

                "dieses Programm. Der Text der GNU FDL ist unter" +

                "http://de.wikipedia.org/wiki/Wikipedia:GNU_Free_Documentation_License verfügbar.\n\nDie von Wortschatz (http://wortschatz.uni-leipzig.de/) bezogenen Inhalte sind urheberrechtlich geschützt." +

                "Sie werden hier für wissenschaftliche Zwecke eingesetzt und dürfen darüber hinaus in keiner Weise" +

                "genutzt werden.\nDieses Programm ist nur zur Nutzung durch den Programmierer selbst gedacht.\n\n Dieses Programm dient" +

                "der Demonstration und dem Erlernen von Prinzipien der Programmierung mit Java. Eine Verwendung" +

                "des Programms für andere Zwecke verletzt die Urheberrechte der Originalautoren der redaktionellen" +

                "Inhalte und ist daher untersagt.");

        alert.showAndWait();

    }
    //Methode fürs drücken von F1
    private void F1Zeug(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.F1) {
            rechtszeugzeigen();
        }
    }
    //Das istz die Medienliste wo die Bücher hinzugefügt werden
    private void medienliste() {
        medienListe.getItems().clear();
        for (Medium medium : zettelkasten.getMedium_Arr()) {
            medienListe.getItems().add(medium);
        }
    }

    //Setzt den Verfasser und das letzte Bearbeitungsdatum
    private void setBookInformation(Medium medium) {
        if (medium != null) {
            bearbeiter.setText("Letzte Bearbeitung: " + wikiBooks.getVerfasser());
            aenderung.setText("Letzte Aenderung: " + WikiBooks.convertTime(wikiBooks.getAenderungsDatum()));
        } else {
            bearbeiter.setText("Letzter Bearbeiter:");
            aenderung.setText("Letzte Änderung:");
        }
    }

}

