package de.salzheld.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.corba.se.impl.util.Version;
import de.salzheld.login.helper.ConnectMySQL;
import de.salzheld.login.model.Student;
import de.salzheld.login.view.LoginListController;
import de.salzheld.login.view.PreferencesDialogController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Student> studentsData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("LoLog - LoNet-Login-Helfer (c) 2016 - Kretzschmar");

        initRootLayout();
        showLoginList();
    }

    private void initStudents(String statement) {

        Connection connection = ConnectMySQL.ConnectDatabase("localhost", "3306", "Danis61128", "root", "tischTuch");
        if (connection == null) {
            System.out.println("no connection");
        }

        try {
            PreparedStatement pst = connection.prepareStatement( statement );
            ResultSet rs = pst.executeQuery();

            // add Students from Database
            while( rs.next() ) {
                studentsData.add(
                        new Student(
                                rs.getString(3),
                                rs.getString(2),
                                rs.getString(1),
                                "Realschule"
                        )
                );
            }
            connection.close();

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
    public ObservableList<Student> getStudentsData() {
        return studentsData;
    }

    /**
     * Returns the data as an observable list of Students.
     * @return
     */
    public ObservableList<Student> getCourse(String course) {
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
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Student person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/PreferencesDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PreferencesDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            //controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}