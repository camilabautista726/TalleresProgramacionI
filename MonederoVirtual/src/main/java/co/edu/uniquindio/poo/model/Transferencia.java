package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class Transferencia extends Transaccion {
    private double comision;
    private final String destino;
    public Transferencia(String codigo, LocalDate fecha, double monto, String destino) {
        super(codigo, fecha, monto);
        this.destino=destino; // comision no se ingresa porque se calula aqui dentro
    }
    public double aplicarComision(){ //metodo propio
        return monto*0.01;
    }

    @Override
    public void aplicar(Monedero monedero){//Aca hay que restarle la comision
        monedero.setSaldo(monedero.getSaldo()-monto- aplicarComision());
    }

    @Override
    public int calcularPuntos() {
        return (int) monto/100*3;

    }

    @Override
    public void determinarAtipica() {

    }
}

