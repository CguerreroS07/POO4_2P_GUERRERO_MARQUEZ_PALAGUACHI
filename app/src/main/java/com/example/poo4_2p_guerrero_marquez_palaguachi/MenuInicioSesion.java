package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import Modelo.ManejoArchivos;
import Modelo.Usuario;

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
            } catch (CredencialesInvalidasException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validarCredenciales(String user, String pass) throws CredencialesInvalidasException {
        boolean encontrado = false;

        List<Usuario> usuarios = ManejoArchivos.leerUsuarios(this);

        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equals(user) && u.getContrasena().equals(pass)) {
                encontrado = true;

                Intent intent;

                if (u.getTipoUsuario().equalsIgnoreCase("ADMINISTRADOR")) {
                    intent = new Intent(MenuInicioSesion.this, MenuAdministradorActivity.class);
                } else {
                    intent = new Intent(MenuInicioSesion.this, MenuParticipanteActivity.class);
                }

                intent.putExtra("NOMBRE_COMPLETO", u.getNombreCompleto());
                intent.putExtra("TIPO_USUARIO", u.getTipoUsuario());

                startActivity(intent);
                finish();
                break;
            }
        }
        if (!encontrado) {
            throw new CredencialesInvalidasException("Usuario o contraseña son incorrectos.");
        }
    }
}