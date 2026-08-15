package Modelo;

import java.io.Serializable;

public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idPartido;
    private String fecha;
    private String hora;
    private String estadio;
    private String seleccion1;
    private String seleccion2;
    private EstadoPartido estado;
    private int golesOficiales1;
    private int golesOficiales2;

    public Partido(String idPartido, String fecha, String hora, String estadio, String seleccion1, String seleccion2, EstadoPartido estado, int golesOficiales1, int golesOficiales2) {
        this.idPartido = idPartido;
        this.fecha = fecha;
        this.hora = hora;
        this.estadio = estadio;
        this.seleccion1 = seleccion1;
        this.seleccion2 = seleccion2;
        this.estado = estado;
        this.golesOficiales1 = golesOficiales1;
        this.golesOficiales2 = golesOficiales2;
    }

    public void cambiarEstado(EstadoPartido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    // Getters y Setters
    public String getIdPartido() { return idPartido; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getEstadio() { return estadio; }
    public String getSeleccion1() { return seleccion1; }
    public String getSeleccion2() { return seleccion2; }
    public EstadoPartido getEstado() { return estado; }
    public int getGolesOficiales1() { return golesOficiales1; }
    public int getGolesOficiales2() { return golesOficiales2; }

    public void setGolesOficiales1(int golesOficiales1) { this.golesOficiales1 = golesOficiales1; }
    public void setGolesOficiales2(int golesOficiales2) { this.golesOficiales2 = golesOficiales2; }
    public void setEstado(EstadoPartido estado) { this.estado = estado; }
}