package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Modelo.EstadoPartido;
import Modelo.ManejoArchivos;
import Modelo.Partido;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class AdministrarPartidosActivity extends AppCompatActivity {
    private Spinner spinnerFasesAdmin;
    private LinearLayout contenedorPartidosAdmin;
    private Button btnVolverAdmin;
    private List<Partido> listaPartidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_partidos);

        spinnerFasesAdmin = findViewById(R.id.spinnerFasesAdmin);
        contenedorPartidosAdmin = findViewById(R.id.contenedorPartidosAdmin);
        btnVolverAdmin = findViewById(R.id.btnVolverAdmin);

        String[] fases = {
                "Fase de grupos",
                "Dieciseisavos de final",
                "Octavos de final",
                "Cuartos de final",
                "Semifinales",
                "Partido por el tercer lugar",
                "Final"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, fases);
        spinnerFasesAdmin.setAdapter(adapter);

        cargarPartidosDesdeArchivo();

        spinnerFasesAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String faseSeleccionada = fases[position];
                mostrarPartidosPorFase(faseSeleccionada);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        btnVolverAdmin.setOnClickListener(v -> finish());
    }

    private void cargarPartidosDesdeArchivo() {
        listaPartidos = ManejoArchivos.leerPartidos(this);
    }
    private void guardarPartidosEnArchivo() {
        ManejoArchivos.guardarEstadoPartidos(this, listaPartidos);
    }

    private void mostrarPartidosPorFase(String fase) {
        contenedorPartidosAdmin.removeAllViews();
        if (listaPartidos == null) return;
        for (Partido p : listaPartidos) {
            if (p.getFase().equalsIgnoreCase(fase)) {
                View card = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, contenedorPartidosAdmin, false);
                TextView text1 = card.findViewById(android.R.id.text1);
                TextView text2 = card.findViewById(android.R.id.text2);

                text1.setText(p.getSeleccion1() + " vs " + p.getSeleccion2());
                text2.setText("Estado: " + p.getEstado() + " | Fecha: " + p.getFecha());

                card.setOnClickListener(v -> {
                    if (p.getEstado() == EstadoPartido.ABIERTO) {
                        p.setEstado(EstadoPartido.CERRADO);
                        guardarPartidosEnArchivo();
                        Toast.makeText(this, "Partido cerrado para pronósticos", Toast.LENGTH_SHORT).show();
                        mostrarPartidosPorFase(fase);
                    } else if (p.getEstado() == EstadoPartido.CERRADO) {
                        try {
                            registrarResultadoOficial(p.getIdPartido(), "1", "0");
                        } catch (DatosIncompletosException e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                contenedorPartidosAdmin.addView(card);
            }
        }
    }
    private void registrarResultadoOficial(String idPartido, String goles1, String goles2) throws DatosIncompletosException {
        if (goles1.isEmpty() || goles2.isEmpty()) {
            throw new DatosIncompletosException("Los goles no pueden estar vacíos.");
        }
        try {
            int g1 = Integer.parseInt(goles1);
            int g2 = Integer.parseInt(goles2);
            if (g1 < 0 || g2 < 0) {
                throw new DatosIncompletosException("Los goles deben ser mayores o iguales a cero.");
            }
            List<String> resultadosActuales = new ArrayList<>();
            File archivoRes = new File(getFilesDir(), "resultados.txt");
            if (archivoRes.exists()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(archivoRes)))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        resultadosActuales.add(linea);
                    }
                }
            }
            String idResultado = "RES_" + idPartido;
            String nuevoResultado = idResultado + ";" + idPartido + ";" + g1 + ";" + g2;
            resultadosActuales.add(nuevoResultado);
            ManejoArchivos.guardarResultados(this, resultadosActuales);

            for (Partido p : listaPartidos) {
                if (p.getIdPartido().equals(idPartido)) {
                    p.setEstado(EstadoPartido.FINALIZADO);
                    break;
                }
            }
            guardarPartidosEnArchivo();
            Toast.makeText(this, "Resultado guardado y partido finalizado.", Toast.LENGTH_SHORT).show();
            mostrarPartidosPorFase(spinnerFasesAdmin.getSelectedItem().toString());
        } catch (NumberFormatException e) {
            throw new DatosIncompletosException("Formato de goles incorrecto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}