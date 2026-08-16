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

public class MenuParticipanteActivity extends AppCompatActivity {

    private TextView txtBienvenida;
    private Button btnTabla, btnPronosticos, btnMisPronosticos, btnSalir;

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

        txtBienvenida = findViewById(R.id.txtBienvenida);
        btnTabla = findViewById(R.id.btnTabla);
        btnPronosticos = findViewById(R.id.btnPronosticos);
        btnMisPronosticos = findViewById(R.id.btnMisPronosticos);
        btnSalir = findViewById(R.id.btnSalir);

        String nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");
        if(nombreUsuario != null) {
            txtBienvenida.setText("Bienvenido(a)\n" + nombreUsuario);
        }

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        btnTabla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuParticipanteActivity.this, TablaPosiciones.class);

                startActivity(intent);
            }
        });

        btnPronosticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btnMisPronosticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}