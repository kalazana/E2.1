package sample;
//2.4


import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;

import java.util.ArrayList;

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

        Hinzufuegen.setOnAction(event -> {
            hinzufuegen();
        });

        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {

            if (Worker.State.SUCCEEDED.equals(newValue)) {
                if (webView.getEngine().getLocation().contains("https://de.wikibooks.org/wiki/")) {
                    urlName = webView.getEngine().getLocation();
                    urlName = urlName.replace("https://de.wikibooks.org/wiki/", "");
                    Suchleiste.setText(urlName);
                    suchen();
                    //Suchleiste.clear();
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

        vor.setOnAction(event ->    {
            synonymBox.getSelectionModel().select(synonymBox.getSelectionModel().getSelectedIndex()-1);
        });

        zurueck.setOnAction(event -> {
            synonymBox.getSelectionModel().select(synonymBox.getSelectionModel().getSelectedIndex()+1);
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


}

