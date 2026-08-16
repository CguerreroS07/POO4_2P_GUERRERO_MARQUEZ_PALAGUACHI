package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuParticipanteActivity extends AppCompatActivity {

    // 1. Declaramos las dos nuevas variables
    private TextView tvNombreHeader, tvTipoHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_participante);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Enlazamos con los IDs del nuevo encabezado
        tvNombreHeader = findViewById(R.id.tvNombreUsuarioHeader);
        tvTipoHeader = findViewById(R.id.tvTipoUsuarioHeader);

        // 3. Capturamos los datos enviados desde LoginActivity
        String nombre = getIntent().getStringExtra("NOMBRE_COMPLETO");
        String tipo = getIntent().getStringExtra("TIPO_USUARIO");

        // 4. Asignamos los textos si no son nulos
        if (nombre != null) tvNombreHeader.setText(nombre);
        if (tipo != null) tvTipoHeader.setText(tipo);
    }


    public void abrirTablaPosiciones(View view) {
        Intent intent = new Intent(this, TablaPosiciones.class);

        String nombre = getIntent().getStringExtra("NOMBRE_COMPLETO");
        String tipo = getIntent().getStringExtra("TIPO_USUARIO");

        intent.putExtra("NOMBRE_COMPLETO", nombre);
        intent.putExtra("TIPO_USUARIO", tipo);

        startActivity(intent);
    }

    public void abrirPronosticos(View view) {
        // Intent intent = new Intent(this, PronosticosActivity.class);
        // startActivity(intent);
    }

    public void abrirMisPronosticos(View view) {
        // Intent intent = new Intent(this, MisPronosticosActivity.class);
        // startActivity(intent);
    }

    public void salir(View view) {
        finishAffinity();
    }
}