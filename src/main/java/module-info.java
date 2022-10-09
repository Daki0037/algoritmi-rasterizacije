module me.danilo.rasterizacija {
    requires javafx.controls;
    requires javafx.fxml;

    exports me.danilo.rasterizacija.controllers to javafx.fxml;
    opens me.danilo.rasterizacija.controllers to javafx.fxml;

    opens me.danilo.rasterizacija to javafx.fxml;
    exports me.danilo.rasterizacija;
}