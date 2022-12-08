module hu.petrik.dogsdbfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports hu.petrik.dogsdbfx;
    opens hu.petrik.dogsdbfx to javafx.fxml;
}