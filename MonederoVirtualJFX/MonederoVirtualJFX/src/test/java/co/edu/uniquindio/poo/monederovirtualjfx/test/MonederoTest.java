package co.edu.uniquindio.poo.monederovirtualjfx.test;

import co.edu.uniquindio.poo.monederovirtualjfx.model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Monedero.
 *
 * @author Mariana Suarez Bran y Laura Camila Bautista
 * @since 2026-11
 */

public class MonederoTest {

    private static final Logger LOG = Logger.getLogger(MonederoTest.class.getName());
    private Monedero monedero;
    private LocalDate fecha;

    @BeforeEach
    public void setUp() {
        LOG.info("Inicio de configuración de MonederoTest...");
        fecha = LocalDate.of(2026, 11, 2);
        // Se crea sin usuario para que no intente mostrar notificaciones con JOptionPane.
        monedero = new Monedero("M001", "Ahorros", 50000, TipoMonedero.AHORROS);
    }

    @AfterEach
    public void tearDown() {
        LOG.info("Fin de la prueba de Monedero...");
    }

    @Test
    @DisplayName("El constructor y los getters devuelven los datos correctos")
    public void constructorYGetters() {
        LOG.info("Inicio de prueba constructorYGetters");
        assertEquals("M001", monedero.getCodigo());
        assertEquals("Ahorros", monedero.getNombre());
        assertEquals(50000.0, monedero.getSaldo());
        assertEquals(TipoMonedero.AHORROS, monedero.getTipoMonedero());
        assertEquals(TipoMonedero.AHORROS, monedero.getTipo());
        LOG.info("Fin de prueba constructorYGetters");
    }

    @Test
    @DisplayName("setSaldo() cambia el saldo del monedero")
    public void setSaldoCambiaElSaldo() {
        LOG.info("Inicio de prueba setSaldoCambiaElSaldo...");
        monedero.setSaldo(75000);
        assertEquals(75000, monedero.getSaldo());
        LOG.info("Fin de prueba setSaldoCambiaElSaldo...");
    }

    @Test
    @DisplayName("aplicarbeneficio1000() suma 50 unidades al saldo")
    public void aplicarBeneficio1000SumaSaldo() {
        LOG.info("Inicio de prueba aplicarBeneficio1000SumaSaldo...");
        monedero.aplicarbeneficio1000();
        assertEquals(50050.0, monedero.getSaldo());
        LOG.info("Fin de prueba aplicarBeneficio1000SumaSaldo...");
    }

    @Test
    @DisplayName("agregarTransaccion() guarda la transacción en la lista")
    public void agregarTransaccion() {
        LOG.info("Inicio de prueba agregarTransaccion");
        Deposito deposito = new Deposito("T1", fecha, 10000);
        monedero.agregarTransaccion(deposito);
        assertEquals(1, monedero.getListaTransacciones().size());
        assertEquals("T1", monedero.getListaTransacciones().get(0).getCodigo());
        LOG.info("Fin de prueba agregarTransaccion");
    }

    @Test
    @DisplayName("agregarTransaccionProgramada() guarda la transacción programada")
    public void agregarTransaccionProgramada() {
        LOG.info("Inicio de prueba agregarTransaccionProgramada...");
        Deposito deposito = new Deposito("T1", fecha, 10000);
        TransaccionProgramada programada = new TransaccionProgramada("TP1", fecha, Frecuencia.DIARIA, deposito, monedero);
        monedero.agregarTransaccionProgramada(programada);
        assertEquals(1, monedero.getListaTransaccionesProgramadas().size());
        assertEquals("TP1", monedero.getListaTransaccionesProgramadas().get(0).getCodigo());
        LOG.info("Fin de prueba agregarTransaccionProgramada...");
    }

}
