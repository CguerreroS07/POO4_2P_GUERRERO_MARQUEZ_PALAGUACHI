package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class AdministrarPartidosActivity extends AppCompatActivity {

    private Spinner spinnerFasesAdmin;
    private LinearLayout contenedorPartidosAdmin;
    private Button btnVolverAdmin;

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
        btnVolverAdmin.setOnClickListener(v -> finish());
    }
}