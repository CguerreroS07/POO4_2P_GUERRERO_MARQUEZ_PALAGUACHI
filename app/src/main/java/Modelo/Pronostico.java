package Modelo;

import java.io.Serializable;

/**
 * Representa el pronóstico realizado por un participante para un partido determinado.
 */
public class Pronostico implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador único del pronóstico. */
    private String idPronostico;
    /** Identificador del partido asociado. */
    private String idPartido;
    /** Identificador del participante que realizó el pronóstico. */
    private String idParticipante;
    /** Fase del torneo en la que se registró el pronóstico. */
    private FaseTorneo faseTorneo;
    /** Goles pronosticados para la selección 1. */
    private int golesSeleccion1;
    /** Goles pronosticados para la selección 2. */
    private int golesSeleccion2;
    /** Puntos obtenidos con este pronóstico tras el resultado oficial. */
    private int puntosObtenidos;

    /**
     * Constructor para la clase Pronostico.
     *
     * @param idPronostico    Identificador único del pronóstico.
     * @param idPartido       ID del partido al que aplica el pronóstico.
     * @param idParticipante  ID del participante que realiza el pronóstico.
     * @param faseTorneo      Fase del torneo asociada.
     * @param golesSeleccion1 Goles pronosticados para la selección 1.
     * @param golesSeleccion2 Goles pronosticados para la selección 2.
     * @param puntosObtenidos Puntos ganados con este pronóstico.
     */
    public Pronostico(String idPronostico, String idPartido, String idParticipante, FaseTorneo faseTorneo, int golesSeleccion1, int golesSeleccion2, int puntosObtenidos) {
        this.idPronostico = idPronostico;
        this.idPartido = idPartido;
        this.idParticipante = idParticipante;
        this.faseTorneo = faseTorneo;
        this.golesSeleccion1 = golesSeleccion1;
        this.golesSeleccion2 = golesSeleccion2;
        this.puntosObtenidos = puntosObtenidos;
    }

    /**
     * Calcula los puntos obtenidos en este pronóstico comparándolo con el resultado oficial.
     * Reglas:
     * - 3 puntos: Acierto exacto del marcador.
     * - 2 puntos: Acierto del ganador y la diferencia de goles (o empate correcto).
     * - 1 punto: Acierto únicamente del ganador o del empate (sin coincidir diferencia).
     * - 0 puntos: Ningún acierto.
     *
     * @param golesOficiales1 Goles oficiales anotados por la selección 1.
     * @param golesOficiales2 Goles oficiales anotados por la selección 2.
     */
    public void calcularPuntos(int golesOficiales1, int golesOficiales2) {
        boolean aciertoExacto = (this.golesSeleccion1 == golesOficiales1) && (this.golesSeleccion2 == golesOficiales2);

        int diffPronostico = this.golesSeleccion1 - this.golesSeleccion2;
        int diffOficial = golesOficiales1 - golesOficiales2;

        boolean ganadorPronosticado = Integer.signum(diffPronostico) == Integer.signum(diffOficial);

        if (aciertoExacto) {
            this.puntosObtenidos = 3;
        } else if (ganadorPronosticado && (diffPronostico == diffOficial)) {
            this.puntosObtenidos = 2;
        } else if (diffPronostico == 0 && diffOficial == 0) {
            this.puntosObtenidos = 2;
        } else if (ganadorPronosticado) {
            this.puntosObtenidos = 1;
        } else {
            this.puntosObtenidos = 0;
        }
    }

    // Getters y Setters
    /**
     * Obtiene el ID del pronóstico.
     * @return ID único.
     */
    public String getIdPronostico() { return idPronostico; }
    /**
     * Obtiene el ID del partido.
     * @return ID del partido.
     */
    public String getIdPartido() { return idPartido; }
    /**
     * Obtiene el ID del participante.
     * @return ID del participante.
     */
    public String getIdParticipante() { return idParticipante; }
    /**
     * Obtiene la fase del torneo.
     * @return Fase del torneo.
     */
    public FaseTorneo getFaseTorneo() { return faseTorneo; }
    /**
     * Obtiene los goles pronosticados para la selección 1.
     * @return Goles selección 1.
     */
    public int getGolesSeleccion1() { return golesSeleccion1; }
    /**
     * Obtiene los goles pronosticados para la selección 2.
     * @return Goles selección 2.
     */
    public int getGolesSeleccion2() { return golesSeleccion2; }
    /**
     * Obtiene los puntos obtenidos.
     * @return Puntos calculados.
     */
    public int getPuntosObtenidos() { return puntosObtenidos; }

    /**
     * Establece los goles pronosticados para la selección 1.
     * @param golesSeleccion1 Goles selección 1.
     */
    public void setGolesSeleccion1(int golesSeleccion1) { this.golesSeleccion1 = golesSeleccion1; }
    /**
     * Establece los goles pronosticados para la selección 2.
     * @param golesSeleccion2 Goles selección 2.
     */
    public void setGolesSeleccion2(int golesSeleccion2) { this.golesSeleccion2 = golesSeleccion2; }
    /**
     * Establece los puntos obtenidos.
     * @param puntosObtenidos Nuevo valor de puntos.
     */
    public void setPuntosObtenidos(int puntosObtenidos) { this.puntosObtenidos = puntosObtenidos; }
}
