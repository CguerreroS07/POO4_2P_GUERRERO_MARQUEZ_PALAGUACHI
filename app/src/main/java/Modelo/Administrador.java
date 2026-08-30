package Modelo;

import java.io.Serializable;

public class Administrador extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cargo;

    public Administrador(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, String tipoUsuario, String cargo) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.cargo = cargo;
    }

    public void cerrarPronosticos(Partido partido) {
        if (partido != null) {
            // Se usa el setter correcto que definimos en la clase Partido
            partido.setEstado(EstadoPartido.CERRADO);
        }
    }

    public void registrarResultadoOficial(Partido partido, int goles1, int goles2) {
        if (partido != null) {
            // Se usan los setters correctos para los goles
            partido.setGoles1(goles1);
            partido.setGoles2(goles2);
            partido.setEstado(EstadoPartido.FINALIZADO);
        }
    }

    public void actualizarPuntajesGeneral() {
        // Método invocado desde el Activity para recalcular los puntos
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}