package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

import javax.swing.JOptionPane;

public class NotificacionSMS extends Notificacion  {
        public NotificacionSMS(String mensaje, LocalDate fecha, TipoAlerta tipo, String destinatario) {
            super(mensaje, fecha, tipo, destinatario); // invova el contructor
        }
    @Override
    public void enviar() {
        JOptionPane.showMessageDialog(null, "Se ha enviado un SMS al número " + getDestinatario() + ": " + getMensaje());
    }
}

