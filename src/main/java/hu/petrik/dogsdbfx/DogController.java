package hu.petrik.dogsdbfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DogController {

    @FXML
    private TableView<Dog> dogTable;
    @FXML
    private TableColumn<Dog, String> nameCol;
    @FXML
    private TableColumn<Dog, Integer> ageCol;
    @FXML
    private TableColumn<Dog, String> breedCol;
    private DogDB db;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField breedInput;
    @FXML
    private Spinner<Integer> ageInput;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    private int updateId;

    @FXML
    private void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        breedCol.setCellValueFactory(new PropertyValueFactory<>("breed"));
        ageInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50));
        try {
            db = new DogDB();
            readDogs();
        } catch (SQLException e) {
            Platform.runLater(() -> {
                sqlAlert(e);
                Platform.exit();
            });
        }
    }

    private void sqlAlert(SQLException e) {
        alert(Alert.AlertType.ERROR,
                "Hiba történt az adatbázis kapcsolat kialakításakor",
                e.getMessage());
    }

    private void readDogs() throws SQLException {
        List<Dog> dogs = db.readDogs();
        dogTable.getItems().clear();
        dogTable.getItems().addAll(dogs);
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
        Dog selected = getSelectedDog();
        if(selected == null) return;
        updateId = selected.getId();
        nameInput.setText(selected.getName());
        ageInput.getValueFactory().setValue(selected.getAge());
        breedInput.setText(selected.getBreed());
        submitButton.setText("Update");
    }

    private void setStateToUpdate() {
        submitButton.setText("Update");
        dogTable.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        Dog selected = getSelectedDog();
        if (selected == null) return;

        try {
            if (db.deleteDog(selected.getId())) {
                alert(Alert.AlertType.WARNING, "Sikeres törlés", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen törlés", "");
            }
            readDogs();
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }

    private Dog getSelectedDog() {
        int selectedIndex = dogTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1){
            alert(Alert.AlertType.WARNING, "Törléshez válasszon ki egy kutyát a táblázatból","");
            return null;
        }
        Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,
                "Biztos, hogy törölni szeretné a viláasztott kutyát?", "");
        if (optionalButtonType.isEmpty() ||
                (!optionalButtonType.get().equals(ButtonType.OK) &&
                        !optionalButtonType.get().equals(ButtonType.YES))) {
            return null;
        }
        Dog selected = dogTable.getSelectionModel().getSelectedItem();
        return selected;
    }

    @FXML
    public void submitClick(ActionEvent actionEvent) {
        String name = nameInput.getText().trim();
        int age = ageInput.getValue();
        String breed = breedInput.getText().trim();
        if(name.isEmpty()){
            alert(Alert.AlertType.ERROR, "Név megadása kötelező","");
            return;
        }
        if(breed.isEmpty()){
            alert(Alert.AlertType.ERROR, "Fajta megadása kötelező","");
            return;
        }

        if(submitButton.getText().equals("Update")){
            updateDog(name, age, breed);
        }else{
            createDog(name, age, breed);
        }

    }

    private void updateDog(String name, int age, String breed) {
        Dog dog = new Dog(updateId, name, age, breed);
        try {
            if (db.updateDog(dog)) {
                alert(Alert.AlertType.WARNING, "Sikeres módosítás", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen módosítás", "");
            }
            resetForm();
            readDogs();
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }


    private void createDog(String name, int age, String breed) {
        Dog dog = new Dog(name, age, breed);
        try {
            if(db.createDog(dog)){
                alert(Alert.AlertType.WARNING, "Sikeres felvétel", "");
                readDogs();
                resetForm();
            }else{
                alert(Alert.AlertType.WARNING, "Siktertelen felvétel", "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetForm() {
        nameInput.setText("");
        ageInput.getValueFactory().setValue(0);
        breedInput.setText("");
        submitButton.setText("Submit");
        dogTable.setDisable(false);
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    @FXML
    public void cancelClick(ActionEvent actionEvent) {
        resetForm();
    }
}