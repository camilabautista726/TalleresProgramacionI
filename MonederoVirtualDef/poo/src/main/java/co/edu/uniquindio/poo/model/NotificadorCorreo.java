package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class NotificadorCorreo implements ObservadorSaldo, ObservadorTransaccionProgramada {
    //La clase que implemente el observadorSaldo va a hacer lo que ella le pida pero a su modo
    private final String correo;
    //Correo al que va a llegar la notifiación
    public NotificadorCorreo(String correo) {
        this.correo = correo;
    }
    @Override 
    public void saldoBajo (Monedero monedero){
        String mensaje = "El saldo del monedero con codigo " + monedero.getCodigo() + " es bajo: " + monedero.getSaldo();
        Notificacion notificacion = new NotificacionCorreo(mensaje, LocalDate.now(), TipoAlerta.SALDO_BAJO, monedero.getUsuario().getCorreo());
        notificacion.enviar();
        monedero.getUsuario().getListaNotificaciones().add(notificacion);
    } 
     @Override 
    public void recordatorioTransaccionProgramada ( TransaccionProgramada transaccionProgramada ){
        String mensaje = "Usuario, recuerda que la transacción programada"+ transaccionProgramada.getTransaccion()+
         "con el código "+ transaccionProgramada.getCodigo()+ 
        " por monto de "+ transaccionProgramada.getTransaccion().getMonto()+ " se realizará en la fecha: "+transaccionProgramada.getFechaEjecucion();
        Notificacion notificacion = new NotificacionCorreo(mensaje, LocalDate.now(), TipoAlerta.TRANSACCION_PROGRAMADA, transaccionProgramada.getMonedero().getUsuario().getCorreo());
        notificacion.enviar();
        transaccionProgramada.getMonedero().getUsuario().getListaNotificaciones().add(notificacion);
    } 
}
