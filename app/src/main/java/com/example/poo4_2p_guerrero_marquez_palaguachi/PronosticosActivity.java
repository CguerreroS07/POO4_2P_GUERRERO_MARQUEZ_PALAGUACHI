package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.UUID;

import Modelo.FaseTorneo;
import Modelo.ManejoArchivos;
import Modelo.Partido;
import Modelo.Pronostico;

/**
 * Actividad que permite a los participantes visualizar los partidos por fase y registrar sus pronósticos.
 * Solo se pueden guardar pronósticos para partidos que se encuentren en estado ABIERTO.
 */
public class PronosticosActivity extends AppCompatActivity {
    /** Caché en memoria para las imágenes de las banderas. */
    private java.util.HashMap<String, android.graphics.drawable.Drawable> cacheBanderas = new java.util.HashMap<>();
    /** Spinner para seleccionar la fase del torneo. */
    private Spinner spFaseTorneo;
    /** Contenedor dinámico para la lista de partidos. */
    private LinearLayout contenedorPartidos;
    /** Lista completa de partidos leída al inicio. */
    private List<Partido> todosLosPartidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pronosticos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spFaseTorneo = findViewById(R.id.spFaseTorneo);
        contenedorPartidos = findViewById(R.id.contenedorPartidos);

        todosLosPartidos = ManejoArchivos.leerPartidos(this);

        spFaseTorneo.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String faseElegida = parent.getItemAtPosition(position).toString();
                mostrarPartidosEnPantalla(faseElegida);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });


        cargarFasesSpinner();
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                fases
        );
        spFaseTorneo.setAdapter(adapter);

        mostrarPartidosEnPantalla(fases[0]);

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

    public void volverAlMenu(View view) {
        finish();
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
            default: codigoArchivo = ""; break;
        }

        try {
            java.io.InputStream inputStream = getAssets().open("banderas/" + codigoArchivo + ".png");
            android.graphics.drawable.Drawable imagenLeida = android.graphics.drawable.Drawable.createFromStream(inputStream, null);

            if (codigoArchivo != null && !codigoArchivo.isEmpty()) {
                cacheBanderas.put(pais, imagenLeida);
            }

            return imagenLeida;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return getResources().getDrawable(android.R.drawable.ic_menu_camera, null);
        }
    }

    private void mostrarPartidosEnPantalla(String faseSeleccionada) {
            contenedorPartidos.removeAllViews();
            String faseCodigo = mapearFaseAEnum(faseSeleccionada);

            for (Partido p : todosLosPartidos) {
                if (!p.getFase().trim().toUpperCase().equals(faseCodigo)) {
                    continue;
            }

            View tarjetaPartido = getLayoutInflater().inflate(R.layout.item_partido, contenedorPartidos, false);

            TextView tvFecha = tarjetaPartido.findViewById(R.id.tvFecha);
            TextView tvHora = tarjetaPartido.findViewById(R.id.tvHora);
            TextView tvEstadio = tarjetaPartido.findViewById(R.id.tvEstadio);
            TextView tvEstado = tarjetaPartido.findViewById(R.id.tvEstado);
            TextView tvEquipo1 = tarjetaPartido.findViewById(R.id.tvEquipo1);
            TextView tvEquipo2 = tarjetaPartido.findViewById(R.id.tvEquipo2);
            ImageView ivBandera1 = tarjetaPartido.findViewById(R.id.ivBandera1);
            ImageView ivBandera2 = tarjetaPartido.findViewById(R.id.ivBandera2);
            EditText etGoles1 = tarjetaPartido.findViewById(R.id.etGoles1);
            EditText etGoles2 = tarjetaPartido.findViewById(R.id.etGoles2);
            Button btnGuardar = tarjetaPartido.findViewById(R.id.btnGuardarPronostico);
            View bannerCerrado = tarjetaPartido.findViewById(R.id.bannerCerrado);
            View bannerFinalizado = tarjetaPartido.findViewById(R.id.bannerFinalizado);

            tvFecha.setText(p.getFecha());
            tvHora.setText(p.getHora());
            tvEquipo1.setText(p.getSeleccion1());
            tvEquipo2.setText(p.getSeleccion2());

            String estadioOriginal = p.getEstadio();
            String estadioLimpio = estadioOriginal;
            int inicioParentesis = estadioOriginal.indexOf("(");
            int finParentesis = estadioOriginal.indexOf(")");

            if (inicioParentesis != -1 && finParentesis != -1 && finParentesis > inicioParentesis) {
                estadioLimpio = estadioOriginal.substring(inicioParentesis + 1, finParentesis);
            }
            tvEstadio.setText(estadioLimpio);

            ivBandera1.setImageDrawable(obtenerBanderaDesdeAssets(p.getSeleccion1()));
            ivBandera2.setImageDrawable(obtenerBanderaDesdeAssets(p.getSeleccion2()));

            String estadoActual = p.getEstado().name();
            tvEstado.setText(estadoActual);

            if (estadoActual.equals("ABIERTO")) {
                tvEstado.setBackgroundColor(android.graphics.Color.parseColor("#DCFCE7"));
                tvEstado.setTextColor(android.graphics.Color.parseColor("#16A34A"));

                etGoles1.setEnabled(true);
                etGoles2.setEnabled(true);

                btnGuardar.setVisibility(View.VISIBLE);
                if(bannerCerrado != null) bannerCerrado.setVisibility(View.GONE);
                if(bannerFinalizado != null) bannerFinalizado.setVisibility(View.GONE);

                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strGoles1 = etGoles1.getText().toString().trim();
                        String strGoles2 = etGoles2.getText().toString().trim();

                        if (strGoles1.isEmpty() || strGoles2.isEmpty()) {
                            Toast.makeText(PronosticosActivity.this, "Por favor, ingresa ambos marcadores.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int goles1 = Integer.parseInt(strGoles1);
                        int goles2 = Integer.parseInt(strGoles2);

                        String idPronosticoGenerado = java.util.UUID.randomUUID().toString();

                        String idParticipante = getIntent().getStringExtra("ID_USUARIO");
                        if (idParticipante == null) {
                            idParticipante = getIntent().getStringExtra("NOMBRE_COMPLETO");
                        }

                        try {
                            String faseSegura = p.getFase().toUpperCase().replace(" ", "_");
                            FaseTorneo faseEnum = FaseTorneo.valueOf(faseSegura);

                            Pronostico nuevoPronostico = new Pronostico(
                                    idPronosticoGenerado,
                                    p.getIdPartido(),
                                    idParticipante,
                                    faseEnum,
                                    goles1,
                                    goles2,
                                    0
                            );

                            boolean exito = ManejoArchivos.guardarOReemplazarPronostico(PronosticosActivity.this, nuevoPronostico);

                            if (exito) {
                                Toast.makeText(PronosticosActivity.this, "¡Pronóstico guardado exitosamente!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PronosticosActivity.this, "Error al guardar. Revisa Logcat.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IllegalArgumentException e) {
                            Toast.makeText(PronosticosActivity.this, "Error: La fase del partido no es válida.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else if (estadoActual.equals("CERRADO")) {
                tvEstado.setBackgroundColor(android.graphics.Color.parseColor("#FEF9C3"));
                tvEstado.setTextColor(android.graphics.Color.parseColor("#CA8A04"));

                etGoles1.setEnabled(false);
                etGoles2.setEnabled(false);

                btnGuardar.setVisibility(View.GONE);
                if(bannerCerrado != null) bannerCerrado.setVisibility(View.VISIBLE);
                if(bannerFinalizado != null) bannerFinalizado.setVisibility(View.GONE);
            }
            else if (estadoActual.equals("FINALIZADO")) {
                tvEstado.setBackgroundColor(android.graphics.Color.parseColor("#F1F5F9"));
                tvEstado.setTextColor(android.graphics.Color.parseColor("#475569"));

                etGoles1.setEnabled(false);
                etGoles2.setEnabled(false);

                btnGuardar.setVisibility(View.GONE);
                if(bannerCerrado != null) bannerCerrado.setVisibility(View.GONE);
                if(bannerFinalizado != null) bannerFinalizado.setVisibility(View.VISIBLE);

                etGoles1.setText(String.valueOf(p.getGoles1()));
                etGoles2.setText(String.valueOf(p.getGoles2()));
            }

            contenedorPartidos.addView(tarjetaPartido);
        }
    }
}