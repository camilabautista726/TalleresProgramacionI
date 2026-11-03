package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

public class NotificadorWhatsApp implements ObservadorSaldo, ObservadorTransaccionProgramada {
    private final String numeroTelefono;

    public NotificadorWhatsApp(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    @Override
    public void saldoBajo(Monedero monedero) {
        String mensaje= "El saldo del monedero "+monedero.getCodigo()+" es bajo: "+monedero.getSaldo();
        Notificacion notificacion= new NotificacionWhatsApp(mensaje, LocalDate.now(), TipoAlerta.SALDO_BAJO, monedero.getUsuario().getTelefono());
        notificacion.enviar();
        monedero.getUsuario().getListaNotificaciones().add(notificacion);
    }
    @Override 
    public void recordatorioTransaccionProgramada ( TransaccionProgramada transaccionProgramada ){
        String mensaje = "Usuario, recuerda que la transacción programada"+ transaccionProgramada.getTransaccion()+
         "con el código "+ transaccionProgramada.getCodigo()+ 
        " por monto de "+ transaccionProgramada.getTransaccion().getMonto()+ " se realizará en la fecha: "+transaccionProgramada.getFechaEjecucion();
        Notificacion notificacion = new NotificacionWhatsApp(mensaje, LocalDate.now(), TipoAlerta.TRANSACCION_PROGRAMADA, transaccionProgramada.getMonedero().getUsuario().getTelefono());
        notificacion.enviar();
        transaccionProgramada.getMonedero().getUsuario().getListaNotificaciones().add(notificacion);
    } 
}
