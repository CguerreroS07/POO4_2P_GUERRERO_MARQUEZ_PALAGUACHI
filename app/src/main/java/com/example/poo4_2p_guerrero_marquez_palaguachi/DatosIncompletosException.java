package com.example.poo4_2p_guerrero_marquez_palaguachi;

/**
 * Excepción lanzada cuando los datos necesarios para una operación están incompletos o son inválidos.
 */
public class DatosIncompletosException extends Exception {
    /**
     * Constructor para la excepción.
     * @param mensaje Detalle del error.
     */
    public DatosIncompletosException(String mensaje) {
        super(mensaje);
    }

    /**
     * Subclase de excepción para casos donde se intenta registrar un pronóstico fuera del tiempo permitido.
     */
    public static class PronosticoFueraDeTiempoException extends Exception {
        /**
         * Constructor para la excepción de tiempo.
         * @param mensaje Detalle del error.
         */
        public PronosticoFueraDeTiempoException(String mensaje) {
            super(mensaje);
        }
    }
}
