package co.edu.uniquindio.poo.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import java.time.LocalDate;      

public class MonederoVirtual {
    private final String nombre;
    private final String nit;
    private ArrayList<Monedero> listaTodosMonederos;
    private ArrayList<Usuario> listaUsuarios;
    private ArrayList<Beneficio> listaBeneficios;
    private int contadorTransacciones;

    public MonederoVirtual(String nombre, String nit) {
        this.nombre = nombre;
        this.nit = nit;
        this.listaTodosMonederos = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
        this.listaBeneficios = new ArrayList<>();
        this.contadorTransacciones = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Usuario> getListaClientes() {
        return listaUsuarios;
    }

    public void setListaClientes(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    //CRUD USUARIO
    public String crearUsuario(Usuario usuario) {
        if (buscarUsuarioDocumentoIdentidad(usuario.getDocumentoIdentidad()).isEmpty()) {
            listaUsuarios.add(usuario);
            return "Cliente registrado con exito";
        }
        return "Ya hay un cliente con esa información, por favor verifique el documento de identidad";
    }

    public Optional<Usuario> buscarUsuarioDocumentoIdentidad(String documentoIdentidad) {
        return listaUsuarios.stream().filter(usuario -> documentoIdentidad.equals(usuario.getDocumentoIdentidad())).findFirst();
    }

    public Monedero crearMonedero(Usuario usuario, String codigo, String nombre,
                                  TipoMonedero tipo, double saldo) {
        Monedero monedero = new Monedero(codigo, nombre, saldo, tipo);
        listaTodosMonederos.add(monedero);   // lista de TODOS
        usuario.agregarMonedero(monedero);   // lista de SUS monederos del cliente
        monedero.setUsuario(usuario); //Asigna el usuario al monedero para poder acceder a sus datos desde el monedero
        return monedero;
    }
        //CRUD BENEFICIO
        public void crearBeneficios (Beneficio beneficio){
            listaBeneficios.add(beneficio);
        }
        public boolean verificarCodigoMonederoExiste (String codigo){
            for (Monedero aux : listaTodosMonederos) {
                if (codigo.equals(aux.getCodigo())) {
                    return true;
                }
            }  
            return false;
        }
        public Optional<Monedero> buscarMonederoPorCodigo(String codigo){
            return listaTodosMonederos.stream().filter(m-> m.getCodigo().equals(codigo)).findFirst();
        }
        public String transferir( Usuario usuarioActual, Monedero monederoOrigen, double monto, String codigoDestinatario, LocalDate fecha, boolean programada,  Frecuencia frecuencia)
        throws MontoInvalidoException, SaldoInsuficienteException{
            Optional<Monedero> monederoDestinatario= buscarMonederoPorCodigo(codigoDestinatario);
            if(monederoDestinatario.isEmpty()){
                return "No existe un monedero con ese código";// Verificar si se puede hacer 
            }
             if(monto<=0){
                throw new MontoInvalidoException ("El valor a transferir debe ser mayor a 0");
            } 
             
            if (monederoDestinatario.get() == monederoOrigen) {
                 return "No puedes transferir al mismo monedero";

            }
            
            Monedero monederoDestino=monederoDestinatario.get();

            String codigoTransferencia= generarCodigoTransaccion();
            boolean conDescuento = usuarioActual.getDescuentoTransferencia();
            Transaccion transferencia= new Transferencia(codigoTransferencia, fecha , monto, codigoDestinatario, conDescuento );
            Transferencia t1=  (Transferencia)transferencia; //Se convierte a transferencia para poderle aplicar el metodo propio
            double comision=t1.aplicarComision();

             if(monederoOrigen.getSaldo()<monto+ comision){
                throw new SaldoInsuficienteException ("Saldo insuficiente para realizar la transferencia");
            }
            usuarioActual.setDescuentoTransferencia(false); // cuando gaste el beneficio
            boolean atipica=usuarioActual.detectarTransaccionesAtipicas(monto);
            transferencia.setAtipica(atipica);
            //Si la transaccion es programada, no se crea y se descuenta del saldo hasta la fecha acordada
            if(programada){
                 TransaccionProgramada transaccionProgramada = new TransaccionProgramada(codigoTransferencia,fecha, frecuencia, transferencia, monederoDestino);
                 monederoOrigen.agregarTransaccionProgramada(transaccionProgramada);
                 return "La transferencia se programó con éxito.\n "+
                 "El código de esta transacción es: "+codigoTransferencia+"\n"+
                 "La fecha programada es: "+fecha;
            }
            transferencia.aplicar(monederoOrigen);
            registrarTransaccion(monederoOrigen, transferencia);
            int puntos=transferencia.calcularPuntos(); 
            Transaccion nuevoDeposito= new Deposito (generarCodigoTransaccion(), fecha, monto);
            nuevoDeposito.aplicar(monederoDestino);
            registrarTransaccion(monederoDestino, nuevoDeposito);

            usuarioActual.setPuntosAcumulados(usuarioActual.getPuntosAcumulados()+puntos);
            usuarioActual.setRango(); // ya lo cambia con los puntos acumulados
        
            return "La transferencia se realizo con exito, su código es:"+ codigoTransferencia+"\n"+
            "Comision: $" + comision+"\n"+
            "Los puntos que ganó con esta transferencia son: "+puntos;

        }

        public String depositar(Usuario usuarioActual, Monedero monedero, double monto, LocalDate fecha, boolean programada, Frecuencia frecuencia)
        throws MontoInvalidoException{
            if(monto<=0){
                throw new MontoInvalidoException ("El monto del deposito debe ser mayor a 0");
            }
            String codigo= generarCodigoTransaccion();
            Transaccion deposito= new Deposito(codigo, fecha , monto);
            boolean atipica=usuarioActual.detectarTransaccionesAtipicas(monto);
            deposito.setAtipica(atipica);
            //Si la transaccion es programada, no se crea y se descuenta del saldo hasta la fecha acordada
            if(programada){
                 TransaccionProgramada transaccionProgramada = new TransaccionProgramada(codigo, fecha, frecuencia, deposito, monedero);
                 monedero.agregarTransaccionProgramada(transaccionProgramada);
                 return "El deposito se programó con éxito.\n "+
                 "El código de esta transacción es: "+codigo+"\n"+
                 "La fecha programada es: "+fecha;
            }
            deposito.aplicar(monedero);
            registrarTransaccion(monedero, deposito);
            int puntos= deposito.calcularPuntos();
            usuarioActual.setPuntosAcumulados(usuarioActual.getPuntosAcumulados()+puntos);
            usuarioActual.setRango(); 
            return "El deposito se realizó con éxito, su código es: "+codigo+" \n"+
            "Los puntos que ganó con esta transacción son: "+puntos;
        }
        public String retirar(Usuario usuarioActual,  Monedero monedero, double monto)
        throws MontoInvalidoException, SaldoInsuficienteException{
            if(monto<=0){
                throw new MontoInvalidoException ("El valor a retirar debe ser mayor a 0");
            }
            String codigoRetiro=generarCodigoTransaccion();
            LocalDate fin= usuarioActual.getFechaSinCargosRetiros();
            boolean sinCargo= fin!=null && !LocalDate.now().isAfter(fin);
            Transaccion retiro= new Retiro (codigoRetiro, LocalDate.now(), monto, sinCargo);
            Retiro r1=(Retiro)retiro;
            double cargo= r1.calcularCargo();
             if(monedero.getSaldo()<monto+cargo){
                throw new SaldoInsuficienteException("Saldo insuficiente para hacer el retiro");
            }
            retiro.aplicar(monedero);
            boolean atipica= usuarioActual.detectarTransaccionesAtipicas(monto);
            retiro.setAtipica(atipica);
            registrarTransaccion(monedero, retiro);

            int puntos=retiro.calcularPuntos();
            usuarioActual.setPuntosAcumulados(usuarioActual.getPuntosAcumulados()+puntos);
            usuarioActual.setRango();

            return "El retiro se realizó con éxito, su código es: "+codigoRetiro+" \n"+
            "Cargo: $" + cargo+ "\n"+
            "Los puntos que ganó con esta transacción son: "+puntos;
        }
       
        //Metodo para agregar cualquier tipo de transaccion a un monedero, ya sea deposito, retiro o transferencia
        public void registrarTransaccion(Monedero monedero,Transaccion transaccion){
            monedero.agregarTransaccion(transaccion); 
        }


        public String generarCodigoTransaccion(){
            contadorTransacciones++;
            return "T" + contadorTransacciones; //genera el codigo solito cada que se va hacer cualquier transaccion
        }

    
        //Método que busca cuáles transacciones se deben aplicar hoy y las ejecuta de acuerdo a su tipo de transaccion
        public void ejecutarTransaccionesProgramadas(){
            for(Monedero monedero: listaTodosMonederos){
            //Algoritmo de transacciones
            ArrayList<TransaccionProgramada> listaOrdenadas= monedero.getListaTransaccionesProgramadas().stream().sorted(Comparator.comparing
                (programada -> programada.getFechaEjecucion())).collect(Collectors.toCollection(ArrayList::new));
           //Ejecuta y programa
            LocalDate hoy= LocalDate.now();
            for(TransaccionProgramada programada: listaOrdenadas){
                if(programada.getFechaEjecucion().isAfter(hoy)){
                    break; // como están ordenadas, las siguientes también son futuras
                }
                if(programada.getEstado()== EstadoTransaccion.PENDIENTE && programada.getFechaEjecucion().equals(hoy)&& !programada.getFechaEjecucion().isAfter(hoy)){
                     Transaccion transaccion = programada.getTransaccion();
                     if(programada.getMonedero().getSaldo()<= 0) {
                        break;
                     }
                     if(programada.getTransaccion() instanceof Transferencia){
                        Transferencia transferencia= (Transferencia) programada.getTransaccion();
                        Optional<Monedero> monederoDestino= buscarMonederoPorCodigo(transferencia.getDestino());
                        if(monedero.getSaldo()< transferencia.getMonto()+ transferencia.aplicarComision()){
                         continue; // no alcanza el saldo, queda pendiente
                        }
                        if(monederoDestino.isPresent()){
                        Transaccion nuevoDeposito= new Deposito (generarCodigoTransaccion(), hoy, transferencia.getMonto());
                        nuevoDeposito.aplicar(monederoDestino.get());
                        registrarTransaccion(monederoDestino.get(), nuevoDeposito);
                        }
                     }
                    transaccion.aplicar(programada.getMonedero());
                     programada.setEstado(EstadoTransaccion. COMPLETADA);
                    }
                    Frecuencia frecuencia= programada.getFrencuenciaProgramada();
                    if(frecuencia!= null){
                        LocalDate proximaFecha= calcularProximaFecha(frecuencia, programada.getFechaEjecucion());
                        programada.setFechaEjecucion(proximaFecha);
                    }
                }
            }
        }

        //Método que me permite calcular la próxima fecha si es que hay una frecuencia
        public LocalDate calcularProximaFecha (Frecuencia frecuencia, LocalDate fecha){
            switch(frecuencia){
                case DIARIA:
                    return fecha.plusDays(1);
                case SEMANAL:
                    return fecha.plusWeeks(1);
                case MENSUAL:
                    return fecha.plusMonths(1);
                default:
                    return fecha;
            }
        }
    
        public String aplicarbeneficio100 (Usuario usuario)
        throws PuntosInsuficientesException{
            if(usuario.getPuntosAcumulados()<100){
                throw new PuntosInsuficientesException ("Puntos insuficientes para el canje");
            }
            usuario.setDescuentoTransferencia(true);
            usuario.setPuntosAcumulados(usuario.getPuntosAcumulados()-100);
            usuario.setRango();
            return  "El beneficio se canjeó exitosamante, ahora tiene un 10% de descuento en la comision de su proxima transferencia";
        }
         public String aplicarbeneficio500 (Usuario usuario)
         throws PuntosInsuficientesException{
            if(usuario.getPuntosAcumulados()<500){
                throw new PuntosInsuficientesException("Puntos insuficientes para el canje");
            }
            usuario.setFechaFinSinCargosRetiros(LocalDate.now().plusMonths(1));
            usuario.setPuntosAcumulados(usuario.getPuntosAcumulados()-500);
            usuario.setRango();
            return  "El beneficio se canjeó exitosamante, ahora tiene un mes sin cargos por retiro";
        }
         public String aplicarbeneficio1000 (Usuario usuario, Monedero monedero )
         throws PuntosInsuficientesException{
            if(usuario.getPuntosAcumulados()<1000){
                throw new PuntosInsuficientesException ("Puntos insuficientes para el canje");
            }
            monedero.aplicarbeneficio1000();
            usuario.setPuntosAcumulados(usuario.getPuntosAcumulados()-1000);
            usuario.setRango();
            return "El beneficio se canjeó exitosamente, ahora tiene 50 unidades más en su saldo";
        }

        
        public String consultarNotificaciones(Usuario usuario, String codigoMonedero){
        Optional<Monedero> monederoOptional= buscarMonederoPorCodigo(codigoMonedero);
        if(monederoOptional.isEmpty()){
            return "No existe un monedero con ese código";
        }
        if(monederoOptional.get().getUsuario()!=usuario){
            return "El monedero " + codigoMonedero + " no pertenece a este usuario";
        }
        ArrayList<Notificacion> listaNotificaciones= usuario.getListaNotificaciones();
        if(listaNotificaciones.isEmpty()){
            return "No hay notificaciones para este usuario";
        }
        StringBuilder resultado= new StringBuilder();
        for(Notificacion notificacion: listaNotificaciones){
            resultado.append("Tipo de notificación: "+notificacion.getTipoAlerta()+"\n"
            + "Fecha de la notificación: "+ notificacion.getFechaNotificacion()+"\n"
            + "Mensaje: "+ notificacion.getMensaje()+"\n");
        }
        return resultado.toString();
        }        

        public void enviarRecordatorios(Usuario usuario){
            for( Monedero monederosUsuario: usuario.getListaMonederos()){
                //Ordenarlas por si se han programado nuevas transacciones por la frecuencia
                ArrayList<TransaccionProgramada> listaTransaccionesProgramadas= monederosUsuario.getListaTransaccionesProgramadas().stream()
                .sorted(Comparator.comparing(TransaccionProgramada:: getFechaEjecucion))
                .collect(Collectors.toCollection(ArrayList::new));
                monederosUsuario.recordarTransaccionesProgramadas(listaTransaccionesProgramadas, LocalDate.now(), 3);
            }
        }

        public String consultarHistorialTransacciones(Usuario usuarioActual, String codigoMonedero) {
            Optional<Monedero> monederoOptional= buscarMonederoPorCodigo(codigoMonedero);
            if(monederoOptional.isEmpty()){
            return "No existe un monedero con ese código";
            }
            if(monederoOptional.get().getUsuario()!=usuarioActual){
            return "El monedero " + codigoMonedero + " no pertenece a este usuario";
            }
            ArrayList<Transaccion> listaTransacciones= monederoOptional.get().getListaTransacciones();
                if(listaTransacciones.isEmpty()){
                return "No hay transacciones para este usuario";
                }
            StringBuilder resultado= new StringBuilder();
                for(Transaccion transaccion: listaTransacciones){
                resultado.append("Código de la transacción: "+transaccion.getCodigo()+"\n"
                + "Fecha de la notificación: "+ transaccion.getFecha()+"\n"
                + "Estado: "+ transaccion.getEstado()+"\n"
                + "Monto: "+ transaccion.getMonto()+"\n"
                //Acá el getClass le pregunta a cuál clase hija pertenece la transacción
                // y el getSimpleName devuelve el nombre de la clase sin el co.edu.uniquindio...
                + "Tipo de transacción: "+ transaccion.getClass().getSimpleName()+"\n");
                }
            return resultado.toString();
        }

}


