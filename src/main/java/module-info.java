module hu.petrik.dogsdbfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.petrik.dogsdbfx to javafx.fxml;
    exports hu.petrik.dogsdbfx;
}