package Modelo;

import java.io.Serializable;

public class Pronostico implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idPronostico;
    private String idPartido;
    private String idParticipante;
    private FaseTorneo faseTorneo;
    private int golesSeleccion1;
    private int golesSeleccion2;
    private int puntosObtenidos;

    public Pronostico(String idPronostico, String idPartido, String idParticipante, FaseTorneo faseTorneo, int golesSeleccion1, int golesSeleccion2, int puntosObtenidos) {
        this.idPronostico = idPronostico;
        this.idPartido = idPartido;
        this.idParticipante = idParticipante;
        this.faseTorneo = faseTorneo;
        this.golesSeleccion1 = golesSeleccion1;
        this.golesSeleccion2 = golesSeleccion2;
        this.puntosObtenidos = puntosObtenidos;
    }

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
    public String getIdPronostico() { return idPronostico; }
    public String getIdPartido() { return idPartido; }
    public String getIdParticipante() { return idParticipante; }
    public FaseTorneo getFaseTorneo() { return faseTorneo; }
    public int getGolesSeleccion1() { return golesSeleccion1; }
    public int getGolesSeleccion2() { return golesSeleccion2; }
    public int getPuntosObtenidos() { return puntosObtenidos; }

    public void setGolesSeleccion1(int golesSeleccion1) { this.golesSeleccion1 = golesSeleccion1; }
    public void setGolesSeleccion2(int golesSeleccion2) { this.golesSeleccion2 = golesSeleccion2; }
    public void setPuntosObtenidos(int puntosObtenidos) { this.puntosObtenidos = puntosObtenidos; }
}
