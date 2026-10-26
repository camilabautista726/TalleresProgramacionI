package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public abstract class Transaccion {
    protected final String codigo;
    protected final LocalDate fecha;
    protected final double monto;
    protected  boolean atipica;
    protected EstadoTransaccion estado;
    public Transaccion(String codigo, LocalDate fecha, double monto) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.monto = monto;
        this.estado= EstadoTransaccion.COMPLETADA;
    }

    public String getCodigo() {
        return codigo;
    } //metodos generales

    public LocalDate getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }

    public EstadoTransaccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransaccion estado) {
        this.estado = estado;
    }

    public boolean getAtipica() {
        return atipica;
    }

    public void setAtipica(boolean atipica) {
        this.atipica = atipica;
    }

    public abstract void aplicar(Monedero monedero);
    public abstract int calcularPuntos(); //Los metodos abstractos no tienen cuerpo porque cada subclase los ejerce segun la necesidad
    public abstract void determinarAtipica();
}

