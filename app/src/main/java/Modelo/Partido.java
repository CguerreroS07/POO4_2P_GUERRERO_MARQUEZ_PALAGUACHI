package Modelo;

import java.io.Serializable;

/**
 * Representa un partido del torneo con toda su información asociada.
 */
public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Identificador único del partido. */
    private String idPartido;
    /** Fase del torneo. */
    private String fase;
    /** Fecha del partido. */
    private String fecha;
    /** Hora del partido. */
    private String hora;
    /** Nombre del estadio. */
    private String estadio;
    /** Selección 1. */
    private String seleccion1;
    /** Selección 2. */
    private String seleccion2;
    /** Estado del partido (Abierto, Cerrado, Finalizado). */
    private EstadoPartido estado;
    /** Goles anotados por la selección 1. */
    private int goles1;
    /** Goles anotados por la selección 2. */
    private int goles2;

    /**
     * Constructor para la clase Partido.
     *
     * @param idPartido  Identificador único del partido.
     * @param fase       Fase del torneo a la que pertenece el partido.
     * @param fecha      Fecha en la que se juega el partido.
     * @param hora       Hora en la que se juega el partido.
     * @param estadio    Nombre del estadio donde se realiza el encuentro.
     * @param seleccion1 Nombre de la primera selección.
     * @param seleccion2 Nombre de la segunda selección.
     * @param estado     Estado actual del partido (Abierto, Cerrado, Finalizado).
     * @param goles1     Goles oficiales anotados por la selección 1.
     * @param goles2     Goles oficiales anotados por la selección 2.
     */
    public Partido(String idPartido, String fase, String fecha, String hora, String estadio, String seleccion1, String seleccion2, EstadoPartido estado, int goles1, int goles2) {
        this.idPartido = idPartido;
        this.fase = fase;
        this.fecha = fecha;
        this.hora = hora;
        this.estadio = estadio;
        this.seleccion1 = seleccion1;
        this.seleccion2 = seleccion2;
        this.estado = estado;
        this.goles1 = goles1;
        this.goles2 = goles2;
    }

    /**
     * Obtiene el ID del partido.
     * @return ID único.
     */
    public String getIdPartido() { return idPartido; }
    /**
     * Obtiene la fase del torneo.
     * @return Fase actual.
     */
    public String getFase() { return fase; }
    /**
     * Obtiene la fecha del partido.
     * @return Fecha del encuentro.
     */
    public String getFecha() { return fecha; }
    /**
     * Obtiene la hora del partido.
     * @return Hora del encuentro.
     */
    public String getHora() { return hora; }
    /**
     * Obtiene el estadio del partido.
     * @return Estadio.
     */
    public String getEstadio() { return estadio; }
    /**
     * Obtiene el nombre de la selección 1.
     * @return Nombre selección 1.
     */
    public String getSeleccion1() { return seleccion1; }
    /**
     * Obtiene el nombre de la selección 2.
     * @return Nombre selección 2.
     */
    public String getSeleccion2() { return seleccion2; }
    /**
     * Obtiene el estado del partido.
     * @return Estado actual.
     */
    public EstadoPartido getEstado() { return estado; }
    /**
     * Obtiene los goles de la selección 1.
     * @return Goles selección 1.
     */
    public int getGoles1() { return goles1; }
    /**
     * Obtiene los goles de la selección 2.
     * @return Goles selección 2.
     */
    public int getGoles2() { return goles2; }

    /**
     * Establece el estado del partido.
     * @param estado Nuevo estado.
     */
    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }
    /**
     * Establece los goles de la selección 1.
     * @param goles1 Goles selección 1.
     */
    public void setGoles1(int goles1) {
        this.goles1 = goles1;
    }
    /**
     * Establece los goles de la selección 2.
     * @param goles2 Goles selección 2.
     */
    public void setGoles2(int goles2) {
        this.goles2 = goles2;
    }
}
