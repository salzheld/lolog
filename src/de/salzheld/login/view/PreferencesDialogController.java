package de.salzheld.login.view;

import de.salzheld.login.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by jkretzschmar on 22.01.2016.
 */
public class PreferencesDialogController {
    @FXML
    private TextField sqlHostField;
    @FXML
    private TextField sqlDatabaseField;
    @FXML
    private TextField sqlUserField;
    @FXML
    private TextField sqlUserPasswordField;
    @FXML
    private TextField lonetPasswordField;

    private Stage dialogStage;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

//        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
//            errorMessage += "Kein gültiger Vorname!\n";
//        }
//        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
//            errorMessage += "Kein gültiger Nachname!\n";
//        }
//        if (passwordField.getText() != null && passwordField.getText().length() < 8) {
//            errorMessage += "Kein gültiges Passwort!\n";
//        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Ungültige Felder");
            alert.setHeaderText("Bitte tragen Sie die Daten korrekt ein!");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

}
