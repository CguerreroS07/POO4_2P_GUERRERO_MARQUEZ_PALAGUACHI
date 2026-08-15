package com.example.poo4_2p_guerrero_marquez_palaguachi;

public class Participante extends Usuario implements Comparable<Participante> {
    private int puntajeAcumulado;

    public Participante(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario, int puntajeAcumulado) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.puntajeAcumulado = puntajeAcumulado;
    }
    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }
    public void setPuntajeAcumulado(int puntajeAcumulado) {
        this.puntajeAcumulado = puntajeAcumulado;
    }

    @Override
    public int compareTo(Participante otro) {
        int resultado = Integer.compare(otro.puntajeAcumulado, this.puntajeAcumulado);
        if (resultado == 0) {

            return this.nombreUsuario.compareTo(otro.nombreUsuario);
        }
        return resultado;
    }
}