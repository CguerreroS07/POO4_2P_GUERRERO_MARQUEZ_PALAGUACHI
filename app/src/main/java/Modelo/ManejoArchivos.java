package Modelo;

import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ManejoArchivos {

    private static void asegurarExistencia(Context context, String nombreArchivo) {
        File archivo = new File(context.getFilesDir(), nombreArchivo);
        if (!archivo.exists()) {
            try (
                    InputStream entrada = context.getAssets().open(nombreArchivo);
                    OutputStream salida = context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE)
            ) {
                byte[] buffer = new byte[1024];
                int bytesLeidos;
                while ((bytesLeidos = entrada.read(buffer)) != -1) {
                    salida.write(buffer, 0, bytesLeidos);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Usuario> leerUsuarios(Context context) {
        asegurarExistencia(context, "usuarios.txt");
        List<Usuario> listaUsuarios = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.openFileInput("usuarios.txt"))
        )) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(";");
                if (datos.length >= 5) {
                    String idUsuario = datos[0].trim();
                    String user = datos[1].trim();
                    String pass = datos[2].trim();
                    String nombreCompleto = datos[3].trim();
                    String tipoUsuario = datos[4].trim();

                    if (tipoUsuario.equalsIgnoreCase("Participante")) {
                        int puntaje = obtenerPuntaje(context, idUsuario);
                        listaUsuarios.add(new Participante(idUsuario, user, pass, nombreCompleto, tipoUsuario, puntaje));
                    } else if (tipoUsuario.equalsIgnoreCase("Administrador")) {
                        String cargo = obtenerCargo(context, idUsuario);
                        listaUsuarios.add(new Administrador(idUsuario, user, pass, nombreCompleto, tipoUsuario, cargo));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }

    private static int obtenerPuntaje(Context context, String idUsuario) {
        asegurarExistencia(context, "participantes.txt");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.openFileInput("participantes.txt"))
        )) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 2 && datos[0].trim().equals(idUsuario)) {
                    return Integer.parseInt(datos[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static String obtenerCargo(Context context, String idUsuario) {
        asegurarExistencia(context, "administradores.txt");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.openFileInput("administradores.txt"))
        )) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 2 && datos[0].trim().equals(idUsuario)) {
                    return datos[1].trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Administrador";
    }

    public static List<Partido> leerPartidos(Context context) {
        asegurarExistencia(context, "partidos.txt");
        List<Partido> listaPartidos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.openFileInput("partidos.txt"))
        )) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(";");

                // Ajuste de lectura para las 8 columnas del nuevo partidos.txt
                if (datos.length >= 8) {
                    String id = datos[0].trim();
                    String fecha = datos[2].trim();
                    String hora = datos[3].trim();
                    String estadio = datos[4].trim();
                    String sel1 = datos[5].trim();
                    String sel2 = datos[6].trim();
                    EstadoPartido estado = EstadoPartido.valueOf(datos[7].trim().toUpperCase());
                    int goles1 = 0;
                    int goles2 = 0;

                    listaPartidos.add(new Partido(id, fecha, hora, estadio, sel1, sel2, estado, goles1, goles2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaPartidos;
    }

    public static void guardarEstadoPartidos(Context context, List<Partido> partidos) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(context.openFileOutput("partidos.txt", Context.MODE_PRIVATE))
        )) {
            writer.write("idPartido;fase;fecha;horaUTC;estadio;seleccion1;seleccion2;estado");
            writer.newLine();

            for (Partido p : partidos) {
                writer.write(p.getIdPartido() + ";FASE_ACTUAL;" + p.getFecha() + ";" + p.getHora() + ";" + p.getEstadio() + ";" + p.getSeleccion1() + ";" + p.getSeleccion2() + ";" + p.getEstado().name());
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guardarResultados(Context context, List<String> resultados) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(context.openFileOutput("resultados.txt", Context.MODE_PRIVATE))
        )) {
            for (String res : resultados) {
                writer.write(res);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serializarPronostico(Context context, Pronostico pronostico, String nombreArchivo) {
        List<Pronostico> lista = deserializarPronosticos(context, nombreArchivo);
        lista.add(pronostico);

        try (FileOutputStream fos = context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Pronostico> deserializarPronosticos(Context context, String nombreArchivo) {
        List<Pronostico> lista = new ArrayList<>();
        File archivo = new File(context.getFilesDir(), nombreArchivo);

        if (!archivo.exists()) {
            return lista;
        }

        try (FileInputStream fis = context.openFileInput(nombreArchivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            lista = (List<Pronostico>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void actualizarPuntajesParticipantes(Context context, List<Participante> participantes) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(context.openFileOutput("participantes.txt", Context.MODE_PRIVATE))
        )) {
            writer.write("idUsuario;puntajeAcumulado");
            writer.newLine();

            for (Participante p : participantes) {
                String linea = p.getIdUsuario() + ";" + p.getPuntajeAcumulado();
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}