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
            partido.cambiarEstado(EstadoPartido.CERRADO);
        }
    }

    public void registrarResultadoOficial(Partido partido, int goles1, int goles2) {
        if (partido != null) {
            partido.setGolesOficiales1(goles1);
            partido.setGolesOficiales2(goles2);
            partido.cambiarEstado(EstadoPartido.FINALIZADO);
        }
    }

    public void actualizarPuntajesGeneral() {
        // Método invocado desde el Activity para recalcular los puntos
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
}
