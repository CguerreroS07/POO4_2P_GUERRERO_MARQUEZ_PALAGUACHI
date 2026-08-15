package Modelo;

import java.io.Serializable;

public class Participante extends Usuario implements Comparable<Participante>, Serializable {

    private static final long serialVersionUID = 1L;

    private int puntajeAcumulado;

    public Participante(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario, int puntajeAcumulado) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.puntajeAcumulado = puntajeAcumulado;
    }

    @Override
    public int compareTo(Participante otro) {
        if (this.puntajeAcumulado != otro.puntajeAcumulado) {
            return Integer.compare(otro.puntajeAcumulado, this.puntajeAcumulado);
        }
        return this.getNombreUsuario().compareToIgnoreCase(otro.getNombreUsuario());
    }

    public int getPuntajeAcumulado() { return puntajeAcumulado; }
    public void setPuntajeAcumulado(int puntajeAcumulado) { this.puntajeAcumulado = puntajeAcumulado; }
}