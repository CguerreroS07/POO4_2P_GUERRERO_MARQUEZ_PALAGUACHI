package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Modelo.ManejoArchivos;
import Modelo.Participante;
import Modelo.Usuario;

/**
 * Actividad que presenta el ranking de los participantes ordenados por su puntaje acumulado de forma descendente.
 */
public class TablaPosiciones extends AppCompatActivity {

    /** Tabla de interfaz donde se listan los participantes y sus puntos. */
    private TableLayout tlTablaPosiciones;
    /** Botón para regresar al menú principal. */
    private Button btnVolver;
    /** Encabezado que muestra el nombre del usuario activo. */
    private TextView tvNombreHeader;
    /** Encabezado que muestra el tipo de usuario activo. */
    private TextView tvTipoHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_posiciones);

        tvNombreHeader = findViewById(R.id.tvNombreUsuarioHeader);
        tvTipoHeader = findViewById(R.id.tvTipoUsuarioHeader);
        tlTablaPosiciones = findViewById(R.id.tlTablaPosiciones);
        btnVolver = findViewById(R.id.btnVolver);

        String nombre = getIntent().getStringExtra("NOMBRE_COMPLETO");
        String tipo = getIntent().getStringExtra("TIPO_USUARIO");

        if (nombre != null) tvNombreHeader.setText(nombre);
        if (tipo != null) tvTipoHeader.setText(tipo);

        btnVolver.setOnClickListener(v -> finish());

        cargarTablaPosiciones();
    }

    private void cargarTablaPosiciones() {
        List<Usuario> usuarios = ManejoArchivos.leerUsuarios(this);
        List<Participante> participantes = new ArrayList<>();

        for (Usuario u : usuarios) {
            if (u instanceof Participante) {
                participantes.add((Participante) u);
            }
        }

        Collections.sort(participantes);

        for (int i = 0; i < participantes.size(); i++) {
            Participante p = participantes.get(i);

            TableRow fila = new TableRow(this);
            fila.setPadding(0, 14, 0, 14);
            fila.setBackgroundColor(Color.WHITE);

            TextView tvPos = new TextView(this);
            tvPos.setText(String.valueOf(i + 1));
            tvPos.setTextColor(Color.parseColor("#1E293B"));
            tvPos.setTextSize(13);
            tvPos.setGravity(Gravity.CENTER);

            TextView tvNombre = new TextView(this);
            tvNombre.setText(p.getNombreCompleto());
            tvNombre.setTextColor(Color.parseColor("#0F172A"));
            tvNombre.setTextSize(13);
            tvNombre.setTypeface(null, Typeface.BOLD);
            tvNombre.setPadding(16, 0, 0, 0);

            TextView tvPuntos = new TextView(this);
            tvPuntos.setText(String.valueOf(p.getPuntajeAcumulado()));
            tvPuntos.setTextColor(Color.parseColor("#0F172A"));
            tvPuntos.setTextSize(13);
            tvPuntos.setTypeface(null, Typeface.BOLD);
            tvPuntos.setGravity(Gravity.CENTER);

            fila.addView(tvPos);
            fila.addView(tvNombre);
            fila.addView(tvPuntos);

            tlTablaPosiciones.addView(fila);

            if (i < participantes.size() - 1) {
                View divisor = new View(this);
                divisor.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, 1));
                divisor.setBackgroundColor(Color.parseColor("#E2E8F0"));
                tlTablaPosiciones.addView(divisor);
            }
        }
    }
}