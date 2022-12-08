package hu.petrik.dogsdbfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

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
                alert(Alert.AlertType.ERROR,
                        "Hiba történt az adatbázis kapcsolat kialakításakor",
                        e.getMessage());
                Platform.exit();
            });
        }
    }

    private void readDogs() throws SQLException {
        List<Dog> dogs = db.readDogs();
        dogTable.getItems().clear();
        dogTable.getItems().addAll(dogs);
    }

    private void alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
    }

    @FXML
    public void submitClick(ActionEvent actionEvent) {
        String name = nameInput.getText().trim();
        int age = ageInput.getValue();
        String breed = breedInput.getText().trim();
        if(name.isEmpty()){
            alert();
        }
    }

    @FXML
    public void cancelClick(ActionEvent actionEvent) {
    }
}