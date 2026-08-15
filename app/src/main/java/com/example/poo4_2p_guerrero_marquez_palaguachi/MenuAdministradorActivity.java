package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuAdministradorActivity extends AppCompatActivity {

    private TextView txtBienvenidaAdmin;
    private Button btnAdministrarPartidos, btnActualizarPuntajes, btnSalirAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_administrador);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtBienvenidaAdmin = findViewById(R.id.txtBienvenidaAdmin);
        btnAdministrarPartidos = findViewById(R.id.btnAdministrarPartidos);
        btnActualizarPuntajes = findViewById(R.id.btnActualizarPuntajes);
        btnSalirAdmin = findViewById(R.id.btnSalirAdmin);

        String nombreAdmin = getIntent().getStringExtra("NOMBRE_USUARIO");
        if (nombreAdmin != null) {
            txtBienvenidaAdmin.setText("Bienvenido(a)\n" + nombreAdmin);
        }

        btnSalirAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        btnAdministrarPartidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btnActualizarPuntajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}