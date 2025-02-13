package motiva.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import motiva.Motiva;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Motiva motiva;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.jpg"));
    private Image motivaImage = new Image(this.getClass().getResourceAsStream("/images/DaMotiva.jpg"));

    /**
     * Creates the GUI window for the application.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().add(
                DialogBox.getMotivaDialog("Hello! I'm Motiva.\nWhat can I do for you?", motivaImage)
        );
    }

    /**
     * Injects the Motiva instance
     */
    public void setMotiva(Motiva m) {
        this.motiva = m;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Motiva's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        String response = motiva.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMotivaDialog(response, motivaImage)
        );
        userInput.clear();
        if (input.equals("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(3)); // 3-second delay
            delay.setOnFinished(event -> Platform.exit()); // Exit after delay
            delay.play();
        }
    }
}
