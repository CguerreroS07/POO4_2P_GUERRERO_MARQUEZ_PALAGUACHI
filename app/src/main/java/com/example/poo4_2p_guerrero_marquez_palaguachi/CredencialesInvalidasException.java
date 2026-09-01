package com.example.poo4_2p_guerrero_marquez_palaguachi;

/**
 * Excepción personalizada que se lanza cuando las credenciales ingresadas (usuario o contraseña)
 * no coinciden con ningún registro en el sistema.
 */
public class CredencialesInvalidasException extends Exception {
    /**
     * Constructor de la excepción con un mensaje específico.
     *
     * @param mensaje El detalle del error.
     */
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
