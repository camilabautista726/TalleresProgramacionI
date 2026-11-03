package co.edu.uniquindio.poo.app;


import co.edu.uniquindio.poo.model.*;
import javax.swing.*;
import java.time.LocalDate;
import java.util.Optional;  

public class Main {
   // static, la ven todos los métodos de la clase y la pueden modificar dependiendo si por ejemplo ya se inicio sesion
    static Usuario usuarioActual = null;
    static void main() {
        MonederoVirtual monederoVirtual= new MonederoVirtual("MonederoVirtual", "LGJDN04850Ñ");
        Beneficio beneficio100= new Beneficio("Beneficio100", (int)100, "Reducción del 10% en la comision por tranferencias");
        Beneficio beneficio500= new Beneficio("Beneficio500", (int)500, "Un mes sin cargos por retiro");
        Beneficio beneficio1000= new Beneficio("Beneficio1000", (int)1000, "Bono de saldo de 50 unidaddes en el monedero");
        monederoVirtual.crearBeneficios(beneficio100);
        monederoVirtual.crearBeneficios(beneficio500);
        monederoVirtual.crearBeneficios(beneficio1000);

        JOptionPane.showMessageDialog(null,"Bienvendio a Monedero Virtual");

        int opcion;
        do{
            try{
            JOptionPane.showMessageDialog(null,"===MENÚ===");
            opcion = Integer.valueOf(JOptionPane.showInputDialog(null,
                    "Por favor seleccione una opcion :\n" +
                            "1. Iniciar Sesion\n" +
                            "2. Registrarse \n"+
                            "0. Salir"));
                
            } catch (NumberFormatException | NullPointerException e) {
                opcion=-1;  //Si se ingresa un texto  o no se ingresa nada atrapa la excepcion 
            }
            switch (opcion){
                case 1:
                    iniciarSesion(monederoVirtual);
                    break;
                case 2:
                    registrarse(monederoVirtual);
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Gracias por usar monedero vitual");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opcion no valida");
                    break;
            }
            if(usuarioActual != null) { // entro o se registro y aquí se queda hasta cerrar sesion
                monederoVirtual.enviarRecordatorios(usuarioActual);
                int opcionMenu;
                do {
                    opcionMenu = mostrarMenuCliente(monederoVirtual);
                    switch (opcionMenu) {
                        case 1:
                            realizarTransferencia(monederoVirtual, LocalDate.now(), false, null);
                            break;
                        case 2:
                            realizarDeposito(monederoVirtual, LocalDate.now(), false, null);
                            break;
                        case 3:
                            realizarRetiro(monederoVirtual);
                            break;
                        case 4:
                            programarTransaccion(monederoVirtual);
                            break;
                        case 5:
                            consultarSaldo(monederoVirtual);
                            break;
                        case 6:
                            consultarpuntosYRango(monederoVirtual);
                            break;
                        case 7:
                            canjearPuntos(monederoVirtual);
                            break;
                        case 8:
                            consultarHistorialNotificaciones(monederoVirtual);
                            break;
                        case 9:
                            consultarHistorialTransacciones(monederoVirtual);
                            break;
                        case 10:
                            ConsultarReporteGastos(monederoVirtual);
                            break;
                        case 11:
                            crearOtroMonedero(monederoVirtual);
                            break;
                        case 0:
                            usuarioActual = null; // Aqui se cierra la sesion
                            JOptionPane.showMessageDialog(null, "Adios");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Opcion no valida");
                    }
                }while(opcionMenu!=0);
            }
        }while(opcion!=0);

    }
    //menú para cuando el usuario no ha iniciado sesion, es decir, cuando usuarioActual es diferente denull
    public static void   iniciarSesion (MonederoVirtual monederoVirtual){
        String documentoIdentidad = JOptionPane.showInputDialog(null, "Ingrese su documento de Indentidad");
        Optional<Usuario> usuarioEncontrado=monederoVirtual.buscarUsuarioDocumentoIdentidad(documentoIdentidad);
        if(usuarioEncontrado.isPresent()){
            Usuario usuario= usuarioEncontrado.get();
            int intento=0;
            while(intento<3 && usuarioActual == null){
                String contraseña = JOptionPane.showInputDialog(null, "Ingrese su contraseña:");
                if(usuario.verificarContraseña(contraseña)){
                    usuarioActual=usuario;
                    break; //Rompe el while cuando se ingrese bien la contraseña y el usuario ingrese
                }else{
                    intento++;
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta, le quedan :"+ (3-intento)+ "intentos");
                }
            }
        }else {
            JOptionPane.showInputDialog(null, "No se encontró usuario con ese documento de identidad");
        }
    }
    //menú para cuando el usuario no ha iniciado sesion, es decir, cuando usuarioActual es null
    public static void   registrarse (MonederoVirtual monederoVirtual){
        String documentoIdentidad= JOptionPane.showInputDialog(null, "Ingrese su documento de Indentidad");
        if (monederoVirtual.buscarUsuarioDocumentoIdentidad(documentoIdentidad).isPresent()) {
            JOptionPane.showMessageDialog(null, "Ya existe un usuario con ese documento");
            return; //No  retorna nada porque es void pero se sale del metodo
        }
        String nombreCompleto= JOptionPane.showInputDialog(null, "Ingrese su nombreCompleto");
        String telefono = JOptionPane.showInputDialog(null, "Ingrese su telefono");
        String correo= JOptionPane.showInputDialog(null, "Ingrese su correo");
        String contraseña= JOptionPane.showInputDialog(null, "cree una contraseña");
        Usuario usuario= new Usuario (documentoIdentidad, nombreCompleto,telefono, correo, contraseña);
        String resultado=monederoVirtual.crearUsuario(usuario);
        JOptionPane.showMessageDialog(null,resultado);
        String codigo;
        boolean existe;
        do {
            codigo = JOptionPane.showInputDialog(null, "Ingrese un código para su monedero:");
            existe = monederoVirtual.verificarCodigoMonederoExiste(codigo);
            if (existe) {
                JOptionPane.showMessageDialog(null, "Ese código ya existe, elija otro");
            }
        } while (existe); //Le pide el codigo al usuario hasta que encuentre uno que no exista
        monederoVirtual.crearMonedero(usuario, codigo, "PRINCIPAL", TipoMonedero.GASTOS_DIARIOS, 0);
        usuarioActual = usuario; // queda con la sesion iniciada
        JOptionPane.showMessageDialog(null, "Registro exitoso. Bienvenido " + nombreCompleto);
        }
        //Menú que se muestra cuando el usuario ya inicio sesion o se registro, es decir, cuando ya hay un usuario actual
    public static int mostrarMenuCliente (MonederoVirtual monederoVirtual){
        int opcion;
        try{
        opcion = Integer.valueOf(JOptionPane.showInputDialog(null,
                "Por favor seleccione una opcion :\n" +
                        "1. Realizar transferencia \n" +
                        "2. Realizar deposito\n \n"+
                        "3. Realizar retiro \n" +
                        "4. Programar transacción \n"+
                        "5. Consultar saldo\n" +
                        "6. Consultar puntos acumulados y rangos \n"+
                        "7. Canjear puntos\n \n"+
                        "8. Consultar historial transacciones\n" +
                        "9. Consular Notificaciones \n"+
                        "10.Consultar reporte gastos \n" +
                        "11.Crear otra monedero \n"+
                        "0. Salir"));
        }catch(NumberFormatException | NullPointerException e){
            opcion=-1;
        }
        return opcion;  
    }
    public static void  realizarTransferencia (MonederoVirtual monederoVirtual, LocalDate fecha, boolean programada, Frecuencia frecuencia){
        String codigo= JOptionPane.showInputDialog(null, "Ingrese el codigo del monedero desde el cual quiere hacer la transferencia:");
        Optional<Monedero> monederoUsuario= usuarioActual.buscarMonederoUsuario(codigo);
        if(monederoUsuario.isPresent()){
            try{
            Monedero monederoOrigen=monederoUsuario.get();
            double monto=Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese el monto de  la transferencia:"));
            String codigoDestinatario=JOptionPane.showInputDialog(null, "Ingrese el codigo del monedero del destinatario:");
            String resultado=monederoVirtual.transferir(usuarioActual,monederoOrigen, monto, codigoDestinatario, fecha, programada, frecuencia);
            JOptionPane.showMessageDialog(null, resultado); 
            }catch(NumberFormatException | NullPointerException e){
                JOptionPane.showMessageDialog(null, "Datos invalidos");
            }catch (MontoInvalidoException | SaldoInsuficienteException e) {
                JOptionPane.showMessageDialog(null, e.getMessage()); //Muestra cualquiera de las excepciones que encuentre
            }
        }else{  
           JOptionPane.showMessageDialog(null, "No existe un monedero con el codigo"+codigo); 
        }

    }
    
    public static void realizarDeposito( MonederoVirtual monederoVirtual, LocalDate fecha, boolean programada, Frecuencia frecuencia){
        String codigoMonedero= JOptionPane.showInputDialog(null, "Ingrese el codigo del monedero al cual le quiere hacer el deposito:");
        Optional<Monedero> monedero= monederoVirtual.buscarMonederoPorCodigo(codigoMonedero);
        if(monedero.isPresent()){
            try{
            String montoMensaje= JOptionPane.showInputDialog(null, "Ingrese el monto del deposito:");
            double montoDeposito= Double.parseDouble(montoMensaje);
            String resultado=monederoVirtual.depositar(usuarioActual, monedero.get(), montoDeposito, fecha, programada, frecuencia);
            JOptionPane.showMessageDialog(null, resultado );
            }catch(NumberFormatException | NullPointerException e){
                JOptionPane.showMessageDialog(null, "Datos invalidos");
            }catch (MontoInvalidoException e) {
                JOptionPane.showMessageDialog(null, e.getMessage()); 
            }
        }else{
              JOptionPane.showMessageDialog(null, "No existe un monedero con el codigo"+codigoMonedero); 
        }
    }
    public static void realizarRetiro(MonederoVirtual monederoVirtual){
        String codigoMonedero=JOptionPane.showInputDialog(null, "Ingrese el codigo del monedero del cual quiere hacer el retiro:");
        Optional<Monedero> monederoUsuario= usuarioActual.buscarMonederoUsuario(codigoMonedero);
        if(monederoUsuario.isPresent()){
            try{
            Monedero monedero= monederoUsuario.get();
            double montoRetiro= Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese el monto del retiro:"));
            String resultado=monederoVirtual.retirar(usuarioActual, monedero, montoRetiro);
            JOptionPane.showMessageDialog(null, resultado);
            }catch(NumberFormatException | NullPointerException e){
                JOptionPane.showMessageDialog(null, "Datos invalidos");
            }catch (MontoInvalidoException| SaldoInsuficienteException e) {
                JOptionPane.showMessageDialog(null, e.getMessage()); 
            }
        }else{
            JOptionPane.showMessageDialog(null, "No existe un monedero con el codigo"+codigoMonedero);
        }
    }

    public static void programarTransaccion(MonederoVirtual monederoVirtual){
            String fechaProgramada= JOptionPane.showInputDialog(null, "Ingrese la fecha programada de la transaccion (AAAA-MM-DD):");
            LocalDate fecha= LocalDate.parse(fechaProgramada);
            Frecuencia frecuenciaElegida = null;
            int frecuencia= JOptionPane.showConfirmDialog(null, "¿Desea programar una frecuencia para esta transacción?");
            if(frecuencia==JOptionPane.YES_OPTION){
                StringBuilder menuFrecuencias = new StringBuilder();
              Frecuencia[] frecuencias= Frecuencia.values();
              for (int i = 0; i < frecuencias.length; i++) {
            menuFrecuencias.append(i+1).append(".").append(frecuencias[i]).append("\n");
            }
             int opcionFrecuencia= Integer.parseInt(JOptionPane.showInputDialog(null, "¿Cada cuánto quiere programar esta transacción?\n"+
                "(Tenga en cuenta que la programación aplicará desde la fecha que ingresó anteriormente)\n"+ menuFrecuencias));
                LocalDate proximaFecha= null;
                 if (opcionFrecuencia < 1 || opcionFrecuencia > frecuencias.length) {
                 JOptionPane.showMessageDialog(null, "Frecuencia no válida");
                 return;
                 }
                if(opcionFrecuencia==1){
                    proximaFecha= fecha.plusDays(1);
                }else if(opcionFrecuencia==2){
                    proximaFecha= fecha.plusWeeks(1);
                }else if(opcionFrecuencia==3){
                    proximaFecha= fecha.plusMonths(1);  
                }
                JOptionPane.showMessageDialog(null, "La próxima fecha programada es: "+ proximaFecha);
                frecuenciaElegida = frecuencias[opcionFrecuencia-1];
            }
            int opcionTransaccion= Integer.valueOf(JOptionPane.showInputDialog(null,
                    "Por favor, seleccione el tipo de transacción que desea realizar :\n" +
                            "1. Programar transferencia \n" +
                            "2. Programar deposito\n \n")+
                            "0. Salir");
            do{
                 switch (opcionTransaccion){
                case 1:
                    realizarTransferencia(monederoVirtual, fecha, true, frecuenciaElegida);
                    break;
                case 2:
                    realizarDeposito(monederoVirtual, fecha, true, frecuenciaElegida);
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Adiós!!");
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida");  
            }  
            }while(opcionTransaccion!=0);
    }

    public static void consultarSaldo(MonederoVirtual monederoVirtual){
        String codigoMonedero= JOptionPane.showInputDialog(null, "Ingrese el codigo del monedero al cual le quiere consultar el saldo:");
        Optional<Monedero> monederoOptional= monederoVirtual.buscarMonederoPorCodigo(codigoMonedero);
        if(monederoOptional.isPresent()){
            Monedero monedero= monederoOptional.get();
            JOptionPane.showMessageDialog(null, "El saldo del monedero con codigo "+codigoMonedero+" es: "+monedero.getSaldo());
        }else{
            JOptionPane.showMessageDialog(null, "No existe un monedero con el codigo "+codigoMonedero);
        }
    }
    
    public static void consultarHistorialNotificaciones (MonederoVirtual monederoVirtual){
        String codigoMonedero= JOptionPane.showInputDialog(null, "Ingrese el codigo del monedero cuyas notificaciones quiere consultar:");
        String resultado= monederoVirtual.consultarNotificaciones(usuarioActual, codigoMonedero);
        //Esta función permite que el usuario haga scroll si hay muchas notificaciones, así no se sale el mensaje de la pantalla
        JTextArea areaTexto= new JTextArea(resultado, 15, 50);
        areaTexto.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(areaTexto), "Notificaciones", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void consultarHistorialTransacciones (MonederoVirtual monederoVirtual){
        String codigoMonedero= JOptionPane.showInputDialog(null, "Ingrese el codigo del monedero cuyas transacciones quiere consultar:");
        String resultado= monederoVirtual.consultarHistorialTransacciones(usuarioActual, codigoMonedero);
        //Esta función permite que el usuario haga scroll si hay muchas notificaciones, así no se sale el mensaje de la pantalla, otra vez
        JTextArea areaTexto= new JTextArea(resultado, 15, 50);
        areaTexto.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(areaTexto), "Transacciones", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void consultarpuntosYRango(MonederoVirtual monederoVirtual){
        String resultado= usuarioActual.ConsultarpuntosYRango();
        JOptionPane.showMessageDialog(null, resultado);

    }
    public static void canjearPuntos (MonederoVirtual monederoVirtual){
        //Si el menú queda dentro del try no va a correr porque está dentro de un bloque que no se ejecuta si hay una excepción, por eso lo puse afuera
        int opcion= Integer.parseInt(JOptionPane.showInputDialog(null,
            "Usted tiene: "+ usuarioActual.getPuntosAcumulados()+" puntos\n" +
            "ingrese el beneficio por el cual quiere canjear sus puntos:\n"+ 
            "1. 100 puntos:Reducción del 10% en la comisión por transferencias.\n" +
            "2. 500 puntos:Un mes sin cargos por retiros. \n"+
            "3. 1000 puntos:Bono de saldo de 50 unidades en el monedero." + "\n"+
            "0. Cancelar"));
        try{
        }catch(NumberFormatException | NullPointerException e){
            JOptionPane.showMessageDialog(null, "Datos invalidos");
        }
            if(opcion==0){
                return;
            }

            switch (opcion) {
                case 1:
                    try{
                    String resultado1=monederoVirtual.aplicarbeneficio100(usuarioActual);
                    JOptionPane.showMessageDialog(null, resultado1);
                     }catch(PuntosInsuficientesException e){
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                    break;
                case 2:
                    try{
                    String resultado2=monederoVirtual.aplicarbeneficio500(usuarioActual);
                    JOptionPane.showMessageDialog(null, resultado2);
                    }catch(PuntosInsuficientesException e){
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                    break;

                case 3:
                    String codigo= JOptionPane.showInputDialog(null,"ingrese el codigo del monedero al cual quiere aplicarle el beneficio");
                    Optional <Monedero> monederoUsuario= usuarioActual.buscarMonederoUsuario(codigo);
                    if(monederoUsuario.isEmpty()){
                    JOptionPane.showMessageDialog(null, "No existe un monedero con ese codigo");
                    return;
                    }
                    Monedero monedero= monederoUsuario.get();
                    try{
                    String resultado3=monederoVirtual.aplicarbeneficio1000(usuarioActual, monedero);
                    JOptionPane.showMessageDialog(null, resultado3);
                    }catch(PuntosInsuficientesException e){
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                    break;
            
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida");
                    break;
            }
    }

    public static void ConsultarReporteGastos(MonederoVirtual monederoVirtual){
        String resultado=usuarioActual.consultarReportesGastos();
        JOptionPane.showMessageDialog(null, resultado);
    }

    public static void crearOtroMonedero(MonederoVirtual monederoVirtual){
        String codigo;
        boolean existe;
        do{
        codigo = JOptionPane.showInputDialog(null, "Ingrese un código para su monedero:");
        existe = monederoVirtual.verificarCodigoMonederoExiste(codigo);
        if(existe){
            JOptionPane.showMessageDialog(null, "Ese código ya existe, elija otro");
        }
        }while(existe); //La misma logica que cuando  se creo el principal
        String nombre = JOptionPane.showInputDialog(null, "Ingrese un nombre para su monedero:");
        int ingresoTipo=Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el tipo de monedero que desea crear:\n"
            +"0. AHORROS\n"+
             "1. GASTOS_DIARIOS"));
        TipoMonedero tipoMonedero=TipoMonedero.values()[ingresoTipo];
        monederoVirtual.crearMonedero(usuarioActual, codigo, nombre.toUpperCase(), tipoMonedero, 0);
        JOptionPane.showMessageDialog(null, "Registro exitoso.");
    }

}