package hu.petrik.dogsdbfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class DogController {
    @FXML
    public TableView table;
    @FXML
    public TableColumn nameCol;
    @FXML
    public TableColumn ageCol;
    @FXML
    public TableColumn breedCol;
    private DogDB db;

    @FXML
    public void initialize(){
        nameCol.setCellFactory(new PropertyValueFactory<>("name"));
        ageCol.setCellFactory(new PropertyValueFactory<>("age"));
        breedCol.setCellFactory(new PropertyValueFactory<>("breed"));

        try {
            db = new DogDB();
            readDogs();
        } catch (SQLException e) {
            Platform.runLater(()->{
                alert(Alert.AlertType.ERROR, "Hiba történt az adatbázis kapcsolat kialakításakor",e.getMessage());
                Platform.exit();
            });
        }
    }

    private void alert(Alert.AlertType alertType, String headerText, String contentText){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void readDogs() throws SQLException {
        List<Dog> dogs = db.readDogs();
        table.getItems().clear();
        table.getItems().addAll(dogs);
    }
}