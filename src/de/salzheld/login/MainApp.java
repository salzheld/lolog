package de.salzheld.login;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import com.sun.corba.se.impl.util.Version;
import de.salzheld.login.model.LoginModel;
import de.salzheld.login.model.Person;
import de.salzheld.login.model.PersonListWrapper;
import de.salzheld.login.view.LoginListController;
import de.salzheld.login.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Connection connection;

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Person> studentsData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("LoLog - LoNet-Login-Helfer (c) 2016 - Kretzschmar");

        initRootLayout();
        connectSql();
        showLoginList();
    }

    private void connectSql() {
        LoginModel loginModel = new LoginModel();
        connection = loginModel.getConnection();
    }

    private void initStudents(String statement) {

        try {
            PreparedStatement pst = connection.prepareStatement( statement );
            ResultSet rs = pst.executeQuery();

            // add Students from Database
            while( rs.next() ) {
                studentsData.add(
                        new Person(
                                rs.getString(3),
                                rs.getString(2),
                                rs.getString(1),
                                "Realschule")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Returns the data as an observable list of Students.
     * @return
     */
    public ObservableList<Person> getStudentsData() {
        return studentsData;
    }

    /**
     * Returns the data as an observable list of Students.
     * @return
     */
    public ObservableList<Person> getCourse(String course) {
        studentsData.removeAll();
        String statement2 =
                "SELECT" +
                        " g.Bezeichnung,p.Nachname,p.Rufname,p.Geschlecht from person p" +
                        " inner join jahrgangsdaten j on p.Id=j.SchuelerId" +
                        " inner join gruppe g on j.GruppeId=g.Id where" +
                        " j.Status='0' and g.Bezeichnung='" + course +"'" +
                        " order by g.Bezeichnung, p.Geschlecht, p.Nachname;";
        initStudents(statement2);
        return studentsData;
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened person file.
        File file = getStudentsFilePath();
        if (file != null) {
            loadStudentDataFromFile(file);
        }
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getStudentsFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setStudentsFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("LoLog - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("LoLog");
        }
    }

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public void loadStudentDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            studentsData.clear();
            studentsData.addAll(wrapper.getPersons());

            // Save the file path to the registry.
            setStudentsFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Kann Daten nicht lesen!");
            alert.setContentText("Kann Daten in Datei nicht lesen:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void saveStudentDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(studentsData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setStudentsFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Kann Daten nicht speichern!");
            alert.setContentText("Kann Daten nicht in Datei speichern:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showLoginList() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LoginList.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            LoginListController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}