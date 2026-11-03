package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

import javax.swing.JOptionPane;

public class NotificacionWhatsApp extends Notificacion {
    public NotificacionWhatsApp(String mensaje, LocalDate fecha, TipoAlerta tipo, String destinatario) {
        super(mensaje, fecha, tipo, destinatario); // invova el contructor
    }

    @Override
    public void enviar() {
        JOptionPane.showMessageDialog(null, "Se ha enviado un WhatsApp al número " + getDestinatario() + ": " + getMensaje());
    }
}
