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
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {
    @FXML
    private Button Suchen;

    @FXML
    private TextField Suchleiste;

    @FXML
    private WebView webView;

    @FXML
    AnchorPane anchorPane;

    @FXML
    Button Speichern;

    @FXML
    Button Laden;

    @FXML
    Button Loeschen;

    @FXML
    Button Hinzufuegen;

    @FXML
    Button Sortieren;

    @FXML
    ListView<String> synonymliste;

    @FXML
    Button vor;

    @FXML
    Button zurueck;

    @FXML
    ComboBox<String> synonymBox;

    @FXML
    Button suchenSynonyme;

    @FXML
    MenuItem fragezeichen;

    @FXML
    ListView<Medium> medienListe;


    @FXML
    private void initialize() {

        webView.getEngine().load("https://de.wikibooks.org/wiki");


        //Loeschen Button deaktiviert
        Loeschen.setDisable(true);

        Suchen.setOnAction((event) -> {
            suchen();


        });

        Suchleiste.setOnMouseClicked(event -> {
            Suchleiste.selectAll();
        });

        Suchleiste.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                suchen();

            }
        });


        anchorPane.setOnKeyPressed(event -> {
            F1Zeug(event);
        });

        Hinzufuegen.setOnAction(event -> {
            hinzufuegen();
        });

        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            //Worker State alle Worker fangen in Ready an, perfomt Aufgaben im Hintergrund, ermöglicht einen "Blick" in die "Zukunft"
           if (Worker.State.FAILED.equals(newValue)) {
                errorWikiBooks();
                synonymBoxClearen();
            }
            if (Worker.State.SCHEDULED.equals(newValue)) {
                if (!webView.getEngine().getLocation().contains("https://de.wikibooks.org/")) {
                    webView.getEngine().load("https://de.wikibooks.org/wiki/");
                }
            }

            if (Worker.State.SUCCEEDED.equals(newValue)) {
                if (webView.getEngine().getLocation().contains("https://de.wikibooks.org/wiki/")) {
                    urlName = webView.getEngine().getLocation();
                    urlName = urlName.replace("https://de.wikibooks.org/wiki/", "");
                    Suchleiste.setText(urlName);
                    suchen();

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

        suchenSynonyme.setOnAction(e -> {
            KnoepfeClearen();
            synonymeSuchen();
            Suchleiste.setText(synonymBegriff);
        });


        // beim selektieren eines Items in der Liste f�r Synonyme
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
            Suchleiste.setText(synonymBegriff);
            synonymeSuchen();
            synonymBox.getSelectionModel().select(synonymBegriff);
            KnoepfeClearen();
            webView.getEngine().load("https://de.wikibooks.org/wiki/" + synonymBegriff);
        });

        vor.setOnAction(event -> {
            synonymBox.getSelectionModel().select(synonymBox.getSelectionModel().getSelectedIndex() - 1);
        });

        zurueck.setOnAction(event -> {
            synonymBox.getSelectionModel().select(synonymBox.getSelectionModel().getSelectedIndex() + 1);
        });

        fragezeichen.setOnAction(event -> {
            rechtszeugzeigen();
        });
    }



    private Synonyme synonyme = new Synonyme();

    private void synonymeSuchen() {
        try {
            ObservableList<String> liste = synonyme.synonymList(Suchleiste.getText());
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
            if (test.contains(wikiBooks) == false) {
                zettelkasten.addMedium(wikiBooks);
                test.add(wikiBooks);
                System.out.println(test);

            } else {
                System.out.println("a");
            }

        } catch (Exception e) {
            errorWikiBooks();
        }
    }

    private void loeschen() {
        try {
            zettelkasten.dropMedium("w", selectedItemBuch.getTitel());

        } catch (Exception e) {
            errorWikiBooks();
        }
    }

    public void sortieren() {
        zettelkasten.sort(richtung);
        if (!richtung.equals("ab")) {
            richtung = "ab";
        } else {
            richtung = "auf";
        }
    }

    private void suchen() {
        try {
            String suchergebnis = Suchleiste.getText().trim().replace(" ", "_");

            if (!urlName.equals(suchergebnis)) {
                webView.getEngine().load("https://de.wikibooks.org/wiki/" + suchergebnis);
                synonymeSuchen();
                synonymBoxClearen();
                wikiBooks.calculateRepraesentation();
            }
        } catch (NullPointerException e) {
            System.out.println("Fehler beim suchen aufgetreten!!!");
        }
    }

    public void speichern() {
        try {

        } catch (Exception e) {
            System.out.println("Speichern fehlgeschlagen!!!");
        }
    }

    private void errorWikiBooks() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(200);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Konnte Wikibooks nicht erreichen.");
        alert.showAndWait();
    }

    public void errorSynonyme() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(200);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Fehler beim Zugriff auf den Wortschatzserver.");
        alert.showAndWait();

    }

    public void KnopError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(200);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Fehler bei Aktion mit Knopf.");
        alert.showAndWait();

    }

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

    private void F1Zeug(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.F1) {
            rechtszeugzeigen();
        }
    }

}

