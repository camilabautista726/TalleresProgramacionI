package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class Transferencia extends Transaccion {
    private double comision;
    private final String destino;
    private boolean conDescuento;
    public Transferencia(String codigo, LocalDate fecha, double monto, String destino, boolean conDescuento) {
        super(codigo, fecha, monto);
        this.destino=destino; // comision no se ingresa porque se calula aqui dentro
        this.conDescuento= conDescuento;
    }
     public String getDestino() {
        return destino;
    }

    public double aplicarComision(){ 
        double comision = monto * 0.01;
        if (conDescuento) {
            comision = comision - (comision * 0.1);   // resta el 10% de la comision
        }
        return comision;
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


