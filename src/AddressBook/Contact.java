package AddressBook;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.Date;

public class Contact {
    public SimpleStringProperty firstname;
    public SimpleStringProperty lastname;
    public SimpleStringProperty address;
    public SimpleStringProperty city;
    public SimpleStringProperty state;
    public SimpleStringProperty zipcode;
    public String contactID;
    public LocalDate birthday;

    public Contact(String firstname, String lastname, String address, String city, String state, String zipcode, String contactID, LocalDate birthday) {
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.zipcode = new SimpleStringProperty(zipcode);
        this.birthday = birthday;
        this.contactID = contactID;
    }

    public void update(String firstname, String lastname, String address, String city, String state, String zipcode, LocalDate birthday) {
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.zipcode = new SimpleStringProperty(zipcode);
        this.birthday = birthday;
    }

    public boolean equals(Contact c) {
        return contactID == c.contactID;
    }

    public String getFirstname() {
        return firstname.get();
    }

    public SimpleStringProperty firstnameProperty() {
        return firstname;
    }

    public String getLastname() {
        return lastname.get();
    }

    public SimpleStringProperty lastnameProperty() {
        return lastname;
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public String getCity() {
        return city.get();
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public String getZipcode() {
        return zipcode.get();
    }

    public SimpleStringProperty zipcodeProperty() {
        return zipcode;
    }

    public LocalDate getBirthDate() { return birthday; }

    public SimpleStringProperty birthdayProperty() {
        return new SimpleStringProperty(birthday.toString());
    }

    @Override
    public String toString() {
        return "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ",  id='" + contactID + '\'' +
                ", birthday='" + birthday + '\'' +
                ", zipcode='" + zipcode + '\'';
    }
}
