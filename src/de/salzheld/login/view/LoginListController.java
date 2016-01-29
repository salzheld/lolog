package de.salzheld.login.view;

import de.salzheld.login.MainApp;
import de.salzheld.login.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * Created by jkretzschmar on 22.01.2016.
 */
public class LoginListController {
    ObservableList<String> courseList = FXCollections.observableArrayList(
            "5a", "5b", "5c"
    );

    @FXML
    private TableView<Student> personTable;
    @FXML
    private TableColumn<Student, String> courseColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> loginColumn;
    @FXML
    private TableColumn<Student, String> passwordColumn;
    @FXML
    private ComboBox selectCourseBox;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public LoginListController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        selectCourseBox.setItems(courseList);

        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        personTable.setItems(mainApp.getStudentsData());
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteStudent() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
            copyClipboard();
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Keine Auswahl");
            alert.setHeaderText("Kein Student ausgewält");
            alert.setContentText("Bitte wählen Sie eine Person aus der Tabelle.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewPerson() {
        Student person = new Student();

        if (isInputValid()) {
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setPassword("Realschule");
            person.buildLogin();
            firstNameField.clear();
            lastNameField.clear();
            mainApp.getStudentsData().add(person);
            copyClipboard();
        }
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
        
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "Kein gültiger Vorname!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "Kein gültiger Nachname!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Ungültige Felder");
            alert.setHeaderText("Bitte tragen Sie die Daten korrekt ein!");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    @FXML
    private void handleCourseSelect() {
        personTable.getItems().clear();
        //String test = selectCourseBox.getSelectionModel().getSelectedItem().toString();
        mainApp.getCourse(selectCourseBox.getSelectionModel().getSelectedItem().toString());
        copyClipboard();
    }

    /**
     * copy all logins to the clipboard
     */
    private void copyClipboard() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        String clip = new String();

        ObservableList<Student> list = mainApp.getStudentsData();
        for (Student student : list) {
            clip += student.getLogin() + " ";
            clip += student.getPassword() + " ";
            clip += student.getFirstName() + " ";
            clip += student.getLastName() + "\n";
        }
        content.putString(clip);
        clipboard.setContent(content);
    }

}
