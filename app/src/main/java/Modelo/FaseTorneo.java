package Modelo;

/**
 * Enumeración que define las distintas fases o etapas del torneo.
 */
public enum FaseTorneo {
    /** Etapa inicial donde los equipos compiten en grupos. */
    FASE_DE_GRUPOS,
    /** Ronda eliminatoria de 32 equipos. */
    DIECISEISAVOS,
    /** Ronda eliminatoria de 16 equipos. */
    OCTAVOS,
    /** Ronda eliminatoria de 8 equipos. */
    CUARTOS_DE_FINAL,
    /** Ronda eliminatoria de 4 equipos. */
    SEMIFINALES,
    /** Partido para definir el tercer puesto del torneo. */
    TERCER_LUGAR,
    /** Partido final para definir al campeón del torneo. */
    FINAL
}
