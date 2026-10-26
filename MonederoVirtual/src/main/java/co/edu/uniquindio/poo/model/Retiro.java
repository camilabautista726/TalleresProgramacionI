package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class Retiro extends Transaccion{
    public Retiro(String codigo, LocalDate fecha, double monto){
        super(codigo, fecha, monto);
    }

    @Override
    public void aplicar() {
    }

    @Override
    public int calcularPuntos() {
        return 0;
    }

    @Override
    public void determinarAtipica() {

    }
}

