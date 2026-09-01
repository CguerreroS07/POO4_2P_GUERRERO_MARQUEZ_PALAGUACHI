package Modelo;

import java.io.Serializable;

/**
 * Representa a un usuario participante en el sistema de pronósticos.
 * Los participantes pueden registrar pronósticos y acumular puntos.
 */
public class Participante extends Usuario implements Comparable<Participante>, Serializable {

    private static final long serialVersionUID = 1L;

    /** Puntaje total acumulado por el participante en el torneo. */
    private int puntajeAcumulado;

    /**
     * Constructor para la clase Participante.
     *
     * @param idUsuario        Identificador único del usuario.
     * @param nombreUsuario    Nombre de usuario para el inicio de sesión.
     * @param contrasena       Contraseña del usuario.
     * @param nombreCompleto   Nombre completo de la persona.
     * @param tipoUsuario      Tipo de usuario (Participante).
     * @param puntajeAcumulado Puntaje total acumulado por el participante.
     */
    public Participante(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario, int puntajeAcumulado) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.puntajeAcumulado = puntajeAcumulado;
    }

    /**
     * Compara este participante con otro para fines de ordenación en la tabla de posiciones.
     * La prioridad es el puntaje acumulado (descendente) y luego el nombre de usuario (ascendente).
     *
     * @param otro El otro participante con el que se va a comparar.
     * @return Un valor negativo, cero o positivo según la comparación.
     */
    @Override
    public int compareTo(Participante otro) {
        if (this.puntajeAcumulado != otro.puntajeAcumulado) {
            return Integer.compare(otro.puntajeAcumulado, this.puntajeAcumulado);
        }
        return this.getNombreUsuario().compareToIgnoreCase(otro.getNombreUsuario());
    }

    /**
     * Obtiene el puntaje acumulado.
     * @return Puntaje actual.
     */
    public int getPuntajeAcumulado() { return puntajeAcumulado; }
    /**
     * Establece el puntaje acumulado.
     * @param puntajeAcumulado Nuevo puntaje.
     */
    public void setPuntajeAcumulado(int puntajeAcumulado) { this.puntajeAcumulado = puntajeAcumulado; }
}
