package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

import javax.swing.JOptionPane;

public class NotificacionCorreo extends Notificacion {
    public NotificacionCorreo(String mensaje, LocalDate fecha, TipoAlerta tipo, String destinatario) {
        super(mensaje, fecha, tipo, destinatario); // invova el contructor
    }

    @Override
    public void enviar() {
        JOptionPane.showMessageDialog(null, "Se ha enviado un correo a " + getDestinatario() + ": " + getMensaje());
    }

    
}
