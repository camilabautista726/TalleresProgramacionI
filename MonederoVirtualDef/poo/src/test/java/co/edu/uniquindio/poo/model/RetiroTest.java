package co.edu.uniquindio.poo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Retiro.
 *
 * @author Mariana Suarez Bran y Laura Camila Bautista
 * @since 2026-11
 */

public class RetiroTest {

    private static final Logger LOG = Logger.getLogger(RetiroTest.class.getName());

    private Monedero monedero;
    private LocalDate fecha;

    @BeforeEach
    public void setUp() {
        LOG.info("Inicio de configuración de RetiroTest...");
        fecha = LocalDate.of(2026, 11, 2);
        monedero = new Monedero("M001", "Ahorros", 50000, TipoMonedero.AHORROS);
    }

    @AfterEach
    public void tearDown() {
        LOG.info("Fin de la prueba de Retiro...");
    }

    @Test
    @DisplayName("aplicar() disminuye el saldo del monedero")
    public void aplicarDisminuyeSaldo() {
        LOG.info("Inicio de prueba aplicarDisminuyeSaldo...");
        Retiro retiro = new Retiro("T1", fecha, 10000, false);

        retiro.aplicar(monedero);

        assertEquals(40000.0, monedero.getSaldo(), 0.001);
        LOG.info("Fin de prueba aplicarDisminuyeSaldo...");
    }

    @Test
    @DisplayName("calcularCargo() cobra el 1% cuando no hay beneficio")
    public void calcularCargoConCargo() {
        LOG.info("Inicio de prueba calcularCargoConCargo...");
        Retiro retiro = new Retiro("T2", fecha, 10000, false);

        assertEquals(100.0, retiro.calcularCargo(), 0.001);
        LOG.info("Fin de prueba calcularCargoConCargo...");
    }

    @Test
    @DisplayName("calcularCargo() devuelve 0 cuando el retiro es sin cargo")
    public void calcularCargoSinCargo() {
        LOG.info("Inicio de prueba calcularCargoSinCargo...");
        Retiro retiro = new Retiro("T3", fecha, 10000, true);

        assertEquals(0.0, retiro.calcularCargo(), 0.001);
        LOG.info("Fin de prueba calcularCargoSinCargo...");
    }

    @Test
    @DisplayName("calcularCargo() calcula el 1% de un monto decimal")
    public void calcularCargoConMontoDecimal() {
        LOG.info("Inicio de prueba calcularCargoConMontoDecimal...");
        Retiro retiro = new Retiro("T4", fecha, 1234, false);

        assertEquals(12.34, retiro.calcularCargo(), 0.001);
        LOG.info("Fin de prueba calcularCargoConMontoDecimal...");
    }

    @Test
    @DisplayName("calcularPuntos() entrega 2 puntos por cada 100 unidades")
    public void calcularPuntos() {
        LOG.info("Inicio de prueba calcularPuntos de Retiro...");
        Retiro retiro = new Retiro("T5", fecha, 10000, false);

        assertEquals(200, retiro.calcularPuntos());
        LOG.info("Fin de prueba calcularPuntos de Retiro...");
    }
}
