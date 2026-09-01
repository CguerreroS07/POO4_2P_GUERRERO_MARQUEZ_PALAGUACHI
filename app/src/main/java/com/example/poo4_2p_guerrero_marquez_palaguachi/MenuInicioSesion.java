package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import Modelo.ManejoArchivos;
import Modelo.Usuario;

/**
 * Actividad principal de inicio de sesión del sistema.
 * Permite a los usuarios autenticarse y redirige al menú correspondiente según su tipo (Administrador o Participante).
 */
public class MenuInicioSesion extends AppCompatActivity {
    /** Campo de texto para el nombre de usuario. */
    private EditText edtUsuario;
    /** Campo de texto para la contraseña. */
    private EditText edtContrasena;
    /** Botón para enviar las credenciales. */
    private Button btnIniciarSesion;
    /** Icono interactivo para mostrar/ocultar la contraseña. */
    private ImageView imgTogglePassword;
    /** Estado de visibilidad de la contraseña. */
    private boolean contrasenaVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio_sesion);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtContrasena = findViewById(R.id.edtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        imgTogglePassword = findViewById(R.id.imgTogglePassword);

        imgTogglePassword.setOnClickListener(v -> {
            contrasenaVisible = !contrasenaVisible;

            if (contrasenaVisible) {
                edtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imgTogglePassword.setImageResource(R.drawable.ic_ojo);
            } else {
                edtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imgTogglePassword.setImageResource(R.drawable.ic_ojo_cerrado);
            }

            edtContrasena.setSelection(edtContrasena.getText().length());
        });

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

                intent.putExtra("ID_USUARIO", u.getIdUsuario());
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