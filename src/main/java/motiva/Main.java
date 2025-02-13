package motiva;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import motiva.ui.MainWindow;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Motiva motiva = new Motiva();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(250);
            stage.setMinWidth(650);
            stage.setTitle("Motiva ChatBot");
            fxmlLoader.<MainWindow>getController().setMotiva(motiva);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


