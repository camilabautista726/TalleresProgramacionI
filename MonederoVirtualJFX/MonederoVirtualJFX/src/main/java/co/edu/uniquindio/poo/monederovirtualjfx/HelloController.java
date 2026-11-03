package co.edu.uniquindio.poo.monederovirtualjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador de la vista de ejemplo.
 * Puedes reemplazar esta clase por el controlador real de tu monedero.
 */
public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Bienvenido a Monedero Virtual");
    }
}
