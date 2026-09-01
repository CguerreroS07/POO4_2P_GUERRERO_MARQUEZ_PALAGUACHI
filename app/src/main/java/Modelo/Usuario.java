package Modelo;

import java.io.Serializable;

/**
 * Clase abstracta que representa la base para todos los usuarios del sistema.
 */
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador único del usuario. */
    private String idUsuario;
    /** Nombre de usuario para el inicio de sesión. */
    private String nombreUsuario;
    /** Contraseña de acceso al sistema. */
    private String contrasena;
    /** Nombre real completo del usuario. */
    private String nombreCompleto;
    /** Categoría del usuario: Administrador o Participante. */
    private String tipoUsuario;

    /**
     * Constructor base para la clase Usuario.
     *
     * @param idUsuario      Identificador único del usuario.
     * @param nombreUsuario  Nombre para el inicio de sesión.
     * @param contrasena     Clave de acceso.
     * @param nombreCompleto Nombre real del usuario.
     * @param tipoUsuario    Categoría del usuario (Administrador o Participante).
     */
    public Usuario(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Verifica si las credenciales del usuario son válidas (no nulas y no vacías).
     *
     * @return true si el usuario y la contraseña tienen contenido, false en caso contrario.
     */
    public boolean autenticar() {
        return this.nombreUsuario != null && !this.nombreUsuario.isEmpty()
                && this.contrasena != null && !this.contrasena.isEmpty();
    }

    // Getters y Setters
    /**
     * Obtiene el ID del usuario.
     * @return ID único.
     */
    public String getIdUsuario() { return idUsuario; }
    /**
     * Obtiene el nombre de usuario de inicio de sesión.
     * @return Nombre de usuario.
     */
    public String getNombreUsuario() { return nombreUsuario; }
    /**
     * Obtiene la contraseña del usuario.
     * @return Contraseña.
     */
    public String getContrasena() { return contrasena; }
    /**
     * Obtiene el nombre completo del usuario.
     * @return Nombre completo.
     */
    public String getNombreCompleto() { return nombreCompleto; }
    /**
     * Obtiene el tipo de usuario.
     * @return Tipo (Administrador/Participante).
     */
    public String getTipoUsuario() { return tipoUsuario; }

    /**
     * Establece el ID del usuario.
     * @param idUsuario Nuevo ID.
     */
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    /**
     * Establece el nombre de usuario de inicio de sesión.
     * @param nombreUsuario Nuevo nombre de usuario.
     */
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    /**
     * Establece la contraseña del usuario.
     * @param contrasena Nueva contraseña.
     */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    /**
     * Establece el nombre completo del usuario.
     * @param nombreCompleto Nuevo nombre completo.
     */
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    /**
     * Establece el tipo de usuario.
     * @param tipoUsuario Nuevo tipo.
     */
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
}
