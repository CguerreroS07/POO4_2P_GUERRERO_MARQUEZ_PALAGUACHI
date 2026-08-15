package com.example.poo4_2p_guerrero_marquez_palaguachi;

public class Administrador extends Usuario {
    private String cargo;

    public Administrador(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario, String cargo) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.cargo = cargo;
    }
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}