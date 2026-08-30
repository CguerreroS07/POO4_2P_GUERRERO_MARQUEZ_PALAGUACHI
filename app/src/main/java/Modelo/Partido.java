package Modelo;

import java.io.Serializable;

public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idPartido;
    private String fase;
    private String fecha;
    private String hora;
    private String estadio;
    private String seleccion1;
    private String seleccion2;
    private EstadoPartido estado;
    private int goles1;
    private int goles2;

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

    public String getIdPartido() { return idPartido; }
    public String getFase() { return fase; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getEstadio() { return estadio; }
    public String getSeleccion1() { return seleccion1; }
    public String getSeleccion2() { return seleccion2; }
    public EstadoPartido getEstado() { return estado; }
    public int getGoles1() { return goles1; }
    public int getGoles2() { return goles2; }
    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }
    public void setGoles1(int goles1) {
        this.goles1 = goles1;
    }
    public void setGoles2(int goles2) {
        this.goles2 = goles2;
    }
}