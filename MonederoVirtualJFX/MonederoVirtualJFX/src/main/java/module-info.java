module co.edu.uniquindio.poo.monederovirtualjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;

    exports co.edu.uniquindio.poo.monederovirtualjfx;
    exports co.edu.uniquindio.poo.monederovirtualjfx.model;
    opens co.edu.uniquindio.poo.monederovirtualjfx to javafx.fxml;
}
