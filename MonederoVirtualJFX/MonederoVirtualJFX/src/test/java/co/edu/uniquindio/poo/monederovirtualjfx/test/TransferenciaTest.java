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
 * Pruebas unitarias para la clase Transferencia.
 *
 * @author Mariana Suarez Bran y Laura Camila Bautista
 * @since 2026-11
 */

public class TransferenciaTest {

    private static final Logger LOG = Logger.getLogger(TransferenciaTest.class.getName());

    private Monedero monedero;
    private LocalDate fecha;

    @BeforeEach
    public void setUp() {
        LOG.info("Inicio de configuración de TransferenciaTest...");
        fecha = LocalDate.of(2026, 11, 2);
        monedero = new Monedero("M001", "Ahorros", 100000, TipoMonedero.AHORROS);
    }

    @AfterEach
    public void tearDown() {
        LOG.info("Fin de la prueba de Transferencia...");
    }

    @Test
    @DisplayName("aplicarComision() cobra el 1% sin descuento")
    public void calcularComisionSinDescuento() {
        LOG.info("Inicio de prueba calcularComisionSinDescuento...");
        Transferencia transferencia = new Transferencia("T1", fecha, 1000, "M002", false);

        assertEquals(10.0, transferencia.aplicarComision(), 0.001);
        LOG.info("Fin de prueba calcularComisionSinDescuento...");
    }

    @Test
    @DisplayName("aplicarComision() cobra el 1% menos el 10% de descuento")
    public void calcularComisionConDescuento() {
        LOG.info("Inicio de prueba calcularComisionConDescuento...");
        Transferencia transferencia = new Transferencia("T2", fecha, 1000, "M002", true);

        assertEquals(9.0, transferencia.aplicarComision(), 0.001);
        LOG.info("Fin de prueba calcularComisionConDescuento...");
    }

    @Test
    @DisplayName("aplicar() descuenta el monto y la comisión del monedero")
    public void aplicarDescuentaMontoYComision() {
        LOG.info("Inicio de prueba aplicarDescuentaMontoYComision...");
        Transferencia transferencia = new Transferencia("T3", fecha, 1000, "M002", false);
        transferencia.aplicar(monedero);
        // 100000 - 1000 (monto) - 10 (comisión) = 98990
        assertEquals(98990.0, monedero.getSaldo(), 0.001);
        LOG.info("Fin de prueba aplicarDescuentaMontoYComision...");
    }

    @Test
    @DisplayName("aplicar() usa la comisión con descuento cuando aplica el beneficio")
    public void aplicarConDescuento() {
        LOG.info("Inicio de prueba aplicarConDescuento...");
        Transferencia transferencia = new Transferencia("T4", fecha, 1000, "M002", true);
        transferencia.aplicar(monedero);
        // 100000 - 1000 (monto) - 9 (comisión con descuento) = 98991
        assertEquals(98991.0, monedero.getSaldo(), 0.001);
        LOG.info("Fin de prueba aplicarConDescuento...");
    }

    @Test
    @DisplayName("calcularPuntos() entrega 3 puntos por cada 100 unidades")
    public void calcularPuntos() {
        LOG.info("Inicio de prueba calcularPuntos de Transferencia...");
        Transferencia transferencia = new Transferencia("T5", fecha, 1000, "M002", false);
        assertEquals(30, transferencia.calcularPuntos());
        LOG.info("Fin de prueba calcularPuntos de Transferencia...");
    }

    @Test
    @DisplayName("La transferencia conserva el código del monedero destino")
    public void conservaDestino() {
        LOG.info("Inicio de prueba conservaDestino...");
        Transferencia transferencia = new Transferencia("T6", fecha, 5000, "M002", false);
        assertEquals("M002", transferencia.getDestino());
        assertEquals(EstadoTransaccion.COMPLETADA, transferencia.getEstado());
        LOG.info("Fin de prueba conservaDestino...");
    }
}

