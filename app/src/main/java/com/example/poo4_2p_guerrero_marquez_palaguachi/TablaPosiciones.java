package com.example.poo4_2p_guerrero_marquez_palaguachi;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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

public class TablaPosiciones extends AppCompatActivity {

    private TableLayout tlTablaPosiciones;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_posiciones);

        tlTablaPosiciones = findViewById(R.id.tlTablaPosiciones);
        btnVolver = findViewById(R.id.btnVolver);

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
            fila.setPadding(0, 16, 0, 16);

            if (i % 2 == 0) {
                fila.setBackgroundColor(Color.parseColor("#171E30"));
            } else {
                fila.setBackgroundColor(Color.parseColor("#121829"));
            }

            TextView tvPos = new TextView(this);
            tvPos.setText(String.valueOf(i + 1));
            tvPos.setTextColor(Color.WHITE);
            tvPos.setTextSize(14);
            tvPos.setPadding(24, 0, 24, 0);

            TextView tvNombre = new TextView(this);
            tvNombre.setText(p.getNombreCompleto());
            tvNombre.setTextColor(Color.WHITE);
            tvNombre.setTextSize(14);
            tvNombre.setPadding(24, 0, 24, 0);

            TextView tvPuntos = new TextView(this);
            tvPuntos.setText(String.valueOf(p.getPuntajeAcumulado()));
            tvPuntos.setTextColor(Color.WHITE);
            tvPuntos.setTextSize(14);
            tvPuntos.setGravity(Gravity.END);
            tvPuntos.setPadding(24, 0, 24, 0);

            fila.addView(tvPos);
            fila.addView(tvNombre);
            fila.addView(tvPuntos);

            tlTablaPosiciones.addView(fila);
        }
    }
}