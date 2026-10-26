package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class Deposito extends Transaccion{
    public Deposito(String codigo, LocalDate fecha, double monto){ //Parametros al crear el objeto
        super(codigo, fecha, monto); // llama al constructor de transaccion
    }

    @Override
    public void aplicar(Monedero monedero) {
        monedero.setSaldo(monedero.getSaldo()+monto); 
    
    }

    @Override
    public int calcularPuntos () {
        double puntos = (monto)/100;
        int puntosEnteros= (int) puntos;
        return puntosEnteros;
    }

    @Override
    public void determinarAtipica() {
    
    }
}

