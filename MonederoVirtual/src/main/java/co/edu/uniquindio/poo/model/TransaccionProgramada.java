package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class TransaccionProgramada {
    private final String codigo;
    private final LocalDate fechaEjecucion;
    private Frecuencia frencuenciaProgramada;
    private EstadoTransaccion estado;

    public TransaccionProgramada(String codigo, LocalDate fechaEjecucion, Frecuencia frencuenciaProgramada) {
        this.codigo = codigo;
        this.fechaEjecucion = fechaEjecucion;
        this.frencuenciaProgramada = frencuenciaProgramada;
        this.estado=EstadoTransaccion.PENDIENTE;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getFechaEjecucion() {
        return fechaEjecucion;
    }

    public Frecuencia getFrencuenciaProgramada() {
        return frencuenciaProgramada;
    }

    public void setFrencuenciaProgramada(Frecuencia frencuenciaProgramada) {
        this.frencuenciaProgramada = frencuenciaProgramada;
    }

    public EstadoTransaccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransaccion estado) {
        this.estado = estado;
    }
}

