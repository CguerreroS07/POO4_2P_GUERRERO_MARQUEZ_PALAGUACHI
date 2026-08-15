package Modelo;

import java.io.Serializable;

public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;
    private String tipoUsuario;

    public Usuario(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.tipoUsuario = tipoUsuario;
    }

    public boolean autenticar() {
        return this.nombreUsuario != null && !this.nombreUsuario.isEmpty()
                && this.contrasena != null && !this.contrasena.isEmpty();
    }

    // Getters y Setters
    public String getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTipoUsuario() { return tipoUsuario; }

    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
}