package co.edu.uniquindio.poo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Deposito.
 *
 * @author Mariana Suarez Bran y Laura Camila Bautista
 * @since 2026-11
 */

public class DepositoTest {

    private static final Logger LOG = Logger.getLogger(DepositoTest.class.getName());
    private Monedero monedero;
    private LocalDate fecha;

    @BeforeEach
    public void setUp() {
        LOG.info("Inicio de configuración de DepositoTest");
        fecha = LocalDate.of(2026, 11, 2);
        monedero = new Monedero("M001", "Ahorros", 50000, TipoMonedero.AHORROS);
    }

    @AfterEach
    public void tearDown() {
        LOG.info("Fin de la prueba de Deposito");
    }

    @Test
    @DisplayName("aplicar() aumenta el saldo del monedero")
    public void aplicarAumentaSaldo() {
        LOG.info("Inicio de prueba aplicarAumentaSaldo");
        Deposito deposito = new Deposito("T1", fecha, 25000);
        deposito.aplicar(monedero);
        assertEquals(75000, monedero.getSaldo());
        LOG.info("Fin de prueba aplicarAumentaSaldo");
    }

    @Test
    @DisplayName("calcularPuntos() convierte el monto en puntos enteros")
    public void calcularPuntos() {
        LOG.info("Inicio de prueba calcularPuntos");
        Deposito deposito = new Deposito("T2", fecha, 25000);
        assertEquals(250, deposito.calcularPuntos());
        LOG.info("Fin de prueba calcularPuntos");
    }

    @Test
    @DisplayName("calcularPuntos() no genera puntos negativos")
    public void calcularPuntosConMontoPequeno() {
        LOG.info("Inicio de prueba calcularPuntosConMontoPequeno...");
        Deposito deposito = new Deposito("T3", fecha, 99);
        assertEquals(0, deposito.calcularPuntos());
        LOG.info("Fin de prueba calcularPuntosConMontoPequeno...");
    }

    @Test
    @DisplayName("El depósito conserva sus datos y queda en estado COMPLETADA")
    public void conservaDatosDeLaTransaccion() {
        LOG.info("Inicio de prueba conservaDatosDeLaTransaccion...");
        Deposito deposito = new Deposito("T4", fecha, 12000);
        assertEquals("T4", deposito.getCodigo());
        assertEquals(fecha, deposito.getFecha());
        assertEquals(12000, deposito.getMonto());
        assertEquals(EstadoTransaccion.COMPLETADA, deposito.getEstado());
        assertNotNull(deposito.getFecha());
        LOG.info("Fin de prueba conservaDatosDeLaTransaccion...");
    }
}
