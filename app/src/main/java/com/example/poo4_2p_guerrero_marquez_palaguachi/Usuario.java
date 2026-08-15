package com.example.poo4_2p_guerrero_marquez_palaguachi;

public class Usuario {
    protected String idUsuario;
    protected String nombreUsuario;
    protected String contrasena;
    protected String nombreCompleto;
    protected String tipoUsuario;

    public Usuario(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.tipoUsuario = tipoUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }
}