package com.example.poo4_2p_guerrero_marquez_palaguachi;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Modelo.ManejoArchivos;
import Modelo.Partido;
import Modelo.Pronostico;

public class MisPronosticos extends AppCompatActivity {

    private LinearLayout containerPronosticos;
    private View btnVolver;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mis_pronosticos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        containerPronosticos = findViewById(R.id.containerPronosticos);
        btnVolver = findViewById(R.id.btnVolver);

        // Obtener el idUsuario enviado desde la pantalla anterior
        idUsuario = getIntent().getStringExtra("ID_USUARIO");
        if (idUsuario == null) {
            idUsuario = getIntent().getStringExtra("NOMBRE_COMPLETO");
        }

        // Cargar y mostrar la información de mis pronósticos
        cargarMisPronosticos();

        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }
    }

    public void volverAlMenu(View view) {
        finish();
    }

    private void cargarMisPronosticos() {
        containerPronosticos.removeAllViews();

        // 1. Obtener todos los partidos almacenados
        List<Partido> partidos = ManejoArchivos.leerPartidos(this);

        // 2. Cargar todos los pronósticos guardados por el usuario
        List<Pronostico> misPronosticos = deserializarPronosticos();

        if (misPronosticos.isEmpty()) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("No tienes pronósticos registrados aún.");
            tvVacio.setPadding(32, 32, 32, 32);
            tvVacio.setTextSize(14f);
            containerPronosticos.addView(tvVacio);
            return;
        }

        // 3. Cruzar pronósticos registrados con la información del partido correspondiente
        for (Pronostico pron : misPronosticos) {
            // Filtrar únicamente los pronósticos del usuario actual
            if (idUsuario != null && !idUsuario.equals(pron.getIdParticipante())) {
                continue;
            }

            Partido partidoAsociado = buscarPartidoPorId(partidos, Integer.parseInt(pron.getIdPartido()));
            if (partidoAsociado != null) {
                agregarTarjetaPronostico(pron, partidoAsociado);
            }
        }
    }

    private void agregarTarjetaPronostico(Pronostico pron, Partido partido) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_pronostico, containerPronosticos, false);

        TextView tvFase = cardView.findViewById(R.id.tvFase);
        TextView tvEstadoBadge = cardView.findViewById(R.id.tvEstadoBadge);
        TextView tvDetallePartido = cardView.findViewById(R.id.tvDetallePartido);
        TextView tvNombreSeleccion1 = cardView.findViewById(R.id.tvNombreSeleccion1);
        TextView tvNombreSeleccion2 = cardView.findViewById(R.id.tvNombreSeleccion2);
        ImageView imgBandera1 = cardView.findViewById(R.id.imgBandera1);
        ImageView imgBandera2 = cardView.findViewById(R.id.imgBandera2);
        TextView tvGoles1 = cardView.findViewById(R.id.tvGoles1);
        TextView tvGoles2 = cardView.findViewById(R.id.tvGoles2);
        TextView tvResultadoOficial = cardView.findViewById(R.id.tvResultadoOficial);
        TextView tvPuntosObtenidos = cardView.findViewById(R.id.tvPuntosObtenidos);
        TextView tvMensajeBanner = cardView.findViewById(R.id.tvMensajeBanner);
        View layoutOficial = cardView.findViewById(R.id.layoutOficial);
        View dividerResultados = cardView.findViewById(R.id.dividerResultados);

        // Llenar información general del partido y selección
        tvFase.setText(partido.getFase());

        String detalleStr = String.format(Locale.getDefault(), "📅 %s   🕒 %s   🏟️ %s",
                partido.getFecha(), partido.getHora(), partido.getEstadio());
        tvDetallePartido.setText(detalleStr);

        tvNombreSeleccion1.setText(partido.getSeleccion1());
        tvNombreSeleccion2.setText(partido.getSeleccion2());
        imgBandera1.setImageResource(obtenerIdBandera(partido.getSeleccion1()));
        imgBandera2.setImageResource(obtenerIdBandera(partido.getSeleccion2()));

        // Goles pronosticados
        tvGoles1.setText(String.valueOf(pron.getGolesSeleccion1()));
        tvGoles2.setText(String.valueOf(pron.getGolesSeleccion2()));

        // Lógica según el estado del partido (ABIERTO, CERRADO, FINALIZADO)
        String estadoPartido = partido.getEstado().name();

        if (estadoPartido.equals("FINALIZADO")) {
            tvEstadoBadge.setText("FINALIZADO");
            tvEstadoBadge.setBackgroundColor(Color.parseColor("#E8F5E9"));
            tvEstadoBadge.setTextColor(Color.parseColor("#2E7D32"));

            if (layoutOficial != null) layoutOficial.setVisibility(View.VISIBLE);
            if (dividerResultados != null) dividerResultados.setVisibility(View.VISIBLE);

            // Formateos usando String.format para evitar advertencias de concatenación
            String resOficial = String.format(Locale.getDefault(), "%d - %d", partido.getGoles1(), partido.getGoles2());
            tvResultadoOficial.setText(resOficial);

            String ptsTxt = String.format(Locale.getDefault(), "%d pt", pron.getPuntosObtenidos());
            tvPuntosObtenidos.setText(ptsTxt);

            String msgBanner = String.format(Locale.getDefault(), "✔ ¡Partido finalizado! Obtuviste %d punto(s).", pron.getPuntosObtenidos());
            tvMensajeBanner.setText(msgBanner);

            tvMensajeBanner.setBackgroundColor(Color.parseColor("#E8F5E9"));
            tvMensajeBanner.setTextColor(Color.parseColor("#2E7D32"));

        } else if (estadoPartido.equals("CERRADO")) {
            tvEstadoBadge.setText("CERRADO");
            tvEstadoBadge.setBackgroundColor(Color.parseColor("#FEF9C3"));
            tvEstadoBadge.setTextColor(Color.parseColor("#CA8A04"));

            if (layoutOficial != null) layoutOficial.setVisibility(View.GONE);
            if (dividerResultados != null) dividerResultados.setVisibility(View.GONE);

            tvMensajeBanner.setText("🔒 Los pronósticos para este partido están cerrados.");
            tvMensajeBanner.setBackgroundColor(Color.parseColor("#FEF9C3"));
            tvMensajeBanner.setTextColor(Color.parseColor("#CA8A04"));

        } else { // ABIERTO / PENDIENTE
            tvEstadoBadge.setText("ABIERTO");
            tvEstadoBadge.setBackgroundColor(Color.parseColor("#E8F5E9"));
            tvEstadoBadge.setTextColor(Color.parseColor("#2E7D32"));

            if (layoutOficial != null) layoutOficial.setVisibility(View.GONE);
            if (dividerResultados != null) dividerResultados.setVisibility(View.GONE);

            tvMensajeBanner.setText("✏️ Puedes modificar tu pronóstico mientras el partido esté abierto.");
            tvMensajeBanner.setBackgroundColor(Color.parseColor("#E8F5E9"));
            tvMensajeBanner.setTextColor(Color.parseColor("#2E7D32"));
        }

        containerPronosticos.addView(cardView);
    }

    private Partido buscarPartidoPorId(List<Partido> partidos, int idPartido) {
        for (Partido p : partidos) {
            if (Integer.parseInt(p.getIdPartido()) == idPartido) {
                return p;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Pronostico> deserializarPronosticos() {
        List<Pronostico> lista = new ArrayList<>();
        File file = new File(getFilesDir(), "pronosticos_guardados.dat");

        if (!file.exists()) {
            return lista;
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();
            if (obj instanceof List) {
                lista = (List<Pronostico>) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    private int obtenerIdBandera(String pais) {
        String codigoArchivo;
        switch (pais) {
            case "Canadá": codigoArchivo = "ca"; break;
            case "México": codigoArchivo = "mx"; break;
            case "Alemania": codigoArchivo = "de"; break;
            case "Brasil": codigoArchivo = "br"; break;
            case "Francia": codigoArchivo = "fr"; break;
            case "España": codigoArchivo = "es"; break;
            case "Argentina": codigoArchivo = "ar"; break;
            case "Países Bajos": codigoArchivo = "nl"; break;
            case "Portugal": codigoArchivo = "pt"; break;
            case "Estados Unidos": codigoArchivo = "us"; break;
            default: codigoArchivo = "ic_menu_camera"; break;
        }

        int idImagen = getResources().getIdentifier(codigoArchivo, "drawable", getPackageName());
        return (idImagen != 0) ? idImagen : android.R.drawable.ic_menu_camera;
    }
}