package co.edu.uniquindio.poo.model;

public interface ObservadorSaldo {
    //Esta clase interfaz es para que se notifique al usuario si su saldo está bajo
    //Es como si esta clase preguntara quién puede notificarlo y la clase que diga que puede hacerlo
    //Lo notifique pero a su manera, es decir, la clase que implemente esta interfaz es la que decide cómo notificar al usuario
    void saldoBajo(Monedero monedero);
}
