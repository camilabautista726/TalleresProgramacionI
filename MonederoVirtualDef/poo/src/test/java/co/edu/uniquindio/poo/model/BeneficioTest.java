package co.edu.uniquindio.poo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Beneficio.
 *
 * @author Mariana Suarez Bran y Laura Camila Bautista
 * @since 2026-11
 */

public class BeneficioTest {

    private static final Logger LOG = Logger.getLogger(BeneficioTest.class.getName());

    @Test
    @DisplayName("El beneficio conserva nombre, puntos y descripción")
    public void conservaDatos() {
        LOG.info("Inicio de prueba conservaDatos de Beneficio");
        Beneficio beneficio = new Beneficio("Descuento", 100, "10% menos en comisión");
        assertEquals("Descuento", beneficio.getNombre());
        assertEquals(100, beneficio.getPuntosRequeridos());
        assertEquals("10% menos en comisión", beneficio.getDescripcion());
        LOG.info("Fin de prueba conservaDatos de Beneficio");
    }
}
