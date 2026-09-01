package Modelo;

/**
 * Enumeración que define los posibles estados de un partido del torneo.
 */
public enum EstadoPartido {
    /** El partido permite el registro y modificación de pronósticos. */
    ABIERTO,
    /** Los pronósticos para este partido ya no pueden ser modificados. */
    CERRADO,
    /** El partido ha concluido y su resultado oficial ha sido registrado. */
    FINALIZADO
}
