package com.example.poo4_2p_guerrero_marquez_palaguachi;

public class DatosIncompletosException extends Exception {
    public DatosIncompletosException(String mensaje) {
        super(mensaje);
    }

    public static class PronosticoFueraDeTiempoException extends Exception {
        public PronosticoFueraDeTiempoException(String mensaje) {
            super(mensaje);
        }
    }
}
