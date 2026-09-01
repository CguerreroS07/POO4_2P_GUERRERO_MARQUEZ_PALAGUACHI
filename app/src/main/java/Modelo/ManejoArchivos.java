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

/**
 * Clase utilitaria para la gestión de persistencia de datos en el sistema.
 * Permite la lectura y escritura de usuarios, partidos y pronósticos tanto en formato texto como serializado.
 */
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

    /**
     * Lee la lista de usuarios desde el archivo de almacenamiento interno.
     * Si el archivo no existe, lo copia desde los assets.
     *
     * @param context El contexto de la aplicación.
     * @return Una lista de objetos Usuario (Participantes y Administradores).
     */
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

    /**
     * Lee la lista de partidos desde el archivo de texto.
     *
     * @param context El contexto de la aplicación.
     * @return Una lista de objetos Partido.
     */
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

                if (datos.length >= 8) {
                    String id = datos[0].trim();
                    String fase = datos[1].trim();
                    String fecha = datos[2].trim();
                    String hora = datos[3].trim();
                    String estadio = datos[4].trim();
                    String sel1 = datos[5].trim();
                    String sel2 = datos[6].trim();
                    EstadoPartido estado = EstadoPartido.valueOf(datos[7].trim().toUpperCase());

                    int goles1 = 0;
                    int goles2 = 0;

                    listaPartidos.add(new Partido(id, fase, fecha, hora, estadio, sel1, sel2, estado, goles1, goles2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        cargarResultadosEnPartidos(context, listaPartidos);

        return listaPartidos;
    }

    private static void cargarResultadosEnPartidos(Context context, List<Partido> partidos) {
        File archivo = new File(context.getFilesDir(), "resultados.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(archivo)))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 4) {
                    String idPartido = datos[1].trim();
                    int goles1 = Integer.parseInt(datos[2].trim());
                    int goles2 = Integer.parseInt(datos[3].trim());

                    for (Partido p : partidos) {
                        if (p.getIdPartido().equals(idPartido)) {
                            p.setGoles1(goles1);
                            p.setGoles2(goles2);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda el estado actual de la lista de partidos en el archivo de texto.
     *
     * @param context  El contexto de la aplicación.
     * @param partidos Lista de partidos a guardar.
     */
    public static void guardarEstadoPartidos(Context context, List<Partido> partidos) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(context.openFileOutput("partidos.txt", Context.MODE_PRIVATE))
        )) {
            writer.write("idPartido;fase;fecha;horaUTC;estadio;seleccion1;seleccion2;estado");
            writer.newLine();

            for (Partido p : partidos) {
                writer.write(p.getIdPartido() + ";" + p.getFase() + ";" + p.getFecha() + ";" + p.getHora() + ";" + p.getEstadio() + ";" + p.getSeleccion1() + ";" + p.getSeleccion2() + ";" + p.getEstado().name());
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda los resultados oficiales de los partidos en un archivo de texto.
     *
     * @param context    El contexto de la aplicación.
     * @param resultados Lista de cadenas con el formato de resultado oficial.
     */
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

    /**
     * Guarda un nuevo pronóstico o reemplaza uno existente para un mismo partido y participante.
     * Los pronósticos se almacenan de forma serializada en archivos por participante y fase.
     *
     * @param context         El contexto de la aplicación.
     * @param nuevoPronostico El objeto Pronostico a guardar.
     * @return true si se guardó con éxito, false en caso contrario.
     */
    public static boolean guardarOReemplazarPronostico(android.content.Context context, Modelo.Pronostico nuevoPronostico) {
        String nombreArchivo = "pronostico_" + nuevoPronostico.getIdParticipante() + "_" + nuevoPronostico.getFaseTorneo().name().toLowerCase() + ".dat";
        java.util.List<Modelo.Pronostico> listaPronosticos = new java.util.ArrayList<>();

        try {
            java.io.FileInputStream fis = context.openFileInput(nombreArchivo);
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);
            listaPronosticos = (java.util.List<Modelo.Pronostico>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
        }

        boolean existe = false;
        for (int i = 0; i < listaPronosticos.size(); i++) {
            if (listaPronosticos.get(i).getIdPartido().equals(nuevoPronostico.getIdPartido())) {
                listaPronosticos.set(i, nuevoPronostico);
                existe = true;
                break;
            }
        }

        if (!existe) {
            listaPronosticos.add(nuevoPronostico);
        }

        try {
            java.io.FileOutputStream fos = context.openFileOutput(nombreArchivo, android.content.Context.MODE_PRIVATE);
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(fos);
            oos.writeObject(listaPronosticos);
            oos.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.e("ERROR_ARCHIVO", "No se pudo guardar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Serializa un pronóstico añadiéndolo a un archivo existente o creando uno nuevo.
     *
     * @param context       El contexto de la aplicación.
     * @param pronostico    El pronóstico a serializar.
     * @param nombreArchivo Nombre del archivo de destino.
     */
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

    /**
     * Deserializa una lista de pronósticos desde un archivo específico.
     *
     * @param context       El contexto de la aplicación.
     * @param nombreArchivo El nombre del archivo a leer.
     * @return Una lista de objetos Pronostico.
     */
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

    /**
     * Actualiza los puntajes acumulados de los participantes en el archivo de texto.
     *
     * @param context       El contexto de la aplicación.
     * @param participantes Lista de participantes con sus puntajes actualizados.
     */
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
