package co.edu.uniquindio.poo.model;
import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;

public class Usuario {
    private final String documentoIdentidad;
    private final String nombreCompleto;
    private String telefono;
    private String correo;
    private Rango rango=Rango.BRONCE;
    private String contrasenia;
    private int puntosAcumulados;
    private boolean descuentoTransferencia;
    private LocalDate FechaFinSinCargosRetiros;
    ArrayList<Monedero> listaMonederos;
    ArrayList<Notificacion> listaNotificaciones;

    public Usuario(String documentoIdentidad, String nombreCompleto, String telefono, String correo, String contrasenia) {
        this.documentoIdentidad = documentoIdentidad;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasenia=contrasenia;
        this.listaMonederos=new ArrayList<>();
        this.listaNotificaciones=new ArrayList<>();
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Rango getRango() {
        return rango;
    }

    public void setRango() {
        if(puntosAcumulados<=500){
        this.rango = Rango.BRONCE;
        }
        else if(puntosAcumulados>=501 && puntosAcumulados<=1000){
        this.rango = Rango.PLATA;
        }
        else if(puntosAcumulados>=1001 && puntosAcumulados<=5000){
        this.rango = Rango.ORO;
        }
        else if(puntosAcumulados>=1001 && puntosAcumulados<=5000){
        this.rango = Rango.PLATINO;
        }
    }

    public int getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(int puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public ArrayList<Monedero> getListaMonederos() {
        return listaMonederos;
    }

    public void setListaMonederos(ArrayList<Monedero> listaMonederos) {
        this.listaMonederos = listaMonederos;
    }

    public ArrayList<Notificacion> getListaNotificaciones() {
        return listaNotificaciones;
    }

    public void setListaNotificaciones(ArrayList<Notificacion> listaNotificaciones) {
        this.listaNotificaciones = listaNotificaciones;
    }
    public void setDescuentoTransferencia(boolean booleano) {
        this.descuentoTransferencia= booleano;
    }

    public boolean getDescuentoTransferencia() {
        return descuentoTransferencia;
    }
    public void setFechaFinSinCargosRetiros(LocalDate fecha) {
        this.descuentoTransferencia= fecha;
    }

    public LocalDate getFechaSinCargosRetiros() {
        return FechaSinCargosRetiros;
    }


    public boolean verificarContraseña(String contrasenia){
        return contrasenia.equals(this.contrasenia);
    }
    public void agregarMonedero(Monedero monedero){
        listaMonederos.add(monedero);
    }
    public Optional <Monedero> buscarMonederoUsuario(String codigo){
        return listaMonederos.stream().filter(m -> codigo.equals(m.getCodigo())).findFirst();
    }

    public boolean detectarTransaccionesAtipicas(double monto){
        double suma=0;
        int contadorTransacciones=0;
        double promedio=0;
        for(Monedero m: listaMonederos){
            for(Transaccion t: m.getListaTransacciones()){
                contadorTransacciones++;
                suma+=t.getMonto();
            }
        }
        if(contadorTransacciones<=3){
            return false;
        }
        promedio= suma/contadorTransacciones;
        double desviacionEstandar=0;
        double valorMenosPromedio=0;

        for(Monedero m: listaMonederos){
            for(Transaccion t: m.getListaTransacciones()){
                valorMenosPromedio+= Math.pow(t.getMonto()-promedio, 2);
            }
        }
        desviacionEstandar= Math.sqrt(valorMenosPromedio/contadorTransacciones);
        double limite= promedio + 2*desviacionEstandar;
        if(monto>limite){
            return true;
        }
        return false;
    }
    
    public String ConsultarpuntosYRango(){
        return "Puntos de"+ nombreCompleto + ":\n"+  + puntosAcumulados + "\n"+
                "Su rango es:"+ "\n"+ rango;
               
    }
    public String consultarReportesGastos(){
        double totalDepositado=0;
        double totalTransferido=0;
        double totalRetirado=0;
        int contadorTransacciones = 0;
        for(Monedero m: listaMonederos){
            for(Transaccion t: m.getListaTransacciones()){
                contadorTransacciones++;
                if(t instanceof Deposito){
                  totalDepositado+= t.getMonto();  
                }
                if(t instanceof Transferencia){
                  totalTransferido+= t.getMonto();  
                }
                if(t instanceof Retiro){
                  totalRetirado+= t.getMonto();  
                }
            }
        }
        if(contadorTransacciones ==0){
            return "Aun no tienes transacciones para generar el reporte";
        }
        return "Reporte de gastos de " + nombreCompleto + ":\n" +
                "Numero de transacciones:\n" + contadorTransacciones + "\n" +
                "Total depositado:\n" + totalDepositado + "\n" +
                "Total transferido:\n" + totalTransferido + "\n" +
                "Total retirado:\n" + totalRetirado+ "\n" +
                "Su mayor gasto fué por\n" + encontrarMayorGasto() + "\n" +
                "Transacciones atipicas:\n" + consultarAtipicas()+"\n"; 
    }
    public double encontrarMayorGasto(){
        return listaMonederos.stream()
        .flatMap(monedero -> monedero.getListaTransacciones().stream())
        .filter(t -> !(t instanceof Deposito))
        .mapToDouble(Transaccion:: getMonto).max()
        .orElse(0);
    }
    public String consultarAtipicas(){
        String detallerAtipica="";
        for(Monedero m: listaMonederos){
            for(Transaccion t: m.getListaTransacciones()){
                if (t.getAtipica()) {
                detallerAtipica+= "Código: " + t.getCodigo() + "\n"
                                 + "Monto: $" + t.getMonto() + "\n"
                                 + "Fecha: " + t.getFecha() + "\n"
                                 + "-------------------\n";
                }
            }
        }
        if (detalleAtipicas.equals("")) {
        return "Ninguna";
    }
    return detalleAtipicas;
    }
}

