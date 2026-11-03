package co.edu.uniquindio.poo.model;
import java.time.LocalDate;

public class Retiro extends Transaccion{
    private boolean sinCargo;
    public Retiro(String codigo, LocalDate fecha, double monto, boolean sinCargo){
        super(codigo, fecha, monto);
        this.sinCargo= sinCargo;
    }

    @Override
    public void aplicar(Monedero monedero) {
        monedero.setSaldo(monedero.getSaldo()- monto);
    }

    @Override
    public int calcularPuntos() {
        return  (int) monto/100*2;
    }
    public double calcularCargo(){ 
        if (sinCargo) {
            return 0; 
        }
        return monto*0.01;
    }

    @Override
    public void determinarAtipica() {

    }
}

