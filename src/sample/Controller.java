package sample;


import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    private void initialize() {

        webView.getEngine().load("https://de.wikibooks.org/wiki");


        //Loeschen Button deaktiviert
        Loeschen.setDisable(true);

        Suchen.setOnAction((event) -> {
            search();
        });

        Suchleiste.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                search();
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
                    search();
                    Suchleiste.clear();
                } else {
                    urlName = webView.getEngine().getLocation();
                    urlName = urlName.replace("https://de.wikibooks.org/w/index.php?search=", "");

                    if (urlName.contains("&title")) {
                        urlName = urlName.substring(0, urlName.indexOf("&title"));
                    }
                    Suchleiste.setText(urlName);
                    search();
                    Suchleiste.clear();
                }
            }
        });
    }

    private String urlName;
    private WikiBooks wikiBooks = null;
    private ArrayList<Medium> test = new ArrayList<>();
    private Zettelkasten zettelkasten = new Zettelkasten();
    private Medium selectedItemBuch = null;
    private String richtung = "auf";

    private void hinzufuegen() {
        try {
            if (test.contains(wikiBooks)==false) {
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

    private void loeschen(){
        try {
            zettelkasten.dropMedium("w", selectedItemBuch.getTitel());

        }catch (Exception e){
            errorWikiBooks();
        }
    }

    public void sortieren(){
        zettelkasten.sort(richtung);
        if (!richtung.equals("ab")) {
            richtung = "ab";
        } else {
           richtung = "auf";
        }
    }

    private void search() {
        try {
            String search = Suchleiste.getText().trim().replace(" ", "_");

            if (!urlName.equals(search)) {
                webView.getEngine().load("https://de.wikibooks.org/wiki/" + search);
                wikiBooks.calculateRepraesentation();

            }
        } catch (NullPointerException e) {
            System.out.println("Fehler beim suchen aufgetreten!!!");
        }
    }

    public void speichern(){
        try{

        }catch (Exception e){
            System.out.println("Speichern fehlgeschlagen!!!");
        }
    }

    private void errorWikiBooks() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.getDialogPane().setMinWidth(200);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(null);
        alert.setContentText("Konnte Wikibooks nicht erreichen");
        alert.showAndWait();
    }


}

