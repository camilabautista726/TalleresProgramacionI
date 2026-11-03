package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public  abstract class Notificacion {
    private final String mensaje;
    private final LocalDate fechaNotificacion;
    private final TipoAlerta tipoAlerta;
    private String destinatario;

    public Notificacion(String mensaje, LocalDate fechaNotificacion, TipoAlerta tipoAlerta, String destinatario) {
        this.mensaje = mensaje;
        this.fechaNotificacion = fechaNotificacion;
        this.tipoAlerta = tipoAlerta;
        this.destinatario = destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDate getFechaNotificacion() {
        return fechaNotificacion;
    }

    public TipoAlerta getTipoAlerta() {
        return tipoAlerta;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    public abstract void enviar();
}
