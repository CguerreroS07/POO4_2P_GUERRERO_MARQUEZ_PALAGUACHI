package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MenuInicioSesion extends AppCompatActivity {

    private EditText edtUsuario, edtContrasena;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio_sesion);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtContrasena = findViewById(R.id.edtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        btnIniciarSesion.setOnClickListener(v -> {
            try {
                validarCredenciales(edtUsuario.getText().toString(), edtContrasena.getText().toString());
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            } catch (CredencialesInvalidasException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validarCredenciales(String user, String pass) throws CredencialesInvalidasException {
        boolean encontrado = false;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("usuarios.txt")));
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[1].equals(user) && datos[2].equals(pass)) {
                    encontrado = true;
                    break;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!encontrado) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos.");
        }
    }
}