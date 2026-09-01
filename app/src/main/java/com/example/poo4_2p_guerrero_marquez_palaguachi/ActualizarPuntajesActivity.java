package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modelo.ManejoArchivos;
import Modelo.Partido;
import Modelo.Participante;
import Modelo.Pronostico;

/**
 * Actividad que permite al administrador actualizar los puntajes de todos los participantes.
 * El proceso consiste en comparar los pronósticos registrados con los resultados oficiales de los partidos finalizados.
 */
public class ActualizarPuntajesActivity extends AppCompatActivity {

    /** Botón para disparar el proceso de actualización de puntajes. */
    private Button btnActualizarPuntajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_puntajes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnActualizarPuntajes = findViewById(R.id.btnActualizarPuntajes);
        btnActualizarPuntajes.setOnClickListener(v -> procesarActualizacion());
    }

    public void volverAlMenu(View view) {
        finish();
    }

    private void procesarActualizacion() {
        List<Partido> todosLosPartidos = ManejoArchivos.leerPartidos(this);

        HashMap<Integer, Partido> partidosFinalizados = new HashMap<>();

        for (Partido p : todosLosPartidos) {
            if (p.getEstado().name().equals("FINALIZADO")) {
                try {
                    int idPartido = Integer.parseInt(p.getIdPartido().trim());
                    partidosFinalizados.put(idPartido, p);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        HashMap<String, Integer> puntajesAcumulados = new HashMap<>();
        List<Modelo.Usuario> todosUsuarios = ManejoArchivos.leerUsuarios(this);
        for (Modelo.Usuario u : todosUsuarios) {
            if (u.getTipoUsuario().equalsIgnoreCase("Participante")) {
                puntajesAcumulados.put(u.getIdUsuario(), 0);
            }
        }

        File directorio = getFilesDir();
        File[] archivos = directorio.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().startsWith("pronostico_") && archivo.getName().endsWith(".dat")) {
                    actualizarArchivoPronostico(archivo, partidosFinalizados, puntajesAcumulados);
                }
            }
        }

        List<Participante> listaParticipantes = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : puntajesAcumulados.entrySet()) {
            Participante p = new Participante(
                    entry.getKey(),
                    "", "", "",
                    "Participante",
                    entry.getValue()
            );
            listaParticipantes.add(p);
        }

        ManejoArchivos.actualizarPuntajesParticipantes(this, listaParticipantes);
        Toast.makeText(this, "¡Los puntajes fueron actualizados correctamente!", Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("unchecked")
    private void actualizarArchivoPronostico(File archivo, HashMap<Integer, Partido> partidosFinalizados, HashMap<String, Integer> puntajesAcumulados) {
        List<Pronostico> listaPronosticos = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(archivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                listaPronosticos = (List<Pronostico>) obj;
            }
        } catch (Exception e) {
            return;
        }

        boolean modificado = false;

        for (Pronostico pron : listaPronosticos) {
            String idUsuario = pron.getIdParticipante();

            try {
                int idPartidoPron = Integer.parseInt(pron.getIdPartido().trim());

                if (partidosFinalizados.containsKey(idPartidoPron)) {
                    Partido oficial = partidosFinalizados.get(idPartidoPron);

                    pron.calcularPuntos(oficial.getGoles1(), oficial.getGoles2());

                    int puntos = pron.getPuntosObtenidos();
                    int puntajeActual = puntajesAcumulados.getOrDefault(idUsuario, 0);
                    puntajesAcumulados.put(idUsuario, puntajeActual + puntos);

                    modificado = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (modificado) {
            try (FileOutputStream fos = openFileOutput(archivo.getName(), Context.MODE_PRIVATE);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(listaPronosticos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}