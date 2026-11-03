package co.edu.uniquindio.poo.monederovirtualjfx.test;

import co.edu.uniquindio.poo.monederovirtualjfx.model.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Notificacion y sus subclases.
 * No se invoca enviar() porque muestra ventanas de JOptionPane.
 *
 * @author Mariana Suarez Bran y Laura Camila Bautista
 * @since 2026-11
 */

public class NotificacionTest {

    private static final Logger LOG = Logger.getLogger(NotificacionTest.class.getName());

    @Test
    @DisplayName("Una notificación de correo conserva sus datos")
    public void notificacionCorreoConservaDatos() {
        LOG.info("Inicio de prueba notificacionCorreoConservaDatos...");
        LocalDate fecha = LocalDate.of(2026, 11, 2);
        Notificacion notificacion = new NotificacionCorreo("Saldo bajo", fecha, TipoAlerta.SALDO_BAJO, "ana@correo.com");
        assertEquals("Saldo bajo", notificacion.getMensaje());
        assertEquals(fecha, notificacion.getFechaNotificacion());
        assertEquals(TipoAlerta.SALDO_BAJO, notificacion.getTipoAlerta());
        assertEquals("ana@correo.com", notificacion.getDestinatario());
        LOG.info("Fin de prueba notificacionCorreoConservaDatos...");
    }

    @Test
    @DisplayName("Una notificación de SMS conserva sus datos")
    public void notificacionSmsConservaDatos() {
        LOG.info("Inicio de prueba notificacionSmsConservaDatos...");
        LocalDate fecha = LocalDate.of(2026, 11, 2);
        Notificacion notificacion = new NotificacionSMS("Recordatorio", fecha, TipoAlerta.TRANSACCION_PROGRAMADA, "3001234567");
        assertEquals("Recordatorio", notificacion.getMensaje());
        assertEquals(TipoAlerta.TRANSACCION_PROGRAMADA, notificacion.getTipoAlerta());
        assertEquals("3001234567", notificacion.getDestinatario());
        LOG.info("Fin de prueba notificacionSmsConservaDatos...");
    }
}