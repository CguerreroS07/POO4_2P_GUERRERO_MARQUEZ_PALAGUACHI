package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        listaPartidos = new ArrayList<>();
        File archivo = new File(getFilesDir(), "partidos.txt");
        if (!archivo.exists()) return;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(archivo)))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 8) {
                    listaPartidos.add(new Partido(partes[0], partes[1], partes[2], partes[3], partes[4], partes[5], partes[6], partes[7]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void guardarPartidosEnArchivo() {
        File archivo = new File(getFilesDir(), "partidos.txt");
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(archivo))) {
            for (Partido p : listaPartidos) {
                writer.write(p.getIdPartido() + "," + p.getFase() + "," + p.getFecha() + "," + p.getHora() + "," + p.getEstadio() + "," + p.getSeleccion1() + "," + p.getSeleccion2() + "," + p.getEstado() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void mostrarPartidosPorFase(String fase) {
        contenedorPartidosAdmin.removeAllViews();

        for (Partido p : listaPartidos) {
            if (p.getFase().equalsIgnoreCase(fase)) {
                View card = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, contenedorPartidosAdmin, false);
                TextView text1 = card.findViewById(android.R.id.text1);
                TextView text2 = card.findViewById(android.R.id.text2);

                text1.setText(p.getSeleccion1() + " vs " + p.getSeleccion2());
                text2.setText("Estado: " + p.getEstado() + " | Fecha: " + p.getFecha());


                card.setOnClickListener(v -> {
                    if (p.getEstado().equalsIgnoreCase("ABIERTO")) {
                        p.setEstado("CERRADO");
                        guardarPartidosEnArchivo();
                        Toast.makeText(this, "Partido cerrado para pronósticos", Toast.LENGTH_SHORT).show();
                        mostrarPartidosPorFase(fase);
                    } else if (p.getEstado().equalsIgnoreCase("CERRADO")) {

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
            File archivoRes = new File(getFilesDir(), "resultados.txt");
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(archivoRes, true))) {
                String idResultado = "RES_" + idPartido;
                writer.write(idResultado + "," + idPartido + "," + g1 + "," + g2 + "\n");
            }
            for (Partido p : listaPartidos) {
                if (p.getIdPartido().equals(idPartido)) {
                    p.setEstado("FINALIZADO");
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