package sample;

/* @author Fuad Aliev
 *  10.12.2018
 */

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
        Suchleiste.setText("Java Standard");

        Suchen.setOnAction((event) -> {
            WikiBooks books = new WikiBooks(Suchleiste.getText());
            webView.getEngine().load(books.getURL());
        });

        Suchleiste.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                search();
            }
        });


    }

    private String urlName;
    private WikiBooks book = null;

    private void search() {
        try {
            String search = Suchleiste.getText().trim().replace(" ", "_");

            if (!urlName.equals(search)) {
                

                webView.getEngine().load("https://de.wikibooks.org/wiki/" + search);

            }
            WikiBooksParser books = new WikiBooksParser();
            book = books.parse(search);
        } catch (NullPointerException e) {
            System.out.println("Fehler bei der Suche!");
        }
    }


}

