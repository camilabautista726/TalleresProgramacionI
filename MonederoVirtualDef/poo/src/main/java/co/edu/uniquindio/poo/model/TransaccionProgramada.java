package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class TransaccionProgramada {
    private final String codigo;
    private LocalDate fechaEjecucion;
    private Frecuencia frencuenciaProgramada;
    private EstadoTransaccion estado;
    private Transaccion transaccion;
    private Monedero monedero;

    public TransaccionProgramada(String codigo, LocalDate fechaEjecucion, Frecuencia frencuenciaProgramada, Transaccion transaccion, Monedero monedero) {
        this.codigo = codigo;
        this.fechaEjecucion = fechaEjecucion;
        this.frencuenciaProgramada = frencuenciaProgramada;
        this.estado=EstadoTransaccion.PENDIENTE;
        this.transaccion= transaccion;
        this.monedero= monedero;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(LocalDate fechaEjecucion) {
    this.fechaEjecucion = fechaEjecucion;
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

    public Transaccion getTransaccion(){
        return transaccion;
    }

    public Monedero getMonedero() {
        return monedero;
    }
}

