package sample;


import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

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
    private void initialize() {

        webView.getEngine().load("https://de.wikibooks.org/wiki");

        Suchen.setOnAction((event) -> {
            search();
        });

        Suchleiste.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                search();
            }
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

    private void search() {
        try {
            String search = Suchleiste.getText().trim().replace(" ", "_");

            if (!urlName.equals(search)) {
                webView.getEngine().load("https://de.wikibooks.org/wiki/" + search);
            }
        } catch (NullPointerException e) {
            System.out.println("Fehler beim suchen aufgetreten!!!");
        }
    }


}

