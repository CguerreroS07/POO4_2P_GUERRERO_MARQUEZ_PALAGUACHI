package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Modelo.EstadoPartido;
import Modelo.ManejoArchivos;
import Modelo.Partido;

public class AdministrarPartidosActivity extends AppCompatActivity {
    private Spinner spinnerFasesAdmin;
    private LinearLayout contenedorPartidosAdmin;
    private List<Partido> listaPartidos;
    private java.util.HashMap<String, android.graphics.drawable.Drawable> cacheBanderas = new java.util.HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_partidos);

        spinnerFasesAdmin = findViewById(R.id.spinnerFasesAdmin);
        contenedorPartidosAdmin = findViewById(R.id.contenedorPartidosAdmin);

        cargarPartidosDesdeArchivo();
        cargarFasesSpinner();
    }

    public void volverAlMenu(View view) {
        finish();
    }

    private void cargarPartidosDesdeArchivo() {
        listaPartidos = ManejoArchivos.leerPartidos(this);
    }

    private void guardarPartidosEnArchivo() {
        ManejoArchivos.guardarEstadoPartidos(this, listaPartidos);
    }

    private String mapearFaseAEnum(String faseSpinner) {
        switch (faseSpinner) {
            case "Fase de grupos": return "FASE_DE_GRUPOS";
            case "Dieciseisavos": return "DIECISEISAVOS_DE_FINAL";
            case "Octavos": return "OCTAVOS_DE_FINAL";
            case "Cuartos de final": return "CUARTOS_DE_FINAL";
            case "Semifinales": return "SEMIFINALES";
            case "Tercer lugar": return "TERCER_LUGAR";
            case "Final": return "FINAL";
            default: return "";
        }
    }

    private void cargarFasesSpinner() {
        String[] fases = {
                "Fase de grupos",
                "Dieciseisavos",
                "Octavos",
                "Cuartos de final",
                "Semifinales",
                "Tercer lugar",
                "Final"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, fases);
        spinnerFasesAdmin.setAdapter(adapter);

        spinnerFasesAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mostrarPartidosPorFase(fases[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void mostrarPartidosPorFase(String faseSeleccionada) {
        contenedorPartidosAdmin.removeAllViews();
        if (listaPartidos == null) return;

        for (Partido p : listaPartidos) {
            String faseCodigo = mapearFaseAEnum(faseSeleccionada);

            if (!p.getFase().trim().toUpperCase().equals(faseCodigo)) {
                continue;
            }

            View tarjeta = getLayoutInflater().inflate(R.layout.item_admin, contenedorPartidosAdmin, false);

            TextView tvEstadoAdmin = tarjeta.findViewById(R.id.tvEstadoAdmin);
            androidx.cardview.widget.CardView cardEstadoAdmin = tarjeta.findViewById(R.id.cardEstadoAdmin);
            TextView tvIdPartidoAdmin = tarjeta.findViewById(R.id.tvIdPartidoAdmin);
            TextView tvFechaAdmin = tarjeta.findViewById(R.id.tvFechaAdmin);
            TextView tvHoraAdmin = tarjeta.findViewById(R.id.tvHoraAdmin);
            TextView tvEstadioAdmin = tarjeta.findViewById(R.id.tvEstadioAdmin);
            ImageView imgBandera1Admin = tarjeta.findViewById(R.id.imgBandera1Admin);
            TextView tvEquipo1Admin = tarjeta.findViewById(R.id.tvEquipo1Admin);
            ImageView imgBandera2Admin = tarjeta.findViewById(R.id.imgBandera2Admin);
            TextView tvEquipo2Admin = tarjeta.findViewById(R.id.tvEquipo2Admin);

            Button btnCerrarAdmin = tarjeta.findViewById(R.id.btnCerrarAdmin);
            LinearLayout layoutRegistroResultados = tarjeta.findViewById(R.id.layoutRegistroResultados);
            EditText etGoles1Admin = tarjeta.findViewById(R.id.etGoles1Admin);
            EditText etGoles2Admin = tarjeta.findViewById(R.id.etGoles2Admin);
            Button btnGuardarResAdmin = tarjeta.findViewById(R.id.btnGuardarResAdmin);

            androidx.cardview.widget.CardView cardBannerAdmin = tarjeta.findViewById(R.id.cardBannerAdmin);
            TextView tvMensajeBannerAdmin = tarjeta.findViewById(R.id.tvMensajeBannerAdmin);

            tvIdPartidoAdmin.setText("Id: " + p.getIdPartido());
            tvFechaAdmin.setText(p.getFecha());
            tvHoraAdmin.setText(p.getHora());

            String estadioLimpio = p.getEstadio();
            int inicioP = estadioLimpio.indexOf("(");
            int finP = estadioLimpio.indexOf(")");
            if (inicioP != -1 && finP != -1) {
                estadioLimpio = estadioLimpio.substring(inicioP + 1, finP);
            }
            tvEstadioAdmin.setText(estadioLimpio);

            tvEquipo1Admin.setText(p.getSeleccion1());
            tvEquipo2Admin.setText(p.getSeleccion2());
            imgBandera1Admin.setImageDrawable(obtenerBanderaDesdeAssets(p.getSeleccion1()));
            imgBandera2Admin.setImageDrawable(obtenerBanderaDesdeAssets(p.getSeleccion2()));

            if (p.getEstado() == EstadoPartido.ABIERTO) {
                tvEstadoAdmin.setText("ABIERTO");
                cardEstadoAdmin.setCardBackgroundColor(Color.parseColor("#DCFCE7"));
                tvEstadoAdmin.setTextColor(Color.parseColor("#16A34A"));

                btnCerrarAdmin.setVisibility(View.VISIBLE);
                layoutRegistroResultados.setVisibility(View.GONE);

                cardBannerAdmin.setCardBackgroundColor(Color.parseColor("#F0FDF4"));
                tvMensajeBannerAdmin.setText("ℹ️ Los participantes pueden registrar o modificar sus pronósticos.");
                tvMensajeBannerAdmin.setTextColor(Color.parseColor("#16A34A"));

                btnCerrarAdmin.setOnClickListener(v -> {
                    p.setEstado(EstadoPartido.CERRADO);
                    guardarPartidosEnArchivo();
                    Toast.makeText(this, "Partido cerrado exitosamente.", Toast.LENGTH_SHORT).show();
                    mostrarPartidosPorFase(faseSeleccionada);
                });

            } else if (p.getEstado() == EstadoPartido.CERRADO) {
                tvEstadoAdmin.setText("CERRADO");
                cardEstadoAdmin.setCardBackgroundColor(Color.parseColor("#FEF9C3"));
                tvEstadoAdmin.setTextColor(Color.parseColor("#CA8A04"));

                btnCerrarAdmin.setVisibility(View.GONE);
                layoutRegistroResultados.setVisibility(View.VISIBLE);
                btnGuardarResAdmin.setVisibility(View.VISIBLE);

                etGoles1Admin.setEnabled(true);
                etGoles2Admin.setEnabled(true);

                cardBannerAdmin.setCardBackgroundColor(Color.parseColor("#FEF9C3"));
                tvMensajeBannerAdmin.setText("🔒 Los pronósticos están cerrados. Registra el resultado oficial.");
                tvMensajeBannerAdmin.setTextColor(Color.parseColor("#CA8A04"));

                btnGuardarResAdmin.setOnClickListener(v -> {
                    String strGoles1 = etGoles1Admin.getText().toString().trim();
                    String strGoles2 = etGoles2Admin.getText().toString().trim();
                    try {
                        registrarResultadoOficial(p.getIdPartido(), strGoles1, strGoles2, faseSeleccionada);
                    } catch (DatosIncompletosException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (p.getEstado() == EstadoPartido.FINALIZADO) {
                tvEstadoAdmin.setText("FINALIZADO");
                cardEstadoAdmin.setCardBackgroundColor(Color.parseColor("#F1F5F9"));
                tvEstadoAdmin.setTextColor(Color.parseColor("#475569"));

                btnCerrarAdmin.setVisibility(View.GONE);
                layoutRegistroResultados.setVisibility(View.VISIBLE);
                btnGuardarResAdmin.setVisibility(View.GONE);

                etGoles1Admin.setEnabled(false);
                etGoles2Admin.setEnabled(false);
                etGoles1Admin.setText(String.valueOf(p.getGoles1()));
                etGoles2Admin.setText(String.valueOf(p.getGoles2()));

                cardBannerAdmin.setCardBackgroundColor(Color.parseColor("#E0F2FE"));
                tvMensajeBannerAdmin.setText("✔ Resultado registrado. El partido ha finalizado.");
                tvMensajeBannerAdmin.setTextColor(Color.parseColor("#0369A1"));
            }

            contenedorPartidosAdmin.addView(tarjeta);
        }
    }

    private void registrarResultadoOficial(String idPartido, String goles1, String goles2, String faseActual) throws DatosIncompletosException {
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
                    p.setGoles1(g1);
                    p.setGoles2(g2);
                    break;
                }
            }

            guardarPartidosEnArchivo();
            Toast.makeText(this, "Resultado guardado y partido finalizado.", Toast.LENGTH_SHORT).show();
            mostrarPartidosPorFase(faseActual);

        } catch (NumberFormatException e) {
            throw new DatosIncompletosException("Formato de goles incorrecto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private android.graphics.drawable.Drawable obtenerBanderaDesdeAssets(String pais) {
        if (cacheBanderas.containsKey(pais)) {
            return cacheBanderas.get(pais);
        }
        String codigoArchivo = "";
        switch (pais) {
            case "Alemania": codigoArchivo = "de"; break;
            case "Arabia Saudita": codigoArchivo = "sa"; break;
            case "Argelia": codigoArchivo = "dz"; break;
            case "Argentina": codigoArchivo = "ar"; break;
            case "Australia": codigoArchivo = "au"; break;
            case "Austria": codigoArchivo = "at"; break;
            case "Bélgica": codigoArchivo = "be"; break;
            case "Bosnia y Herzegovina": codigoArchivo = "ba"; break;
            case "Brasil": codigoArchivo = "br"; break;
            case "Cabo Verde": codigoArchivo = "cv"; break;
            case "Canadá": codigoArchivo = "ca"; break;
            case "Catar": codigoArchivo = "qa"; break;
            case "Chequia": codigoArchivo = "cz"; break;
            case "Colombia": codigoArchivo = "co"; break;
            case "Corea del Sur": codigoArchivo = "kr"; break;
            case "Costa de Marfil": codigoArchivo = "ci"; break;
            case "Croacia": codigoArchivo = "hr"; break;
            case "Curazao": codigoArchivo = "cw"; break;
            case "Ecuador": codigoArchivo = "ec"; break;
            case "Egipto": codigoArchivo = "eg"; break;
            case "Escocia": codigoArchivo = "gb_sct"; break;
            case "España": codigoArchivo = "es"; break;
            case "Estados Unidos": codigoArchivo = "us"; break;
            case "Francia": codigoArchivo = "fr"; break;
            case "Ghana": codigoArchivo = "gh"; break;
            case "Haití": codigoArchivo = "ht"; break;
            case "Inglaterra": codigoArchivo = "gb_eng"; break;
            case "Irak": codigoArchivo = "iq"; break;
            case "Irán": codigoArchivo = "ir"; break;
            case "Japón": codigoArchivo = "jp"; break;
            case "Jordania": codigoArchivo = "jo"; break;
            case "Marruecos": codigoArchivo = "ma"; break;
            case "México": codigoArchivo = "mx"; break;
            case "Noruega": codigoArchivo = "no"; break;
            case "Nueva Zelanda": codigoArchivo = "nz"; break;
            case "Países Bajos": codigoArchivo = "nl"; break;
            case "Panamá": codigoArchivo = "pa"; break;
            case "Paraguay": codigoArchivo = "py"; break;
            case "Portugal": codigoArchivo = "pt"; break;
            case "República Democrática del Congo": codigoArchivo = "cd"; break;
            case "Senegal": codigoArchivo = "sn"; break;
            case "Sudáfrica": codigoArchivo = "za"; break;
            case "Suecia": codigoArchivo = "se"; break;
            case "Suiza": codigoArchivo = "ch"; break;
            case "Túnez": codigoArchivo = "tn"; break;
            case "Turquía": codigoArchivo = "tr"; break;
            case "Uruguay": codigoArchivo = "uy"; break;
            case "Uzbekistán": codigoArchivo = "uz"; break;
        }
        try {
            java.io.InputStream inputStream = getAssets().open("banderas/" + codigoArchivo + ".png");
            android.graphics.drawable.Drawable imagen = android.graphics.drawable.Drawable.createFromStream(inputStream, null);
            cacheBanderas.put(pais, imagen);
            return imagen;
        } catch (java.io.IOException e) {
            return getResources().getDrawable(android.R.drawable.ic_menu_camera, null);
        }
    }
}