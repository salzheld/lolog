package de.salzheld.login.model;

import de.salzheld.login.util.Tools;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Joern on 18.01.2016.
 */
public class Person {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty name;
    private final StringProperty course;
    private final IntegerProperty grade;

    private final StringProperty login;
    private final StringProperty password;

    /**
     * Default constructor.
     */
    public Person() {
        this(null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param firstName
     * @param lastName
     * @param course
     * @param password
     */
    public Person(String firstName, String lastName, String course, String password) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.name = new SimpleStringProperty();
        this.name.bind(Bindings.concat(this.firstName, "  ", this.lastName));
        this.course = new SimpleStringProperty(course);
        this.password = new SimpleStringProperty(password);
        grade = new SimpleIntegerProperty(5);
        login = new SimpleStringProperty(null);
        buildLogin();
    }

    /**
     * Build the LoNet²-Login
     **/
    public void buildLogin() {
        if(firstName.getValue() != null) {
            login.set( Tools.sanitizeName(lastName.getValue()) + "-" + Tools.sanitizeName(firstName.getValue()) );
        }
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getGrade() {
        return grade.get();
    }

    public void setGrade(int grade) {
        this.grade.set(grade);
    }

    public IntegerProperty gradeProperty() {
        return grade;
    }

    public String getCourse() {
        return this.course.get();
    }

    public void setCourse(String course) {
        this.course.set(course);
    }

    public StringProperty courseProperty() {
        return course;
    }

    public String getLogin() {
        return this.login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public StringProperty loginProperty() {
        return login;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }
}
