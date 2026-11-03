package co.edu.uniquindio.poo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

public class Monedero {
    private final String codigo;
    private final String nombre;
    private double saldo;
    private final TipoMonedero tipoMonedero;
    private Usuario usuario;
    //El umbral determina cuál es el saldo bajo
    private final double umbralSaldoBajo= 10000;
    private boolean alertaEnviada;
    private final List<ObservadorSaldo> observadores = new ArrayList<>();
    private final List<ObservadorTransaccionProgramada> observadoresTransaccionProgramada= new ArrayList<>();
    private ArrayList<Transaccion> listaTransacciones;
    private ArrayList<TransaccionProgramada> listaTransaccionesProgramadas;

    public Monedero(String codigo, String nombre,  double saldo, TipoMonedero tipoMonedero) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.saldo = saldo;
        this.tipoMonedero = tipoMonedero;
        this.listaTransacciones=new ArrayList<>();
        this.listaTransaccionesProgramadas=new ArrayList<>();

    }


    public TipoMonedero getTipoMonedero() {
        return tipoMonedero;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldo() {
        verificarSaldoBajo();
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public TipoMonedero getTipo() {
        return tipoMonedero;
    }

    public ArrayList<Transaccion> getListaTransacciones() {
        return listaTransacciones;
    }

    public void setListaTransacciones(ArrayList<Transaccion> listaTransacciones) {
        this.listaTransacciones = listaTransacciones;
    }

    public ArrayList<TransaccionProgramada> getListaTransaccionesProgramadas() {
        return listaTransaccionesProgramadas;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        //"Inscribe" al usuario para que reciba notificaciones al correo 
        NotificadorCorreo notificadorCorreo = new NotificadorCorreo(usuario.getCorreo());
        NotificadorWhatsApp notificadorWhatsApp = new NotificadorWhatsApp(usuario.getTelefono());
        NotificadorSMS notificadorSMS = new NotificadorSMS(usuario.getTelefono());
        //Observadores para notificaciones de saldo bajo
        observadores.add(notificadorCorreo);
        observadores.add(notificadorWhatsApp);
        observadores.add(notificadorSMS);
        //Observadores para notificaciones de transacciones programadas
        observadoresTransaccionProgramada.add(notificadorCorreo);
        observadoresTransaccionProgramada.add(notificadorWhatsApp);
        observadoresTransaccionProgramada.add(notificadorSMS);
    }   

    public Usuario getUsuario() {
        return usuario;
    }

    public void setListaTransaccionesProgramadas(ArrayList<TransaccionProgramada> listaTransaccionesProgramadas) {
        this.listaTransaccionesProgramadas = listaTransaccionesProgramadas;
    }
    public void agregarTransaccion (Transaccion transaccion){
        listaTransacciones.add(transaccion);
    }
    public void agregarTransaccionProgramada (TransaccionProgramada transaccion){
        listaTransaccionesProgramadas.add(transaccion);
    }
    public void aplicarbeneficio1000(){
        setSaldo(saldo +50);
    }

    public void verificarSaldoBajo(){
        if(saldo<=umbralSaldoBajo && !alertaEnviada){
            alertaEnviada=true;
            for(ObservadorSaldo observador: observadores){
                observador.saldoBajo(this);
            }
        }
    }

    public void recordarTransaccionesProgramadas(List<TransaccionProgramada> ordenadas, LocalDate hoy, int diasAnticipacion){
        //establecer el límite para el recordatorio
        LocalDate limiteFecha= LocalDate.now().plusDays(3);
        //recorrer las transacciones programadas que ya deben estar ordenadas
        for(TransaccionProgramada transaccionProgramada: listaTransaccionesProgramadas){
            //Si la fecha de ejecución es después del límite que se puso, no se hace nada
            if(transaccionProgramada.getFechaEjecucion().isAfter(limiteFecha)){
                break;
            }else if (transaccionProgramada.getFechaEjecucion().isEqual(limiteFecha) || transaccionProgramada.getFechaEjecucion().isBefore(limiteFecha)){
               //Recorre los observadores para que cada uno haga la notificación a su manera
                for(ObservadorTransaccionProgramada observador: observadoresTransaccionProgramada){
                    observador.recordatorioTransaccionProgramada(transaccionProgramada);
                }
            }
        }
    }

    

}


