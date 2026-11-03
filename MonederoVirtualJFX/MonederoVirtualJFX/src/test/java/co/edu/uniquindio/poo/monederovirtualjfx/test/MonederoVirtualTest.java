package co.edu.uniquindio.poo.monederovirtualjfx.test;

import co.edu.uniquindio.poo.monederovirtualjfx.model.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase MonederoVirtual.
 * Aquí se prueban los flujos completos: usuarios, monederos, depósitos,
 * retiros, transferencias, beneficios e historial.
 *
 * @author Mariana Suarez Bran y Laura Camila Bautista
 * @since 2026-11
 */

public class MonederoVirtualTest {

    private static final Logger LOG = Logger.getLogger(MonederoVirtualTest.class.getName());

    private MonederoVirtual monederoVirtual;
    private Usuario usuario;
    private Monedero monedero;
    private LocalDate hoy;

    @BeforeEach
    public void setUp() {
        LOG.info("Inicio de configuración de MonederoVirtualTest...");
        monederoVirtual = new MonederoVirtual("Monedero Virtual UQ", "900123456");
        usuario = new Usuario("1001", "Ana Perez", "3001234567", "ana@correo.com", "clave123");
        monederoVirtual.crearUsuario(usuario);
        // Saldo alto para que getSaldo() no dispare notificaciones con JOptionPane.
        monedero = monederoVirtual.crearMonedero(usuario, "M001", "PRINCIPAL", TipoMonedero.AHORROS, 100000);
        hoy = LocalDate.now();
    }

    @AfterEach
    public void tearDown() {
        LOG.info("Fin de la prueba de MonederoVirtual...");
    }

    private Monedero crearMonederoDestino() {
        Usuario otroUsuario = new Usuario("2002", "Luis Gomez", "3007654321", "luis@correo.com", "clave456");
        monederoVirtual.crearUsuario(otroUsuario);
        return monederoVirtual.crearMonedero(otroUsuario, "M002", "DESTINO", TipoMonedero.GASTOS_DIARIOS, 50000);
    }


    // Usuarios
    // ------------------------------------------------------------------

    @Test
    @DisplayName("crearUsuario() registra un usuario nuevo")
    public void crearUsuarioNuevo() {
        LOG.info("Inicio de prueba crearUsuarioNuevo...");
        Usuario nuevo = new Usuario("2002", "Luis Gomez", "3007654321", "luis@correo.com", "clave456");
        String resultado = monederoVirtual.crearUsuario(nuevo);
        assertEquals("Cliente registrado con exito", resultado);
        assertEquals(2, monederoVirtual.getListaClientes().size());
        assertTrue(monederoVirtual.buscarUsuarioDocumentoIdentidad("2002").isPresent());
        LOG.info("Fin de prueba crearUsuarioNuevo...");
    }

    @Test
    @DisplayName("crearUsuario() rechaza un documento de identidad duplicado")
    public void crearUsuarioDuplicado() {
        LOG.info("Inicio de prueba crearUsuarioDuplicado...");
        Usuario duplicado = new Usuario("1001", "Otra Ana", "3000000000", "otra@correo.com", "otraClave");
        String resultado = monederoVirtual.crearUsuario(duplicado);
        assertEquals("Ya hay un cliente con esa información, por favor verifique el documento de identidad", resultado);
        assertEquals(1, monederoVirtual.getListaClientes().size());
        LOG.info("Fin de prueba crearUsuarioDuplicado...");
    }

    @Test
    @DisplayName("buscarUsuarioDocumentoIdentidad() devuelve vacío si no existe")
    public void buscarUsuarioInexistente() {
        LOG.info("Inicio de prueba buscarUsuarioInexistente...");
        assertTrue(monederoVirtual.buscarUsuarioDocumentoIdentidad("9999").isEmpty());
        LOG.info("Fin de prueba buscarUsuarioInexistente...");
    }
    // Monederos
    // ------------------------------------------------------------------

    @Test
    @DisplayName("crearMonedero() agrega el monedero al usuario y a la lista global")
    public void crearMonederoAgregaEnAmbasListas() {
        LOG.info("Inicio de prueba crearMonederoAgregaEnAmbasListas...");
        assertTrue(monederoVirtual.verificarCodigoMonederoExiste("M001"));
        assertEquals(1, usuario.getListaMonederos().size());
        assertEquals(usuario, monedero.getUsuario());
        LOG.info("Fin de prueba crearMonederoAgregaEnAmbasListas...");
    }

    @Test
    @DisplayName("buscarMonederoPorCodigo() encuentra un monedero existente")
    public void buscarMonederoExistente() {
        LOG.info("Inicio de prueba buscarMonederoExistente...");
        assertTrue(monederoVirtual.buscarMonederoPorCodigo("M001").isPresent());
        assertEquals("M001", monederoVirtual.buscarMonederoPorCodigo("M001").get().getCodigo());
        LOG.info("Fin de prueba buscarMonederoExistente...");
    }

    @Test
    @DisplayName("buscarMonederoPorCodigo() devuelve vacío si el código no existe")
    public void buscarMonederoInexistente() {
        LOG.info("Inicio de prueba buscarMonederoInexistente...");
        assertTrue(monederoVirtual.buscarMonederoPorCodigo("M999").isEmpty());
        LOG.info("Fin de prueba buscarMonederoInexistente...");
    }

    @Test
    @DisplayName("verificarCodigoMonederoExiste() devuelve false para un código nuevo")
    public void verificarCodigoNuevo() {
        LOG.info("Inicio de prueba verificarCodigoNuevo...");
        assertFalse(monederoVirtual.verificarCodigoMonederoExiste("M999"));
        LOG.info("Fin de prueba verificarCodigoNuevo...");
    }

    // Depósitos
    // ------------------------------------------------------------------

    @Test
    @DisplayName("depositar() aumenta el saldo, registra la transacción y suma puntos")
    public void depositarAumentaSaldoYRegistra() throws MontoInvalidoException {
        LOG.info("Inicio de prueba depositarAumentaSaldoYRegistra...");
        String resultado = monederoVirtual.depositar(usuario, monedero, 25000, hoy, false, null);
        assertTrue(resultado.contains("éxito"));
        assertEquals(125000.0, monedero.getSaldo(), 0.001);
        assertEquals(1, monedero.getListaTransacciones().size());
        assertTrue(monedero.getListaTransacciones().get(0) instanceof Deposito);
        assertEquals(250, usuario.getPuntosAcumulados());
        LOG.info("Fin de prueba depositarAumentaSaldoYRegistra...");
    }

    @Test
    @DisplayName("depositar() rechaza un monto negativo y no altera el saldo")
    public void depositarMontoNegativo() {
        LOG.info("Inicio de prueba depositarMontoNegativo...");
        assertThrows(MontoInvalidoException.class,
                () -> monederoVirtual.depositar(usuario, monedero, -5000, hoy, false, null));
        assertEquals(100000.0, monedero.getSaldo(), 0.001);
        assertTrue(monedero.getListaTransacciones().isEmpty());
        LOG.info("Fin de prueba depositarMontoNegativo...");
    }

    @Test
    @DisplayName("depositar() programado no modifica el saldo y queda PENDIENTE")
    public void depositarProgramadoNoAplicaSaldo() throws MontoInvalidoException {
        LOG.info("Inicio de prueba depositarProgramadoNoAplicaSaldo...");
        String resultado = monederoVirtual.depositar(usuario, monedero, 10000, hoy.plusDays(2), true, Frecuencia.DIARIA);
        assertTrue(resultado.contains("programó"));
        assertEquals(100000.0, monedero.getSaldo(), 0.001);
        assertTrue(monedero.getListaTransacciones().isEmpty());
        assertEquals(1, monedero.getListaTransaccionesProgramadas().size());
        assertEquals(EstadoTransaccion.PENDIENTE, monedero.getListaTransaccionesProgramadas().get(0).getEstado());
        LOG.info("Fin de prueba depositarProgramadoNoAplicaSaldo...");
    }
    // Retiros
    // ------------------------------------------------------------------

    @Test
    @DisplayName("retirar() disminuye el saldo, registra la transacción y suma puntos")
    public void retirarDisminuyeSaldoYRegistra() throws MontoInvalidoException, SaldoInsuficienteException {
        LOG.info("Inicio de prueba retirarDisminuyeSaldoYRegistra...");
        String resultado = monederoVirtual.retirar(usuario, monedero, 10000);
        assertTrue(resultado.contains("éxito"));
        assertEquals(90000.0, monedero.getSaldo(), 0.001);
        assertEquals(1, monedero.getListaTransacciones().size());
        assertTrue(monedero.getListaTransacciones().get(0) instanceof Retiro);
        assertEquals(200, usuario.getPuntosAcumulados());
        LOG.info("Fin de prueba retirarDisminuyeSaldoYRegistra...");
    }

    @Test
    @DisplayName("retirar() rechaza un monto negativo y no altera el saldo")
    public void retirarMontoNegativo() {
        LOG.info("Inicio de prueba retirarMontoNegativo...");
        assertThrows(MontoInvalidoException.class,
                () -> monederoVirtual.retirar(usuario, monedero, -1000));
        assertEquals(100000.0, monedero.getSaldo(), 0.001);
        assertTrue(monedero.getListaTransacciones().isEmpty());
        LOG.info("Fin de prueba retirarMontoNegativo...");
    }

    // Transferencias
    // ------------------------------------------------------------------

    @Test
    @DisplayName("transferir() resta al origen, suma al destino y registra ambas transacciones")
    public void transferirExitosa() throws MontoInvalidoException, SaldoInsuficienteException {
        LOG.info("Inicio de prueba transferirExitosa...");
        Monedero destino = crearMonederoDestino();
        String resultado = monederoVirtual.transferir(usuario, monedero, 10000, "M002", hoy, false, null);
        assertTrue(resultado.contains("La transferencia se realizo con exito"));
        // Origen: 100000 - 10000 - 100 (comisión 1%) = 89900
        assertEquals(89900.0, monedero.getSaldo(), 0.001);
        // Destino: 50000 + 10000 = 60000
        assertEquals(60000.0, destino.getSaldo(), 0.001);
        assertEquals(1, monedero.getListaTransacciones().size());
        assertEquals(1, destino.getListaTransacciones().size());
        assertTrue(monedero.getListaTransacciones().get(0) instanceof Transferencia);
        assertTrue(destino.getListaTransacciones().get(0) instanceof Deposito);
        assertEquals(300, usuario.getPuntosAcumulados());
        LOG.info("Fin de prueba transferirExitosa...");
    }

    @Test
    @DisplayName("transferir() rechaza un monto negativo y no mueve saldos")
    public void transferirMontoNegativo() {
        LOG.info("Inicio de prueba transferirMontoNegativo...");
        Monedero destino = crearMonederoDestino();

        assertThrows(MontoInvalidoException.class,
                () -> monederoVirtual.transferir(usuario, monedero, -1000, "M002", hoy, false, null));

        assertEquals(100000.0, monedero.getSaldo(), 0.001);
        assertEquals(50000.0, destino.getSaldo(), 0.001);
        LOG.info("Fin de prueba transferirMontoNegativo...");
    }

    @Test
    @DisplayName("transferir() impide una transferencia sin saldo suficiente")
    public void transferirSaldoInsuficiente() {
        LOG.info("Inicio de prueba transferirSaldoInsuficiente...");
        Monedero destino = crearMonederoDestino();
        SaldoInsuficienteException excepcion = assertThrows(SaldoInsuficienteException.class,
                () -> monederoVirtual.transferir(usuario, monedero, 200000, "M002", hoy, false, null));
        assertEquals("Saldo insuficiente para realizar la transferencia", excepcion.getMessage());
        assertEquals(100000.0, monedero.getSaldo(), 0.001);
        assertEquals(50000.0, destino.getSaldo(), 0.001);
        assertTrue(monedero.getListaTransacciones().isEmpty());
        assertTrue(destino.getListaTransacciones().isEmpty());
        LOG.info("Fin de prueba transferirSaldoInsuficiente...");
    }

    @Test
    @DisplayName("transferir() informa cuando el monedero destino no existe")
    public void transferirDestinoInexistente() throws MontoInvalidoException, SaldoInsuficienteException {
        LOG.info("Inicio de prueba transferirDestinoInexistente...");
        String resultado = monederoVirtual.transferir(usuario, monedero, 1000, "M999", hoy, false, null);
        assertEquals("No existe un monedero con ese código", resultado);
        assertEquals(100000.0, monedero.getSaldo(), 0.001);
        assertTrue(monedero.getListaTransacciones().isEmpty());
        LOG.info("Fin de prueba transferirDestinoInexistente...");
    }

    @Test
    @DisplayName("transferir() programado no mueve saldos y queda PENDIENTE")
    public void transferirProgramada() throws MontoInvalidoException, SaldoInsuficienteException {
        LOG.info("Inicio de prueba transferirProgramada...");
        Monedero destino = crearMonederoDestino();
        String resultado = monederoVirtual.transferir(usuario, monedero, 10000, "M002", hoy.plusDays(1), true, Frecuencia.SEMANAL);
        assertTrue(resultado.contains("programó"));
        assertEquals(100000.0, monedero.getSaldo(), 0.001);
        assertEquals(50000.0, destino.getSaldo(), 0.001);
        assertTrue(monedero.getListaTransacciones().isEmpty());
        assertEquals(1, monedero.getListaTransaccionesProgramadas().size());
        assertEquals(EstadoTransaccion.PENDIENTE, monedero.getListaTransaccionesProgramadas().get(0).getEstado());
        LOG.info("Fin de prueba transferirProgramada...");
    }
    // Códigos y fechas
    // ------------------------------------------------------------------

    @Test
    @DisplayName("generarCodigoTransaccion() genera códigos consecutivos")
    public void generarCodigoTransaccion() {
        LOG.info("Inicio de prueba generarCodigoTransaccion...");
        assertEquals("T1", monederoVirtual.generarCodigoTransaccion());
        assertEquals("T2", monederoVirtual.generarCodigoTransaccion());
        assertEquals("T3", monederoVirtual.generarCodigoTransaccion());
        LOG.info("Fin de prueba generarCodigoTransaccion...");
    }

    // Historial y notificaciones
    // ------------------------------------------------------------------

    @Test
    @DisplayName("consultarHistorialTransacciones() informa si el monedero no existe")
    public void historialMonederoInexistente() {
        LOG.info("Inicio de prueba historialMonederoInexistente...");
        assertEquals("No existe un monedero con ese código",
                monederoVirtual.consultarHistorialTransacciones(usuario, "M999"));
        LOG.info("Fin de prueba historialMonederoInexistente...");
    }

    @Test
    @DisplayName("consultarHistorialTransacciones() valida que el monedero sea del usuario")
    public void historialOtroUsuario() {
        LOG.info("Inicio de prueba historialOtroUsuario...");
        crearMonederoDestino();
        assertEquals("El monedero M002 no pertenece a este usuario",
                monederoVirtual.consultarHistorialTransacciones(usuario, "M002"));
        LOG.info("Fin de prueba historialOtroUsuario...");
    }

    @Test
    @DisplayName("consultarHistorialTransacciones() informa si no hay transacciones")
    public void historialVacio() {
        LOG.info("Inicio de prueba historialVacio...");
        assertEquals("No hay transacciones para este usuario",
                monederoVirtual.consultarHistorialTransacciones(usuario, "M001"));
        LOG.info("Fin de prueba historialVacio...");
    }

    @Test
    @DisplayName("consultarHistorialTransacciones() muestra los datos de las transacciones")
    public void historialConTransacciones() throws MontoInvalidoException {
        LOG.info("Inicio de prueba historialConTransacciones...");
        monederoVirtual.depositar(usuario, monedero, 25000, hoy, false, null);
        String resultado = monederoVirtual.consultarHistorialTransacciones(usuario, "M001");
        assertTrue(resultado.contains("Código de la transacción: T1"));
        assertTrue(resultado.contains("Estado: COMPLETADA"));
        assertTrue(resultado.contains("Monto: 25000.0"));
        assertTrue(resultado.contains("Tipo de transacción: Deposito"));
        LOG.info("Fin de prueba historialConTransacciones...");
    }

    @Test
    @DisplayName("consultarNotificaciones() informa si el monedero no existe")
    public void notificacionesMonederoInexistente() {
        LOG.info("Inicio de prueba notificacionesMonederoInexistente...");
        assertEquals("No existe un monedero con ese código",
                monederoVirtual.consultarNotificaciones(usuario, "M999"));
        LOG.info("Fin de prueba notificacionesMonederoInexistente...");
    }

    @Test
    @DisplayName("consultarNotificaciones() informa si no hay notificaciones")
    public void notificacionesVacias() {
        LOG.info("Inicio de prueba notificacionesVacias...");
        assertEquals("No hay notificaciones para este usuario",
                monederoVirtual.consultarNotificaciones(usuario, "M001"));
        LOG.info("Fin de prueba notificacionesVacias...");
    }

    // Transacciones programadas
    // ------------------------------------------------------------------

    @Test
    @DisplayName("ejecutarTransaccionesProgramadas() aplica un depósito pendiente de hoy")
    public void ejecutarDepositoProgramadoDeHoy() throws MontoInvalidoException {
        LOG.info("Inicio de prueba ejecutarDepositoProgramadoDeHoy...");
        monederoVirtual.depositar(usuario, monedero, 5000, LocalDate.now(), true, null);
        monederoVirtual.ejecutarTransaccionesProgramadas();
        assertEquals(105000.0, monedero.getSaldo(), 0.001);
        assertEquals(EstadoTransaccion.COMPLETADA,
                monedero.getListaTransaccionesProgramadas().get(0).getEstado());
        LOG.info("Fin de prueba ejecutarDepositoProgramadoDeHoy...");
    }
}
