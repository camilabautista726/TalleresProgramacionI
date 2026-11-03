package co.edu.uniquindio.poo.monederovirtualjfx.test;

import co.edu.uniquindio.poo.monederovirtualjfx.model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase TransaccionProgramada.
 *
 * @author Mariana Suarez Bran y Laura Camila Bautista
 * @since 2026-11
 */

public class TransaccionProgramadaTest {

    private static final Logger LOG = Logger.getLogger(TransaccionProgramadaTest.class.getName());

    private Monedero monedero;
    private Transaccion deposito;
    private LocalDate fecha;
    private TransaccionProgramada programada;

    @BeforeEach
    public void setUp() {
        LOG.info("Inicio de configuración de TransaccionProgramadaTest...");
        fecha = LocalDate.of(2026, 11, 2);
        monedero = new Monedero("M001", "Ahorros", 50000, TipoMonedero.AHORROS);
        deposito = new Deposito("T1", fecha, 10000);
        programada = new TransaccionProgramada("TP1", fecha, Frecuencia.DIARIA, deposito, monedero);
    }

    @Test
    @DisplayName("Una transacción programada nueva queda PENDIENTE")
    public void estadoInicialPendiente() {
        LOG.info("Inicio de prueba estadoInicialPendiente...");
        assertEquals(EstadoTransaccion.PENDIENTE, programada.getEstado());
        LOG.info("Fin de prueba estadoInicialPendiente...");
    }

    @Test
    @DisplayName("Los datos de la transacción programada se conservan")
    public void conservaDatos() {
        LOG.info("Inicio de prueba conservaDatos de TransaccionProgramada...");
        assertEquals("TP1", programada.getCodigo());
        assertEquals(fecha, programada.getFechaEjecucion());
        assertEquals(Frecuencia.DIARIA, programada.getFrencuenciaProgramada());
        assertEquals("T1", programada.getTransaccion().getCodigo());
        assertEquals("M001", programada.getMonedero().getCodigo());
        assertNotNull(programada.getTransaccion());
        LOG.info("Fin de prueba conservaDatos de TransaccionProgramada...");
    }

    @Test
    @DisplayName("Los setters actualizan fecha, frecuencia y estado")
    public void settersActualizanDatos() {
        LOG.info("Inicio de prueba settersActualizanDatos...");
        LocalDate nuevaFecha = fecha.plusMonths(1);

        programada.setFechaEjecucion(nuevaFecha);
        programada.setFrencuenciaProgramada(Frecuencia.MENSUAL);
        programada.setEstado(EstadoTransaccion.COMPLETADA);

        assertEquals(nuevaFecha, programada.getFechaEjecucion());
        assertEquals(Frecuencia.MENSUAL, programada.getFrencuenciaProgramada());
        assertEquals(EstadoTransaccion.COMPLETADA, programada.getEstado());
        LOG.info("Fin de prueba settersActualizanDatos...");
    }
}
