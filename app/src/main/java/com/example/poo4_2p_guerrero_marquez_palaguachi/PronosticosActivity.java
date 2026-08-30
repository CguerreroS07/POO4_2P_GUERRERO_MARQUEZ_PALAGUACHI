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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import Modelo.ManejoArchivos;
import Modelo.Partido;

public class PronosticosActivity extends AppCompatActivity {

    private Spinner spFaseTorneo;
    private LinearLayout contenedorPartidos;
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

        cargarFasesSpinner();

        String nombre = getIntent().getStringExtra("NOMBRE_COMPLETO");
        String tipo = getIntent().getStringExtra("TIPO_USUARIO");

        todosLosPartidos = ManejoArchivos.leerPartidos(this);
        mostrarPartidosEnPantalla();
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
    }

    public void volverAlMenu(View view) {
        finish();
    }

    private void mostrarPartidosEnPantalla() {
        contenedorPartidos.removeAllViews();

        for (Partido p : todosLosPartidos) {
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

            ivBandera1.setImageResource(obtenerIdBandera(p.getSeleccion1()));
            ivBandera2.setImageResource(obtenerIdBandera(p.getSeleccion2()));
            tvFecha.setText(p.getFecha());
            tvHora.setText(p.getHora());
            tvEstadio.setText(p.getEstadio());
            tvEquipo1.setText(p.getSeleccion1());
            tvEquipo2.setText(p.getSeleccion2());



            String estadoActual = p.getEstado().name();
            tvEstado.setText(estadoActual);

            if (estadoActual.equals("ABIERTO")) {
                tvEstado.setBackgroundColor(android.graphics.Color.parseColor("#DCFCE7"));
                tvEstado.setTextColor(android.graphics.Color.parseColor("#16A34A"));

                etGoles1.setEnabled(true);
                etGoles2.setEnabled(true);

                btnGuardar.setVisibility(View.VISIBLE);
                bannerCerrado.setVisibility(View.GONE);
                bannerFinalizado.setVisibility(View.GONE);

                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strGoles1 = etGoles1.getText().toString().trim();
                        String strGoles2 = etGoles2.getText().toString().trim();

                        if (strGoles1.isEmpty() || strGoles2.isEmpty()) {
                            android.widget.Toast.makeText(PronosticosActivity.this, "Por favor, ingresa ambos marcadores.", android.widget.Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int goles1 = Integer.parseInt(strGoles1);
                        int goles2 = Integer.parseInt(strGoles2);

                        String idPronosticoGenerado = java.util.UUID.randomUUID().toString();

                        String idParticipante = getIntent().getStringExtra("ID_USUARIO");
                        if (idParticipante == null) {
                            idParticipante = getIntent().getStringExtra("NOMBRE_COMPLETO");
                        }

                        Modelo.FaseTorneo faseEnum = Modelo.FaseTorneo.valueOf(p.getFase().toUpperCase());

                        Modelo.Pronostico nuevoPronostico = new Modelo.Pronostico(
                                idPronosticoGenerado,
                                p.getIdPartido(),
                                idParticipante,
                                faseEnum,
                                goles1,
                                goles2,
                                0
                        );

                        Modelo.ManejoArchivos.serializarPronostico(PronosticosActivity.this, nuevoPronostico, "pronosticos_guardados.dat");
                        android.widget.Toast.makeText(PronosticosActivity.this, "¡Pronóstico guardado!", android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (estadoActual.equals("CERRADO")) {
                tvEstado.setBackgroundColor(android.graphics.Color.parseColor("#FEF9C3"));
                tvEstado.setTextColor(android.graphics.Color.parseColor("#CA8A04"));

                etGoles1.setEnabled(false);
                etGoles2.setEnabled(false);

                btnGuardar.setVisibility(View.GONE);
                bannerCerrado.setVisibility(View.VISIBLE);
                bannerFinalizado.setVisibility(View.GONE);
            }
            else if (estadoActual.equals("FINALIZADO")) {
                tvEstado.setBackgroundColor(android.graphics.Color.parseColor("#F1F5F9"));
                tvEstado.setTextColor(android.graphics.Color.parseColor("#475569"));

                etGoles1.setEnabled(false);
                etGoles2.setEnabled(false);

                btnGuardar.setVisibility(View.GONE);
                bannerCerrado.setVisibility(View.GONE);
                bannerFinalizado.setVisibility(View.VISIBLE);

                etGoles1.setText(String.valueOf(p.getGoles1()));
                etGoles2.setText(String.valueOf(p.getGoles2()));
            }

            contenedorPartidos.addView(tarjetaPartido);
        }

    }

    private int obtenerIdBandera(String pais) {
        String codigoArchivo;
        switch (pais) {
            case "Canadá": codigoArchivo = "ca"; break;
            case "México": codigoArchivo = "mx"; break;
            case "Alemania": codigoArchivo = "de"; break;
            case "Brasil": codigoArchivo = "br"; break;
            case "Francia": codigoArchivo = "fr"; break;
            default: codigoArchivo = "ic_menu_camera"; break;
        }

        int idImagen = getResources().getIdentifier(codigoArchivo, "drawable", getPackageName());

        return (idImagen != 0) ? idImagen : android.R.drawable.ic_menu_camera;
    }


}