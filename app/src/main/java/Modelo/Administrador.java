package Modelo;

import java.io.Serializable;

/**
 * Representa a un usuario con privilegios de administrador en el sistema.
 * El administrador puede gestionar partidos, resultados y puntajes.
 */
public class Administrador extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Cargo específico del administrador dentro de la organización. */
    private String cargo;

    /**
     * Constructor para la clase Administrador.
     *
     * @param idUsuario      Identificador único del usuario.
     * @param nombreUsuario  Nombre de usuario para el inicio de sesión.
     * @param contrasena     Contraseña del usuario.
     * @param nombreCompleto Nombre completo de la persona.
     * @param tipoUsuario    Tipo de usuario (Administrador).
     * @param cargo          Cargo específico que ocupa el administrador.
     */
    public Administrador(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario, String cargo) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.cargo = cargo;
    }

    /**
     * Cierra los pronósticos para un partido específico, impidiendo más modificaciones de los participantes.
     *
     * @param partido El partido cuyos pronósticos se desean cerrar.
     */
    public void cerrarPronosticos(Partido partido) {
        if (partido != null) {
            // Se usa el setter correcto que definimos en la clase Partido
            partido.setEstado(EstadoPartido.CERRADO);
        }
    }

    /**
     * Registra el resultado oficial de un partido y cambia su estado a finalizado.
     *
     * @param partido El partido al que se le registrará el resultado.
     * @param goles1  Goles anotados por el equipo 1.
     * @param goles2  Goles anotados por el equipo 2.
     */
    public void registrarResultadoOficial(Partido partido, int goles1, int goles2) {
        if (partido != null) {
            // Se usan los setters correctos para los goles
            partido.setGoles1(goles1);
            partido.setGoles2(goles2);
            partido.setEstado(EstadoPartido.FINALIZADO);
        }
    }

    /**
     * Método invocado para recalcular los puntajes generales de todos los participantes.
     */
    public void actualizarPuntajesGeneral() {
        // Método invocado desde el Activity para recalcular los puntos
    }

    /**
     * Obtiene el cargo del administrador.
     * @return Cargo actual.
     */
    public String getCargo() { return cargo; }
    /**
     * Establece el cargo del administrador.
     * @param cargo Nuevo cargo.
     */
    public void setCargo(String cargo) { this.cargo = cargo; }
}
