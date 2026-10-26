package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class NotificacionWhatsApp extends Notificacion {
    public NotificacionWhatsApp(String mensaje, LocalDate fecha, TipoAlerta tipo, String destinatario) {
        super(mensaje, fecha, tipo, destinatario); // invova el contructor
    }

    @Override
    public void enviar() {
    }
}
