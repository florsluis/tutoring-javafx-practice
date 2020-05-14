package AddressBook;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    AddressBook addressBook = new AddressBook();

    // Fields
    @FXML
    public DatePicker datepickerBirthdate;
    @FXML
    private TextField fieldFirst;
    @FXML
    private TextField fieldLast;
    @FXML
    private TextField fieldAddress;
    @FXML
    private TextField fieldCity;
    @FXML
    private TextField fieldState;
    @FXML
    private TextField fieldZip;

    // TableView
    @FXML
    private TableView<Contact> tableView;
    @FXML
    private TableColumn<Contact, String> firstColumn;
    @FXML
    private TableColumn<Contact, String> lastColumn;
    @FXML
    private TableColumn<Contact, String> addressColumn;
    @FXML
    private TableColumn<Contact, String> cityColumn;
    @FXML
    private TableColumn<Contact, String> stateColumn;
    @FXML
    private TableColumn<Contact, String> zipColumn;
    @FXML
    public TableColumn<Contact, LocalDate> birthdayColumn;

    String alphaRegExp = "^[A-Za-z]+$";
    String stateRegExp = "^[A-Za-z]{2}$";
    String zipRegExp = "(^\\d{5}$)|(^\\d{5}-\\d{4}$)";

    private String filterString = "";

    public void clearClicked() {
        fieldFirst.setText("");
        fieldLast.setText("");
        fieldAddress.setText("");
        fieldCity.setText("");
        fieldState.setText("");
        fieldZip.setText("");
        datepickerBirthdate.setValue(LocalDate.now());
        filterString = "";
        updateTableView();
    }

    public void findClicked() {
        filterString = fieldLast.getText().toLowerCase();
        updateTableView();
    }

    /**
     * Adds or Updates Contact
     */
    public void updateClicked() {
        String first = fieldFirst.getText();
        String last = fieldLast.getText();
        String address = fieldAddress.getText();
        String city = fieldCity.getText();
        String state = fieldState.getText();
        String zipcode = fieldZip.getText();
        LocalDate birthday = datepickerBirthdate.getValue();

        boolean isFirstAlpha = first.matches(alphaRegExp);
        boolean isLastAlpha = last.matches(alphaRegExp);
        boolean isZipCodeValid = zipcode.matches(zipRegExp);
        boolean isStateValid = state.matches(stateRegExp);

        // TODO: Add error message label for errors

        if (!isFirstAlpha) {
            System.out.println("First not alpha:");
        }

        if (!isLastAlpha) {
            System.out.println("Second not alpha:");
        }

        if (!isZipCodeValid) {
            System.out.println("Zipcode not valid:");
        }

        if (!isStateValid) {
            System.out.println("State not valid:");
        }

        if (isFirstAlpha && isLastAlpha && isZipCodeValid && isStateValid) {
            try {
                Contact selected = tableView.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    addressBook.createContact(first, last, address, city, state, zipcode, birthday);
                } else {
                    selected.update(first, last, address, city, state, zipcode, birthday);
                    addressBook.updateContact(selected);
                }
                updateTableView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void deleteClicked() {
        Contact selected = tableView.getSelectionModel().getSelectedItem();
        try {
            addressBook.removeContact(selected);
            updateTableView();
        } catch (Exception e) {
            System.out.println("Error removing contact: \n" + e.toString());
        }
    }

    public void updateTableView() {
        ObservableList<Contact> dataList = addressBook.getContacts();
        FilteredList<Contact> filteredData = new FilteredList<>(dataList, b -> true);

        filteredData.setPredicate(contact -> {
            if (filterString == null || filterString.isEmpty()) {
                return true;
            } else return contact.getLastname().toLowerCase().contains(filterString);
        });

        SortedList<Contact> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstname"));
        lastColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("address"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("city"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("state"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("zipcode"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Contact, LocalDate>("birthday"));

        tableView.setRowFactory(tv -> {
            TableRow<Contact> row;

            // Conditional Formatting
            row = new TableRow<Contact>() {
                @Override
                public void updateItem(Contact item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle("");
                    } else if (LocalDate.now().isEqual(item.birthday)) {
                        setStyle("-fx-background-color: red;");
                    } else {
                        setStyle("");
                    }
                }
            };

            // Double click to populate fields for edit
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Contact selected = row.getItem();

                    fieldFirst.setText(selected.getFirstname());
                    fieldLast.setText(selected.getLastname());
                    fieldAddress.setText(selected.getAddress());
                    fieldCity.setText(selected.getCity());
                    fieldState.setText(selected.getState());
                    fieldZip.setText(selected.getZipcode());
                    datepickerBirthdate.setValue(selected.getBirthDate());
                    filterString = "";
                }
            });

            return row;
        });

        updateTableView();
    }
}
