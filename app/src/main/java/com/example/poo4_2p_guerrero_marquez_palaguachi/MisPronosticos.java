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

/**
 * Actividad que muestra un resumen de todos los pronósticos registrados por el participante.
 * Indica el estado del partido y los puntos obtenidos si el encuentro ya ha finalizado.
 */
public class MisPronosticos extends AppCompatActivity {

    /** Contenedor de la lista de pronósticos del usuario. */
    private LinearLayout containerPronosticos;
    /** Botón para regresar al menú anterior. */
    private View btnVolver;
    /** ID del usuario que ha iniciado sesión. */
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

        idUsuario = getIntent().getStringExtra("ID_USUARIO");
        if (idUsuario == null) {
            idUsuario = getIntent().getStringExtra("NOMBRE_COMPLETO");
        }

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

        List<Partido> partidos = ManejoArchivos.leerPartidos(this);
        List<Pronostico> misPronosticos = deserializarPronosticos();

        if (misPronosticos.isEmpty()) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("No tienes pronósticos registrados aún.");
            tvVacio.setPadding(32, 32, 32, 32);
            tvVacio.setTextSize(14f);
            containerPronosticos.addView(tvVacio);
            return;
        }

        for (Pronostico pron : misPronosticos) {
            if (idUsuario != null && !idUsuario.equals(pron.getIdParticipante())) {
                continue;
            }

            Partido partidoAsociado = buscarPartidoPorId(partidos, Integer.parseInt(pron.getIdPartido()));
            if (partidoAsociado != null) {
                if (partidoAsociado.getEstado().name().equals("FINALIZADO")) {
                    pron.calcularPuntos(partidoAsociado.getGoles1(), partidoAsociado.getGoles2());
                }
                agregarTarjetaPronostico(pron, partidoAsociado);
            }
        }
    }

    private void agregarTarjetaPronostico(Pronostico pron, Partido partido) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_pronostico, containerPronosticos, false);

        TextView tvFase = cardView.findViewById(R.id.tvFase);
        androidx.cardview.widget.CardView cardEstadoBadge = cardView.findViewById(R.id.cardEstadoBadge);
        TextView tvEstadoBadge = cardView.findViewById(R.id.tvEstadoBadge);
        TextView tvFecha = cardView.findViewById(R.id.tvFecha);
        TextView tvHora = cardView.findViewById(R.id.tvHora);
        TextView tvEstadio = cardView.findViewById(R.id.tvEstadio);
        TextView tvNombreSeleccion1 = cardView.findViewById(R.id.tvNombreSeleccion1);
        TextView tvNombreSeleccion2 = cardView.findViewById(R.id.tvNombreSeleccion2);
        ImageView imgBandera1 = cardView.findViewById(R.id.imgBandera1);
        ImageView imgBandera2 = cardView.findViewById(R.id.imgBandera2);
        TextView tvGoles1 = cardView.findViewById(R.id.tvGoles1);
        TextView tvGoles2 = cardView.findViewById(R.id.tvGoles2);
        TextView tvResultadoOficial = cardView.findViewById(R.id.tvResultadoOficial);
        TextView tvPuntosObtenidos = cardView.findViewById(R.id.tvPuntosObtenidos);
        androidx.cardview.widget.CardView cardBanner = cardView.findViewById(R.id.cardBanner);
        TextView tvMensajeBanner = cardView.findViewById(R.id.tvMensajeBanner);
        View layoutOficial = cardView.findViewById(R.id.layoutOficial);
        View dividerResultados = cardView.findViewById(R.id.dividerResultados);

        tvFase.setText(partido.getFase());

        String estadioOriginal = partido.getEstadio();
        String estadioLimpio = estadioOriginal;
        int inicioParentesis = estadioOriginal.indexOf("(");
        int finParentesis = estadioOriginal.indexOf(")");

        if (inicioParentesis != -1 && finParentesis != -1 && finParentesis > inicioParentesis) {
            estadioLimpio = estadioOriginal.substring(inicioParentesis + 1, finParentesis);
        }

        tvFecha.setText(partido.getFecha());
        tvHora.setText(partido.getHora());
        tvEstadio.setText(estadioLimpio);

        tvNombreSeleccion1.setText(partido.getSeleccion1());
        tvNombreSeleccion2.setText(partido.getSeleccion2());
        imgBandera1.setImageDrawable(obtenerBanderaDesdeAssets(partido.getSeleccion1()));
        imgBandera2.setImageDrawable(obtenerBanderaDesdeAssets(partido.getSeleccion2()));

        tvGoles1.setText(String.valueOf(pron.getGolesSeleccion1()));
        tvGoles2.setText(String.valueOf(pron.getGolesSeleccion2()));

        String estadoPartido = partido.getEstado().name();

        if (estadoPartido.equals("FINALIZADO")) {
            tvEstadoBadge.setText("FINALIZADO");
            cardEstadoBadge.setCardBackgroundColor(Color.parseColor("#DCFCE7"));
            tvEstadoBadge.setTextColor(Color.parseColor("#16A34A"));

            layoutOficial.setVisibility(View.VISIBLE);
            dividerResultados.setVisibility(View.VISIBLE);

            String resOficial = String.format(Locale.getDefault(), "%d - %d", partido.getGoles1(), partido.getGoles2());
            tvResultadoOficial.setText(resOficial);

            int puntos = pron.getPuntosObtenidos();
            tvPuntosObtenidos.setText(puntos + " pt" + (puntos != 1 ? "s" : ""));

            if (puntos == 3) {
                tvMensajeBanner.setText("✔ ¡Acertaste el marcador exacto! Obtuviste 3 puntos.");
                cardBanner.setCardBackgroundColor(Color.parseColor("#DCFCE7"));
                tvMensajeBanner.setTextColor(Color.parseColor("#16A34A"));
            } else if (puntos == 2) {
                tvMensajeBanner.setText("✔ ¡Acertaste la diferencia / empate! Obtuviste 2 puntos.");
                cardBanner.setCardBackgroundColor(Color.parseColor("#DCFCE7"));
                tvMensajeBanner.setTextColor(Color.parseColor("#16A34A"));
            } else if (puntos == 1) {
                tvMensajeBanner.setText("✔ ¡Acertaste el ganador! Obtuviste 1 punto.");
                cardBanner.setCardBackgroundColor(Color.parseColor("#DCFCE7"));
                tvMensajeBanner.setTextColor(Color.parseColor("#16A34A"));
            } else {
                tvMensajeBanner.setText("❌ No acertaste. Obtuviste 0 puntos.");
                cardBanner.setCardBackgroundColor(Color.parseColor("#FEE2E2")); // Rojo claro
                tvMensajeBanner.setTextColor(Color.parseColor("#991B1B"));
            }

        } else if (estadoPartido.equals("CERRADO")) {
            tvEstadoBadge.setText("CERRADO");
            cardEstadoBadge.setCardBackgroundColor(Color.parseColor("#FEF9C3"));
            tvEstadoBadge.setTextColor(Color.parseColor("#CA8A04"));

            layoutOficial.setVisibility(View.GONE);
            dividerResultados.setVisibility(View.GONE);

            tvMensajeBanner.setText("🔒 Los pronósticos para este partido están cerrados.");
            cardBanner.setCardBackgroundColor(Color.parseColor("#FEF9C3"));
            tvMensajeBanner.setTextColor(Color.parseColor("#CA8A04"));

        } else {
            tvEstadoBadge.setText("ABIERTO");
            cardEstadoBadge.setCardBackgroundColor(Color.parseColor("#E0F2FE"));
            tvEstadoBadge.setTextColor(Color.parseColor("#0369A1"));

            layoutOficial.setVisibility(View.GONE);
            dividerResultados.setVisibility(View.GONE);

            tvMensajeBanner.setText("✏️ Puedes modificar tu pronóstico mientras esté abierto.");
            cardBanner.setCardBackgroundColor(Color.parseColor("#F1F5F9"));
            tvMensajeBanner.setTextColor(Color.parseColor("#475569"));
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
        List<Pronostico> listaUnificada = new ArrayList<>();
        File directorio = getFilesDir();
        File[] archivos = directorio.listFiles();

        if (archivos == null || idUsuario == null) {
            return listaUnificada;
        }

        String prefijoUsuario = "pronostico_" + idUsuario + "_";

        for (File archivo : archivos) {
            if (archivo.getName().startsWith(prefijoUsuario) && archivo.getName().endsWith(".dat")) {
                try (FileInputStream fis = new FileInputStream(archivo);
                     ObjectInputStream ois = new ObjectInputStream(fis)) {

                    Object obj = ois.readObject();
                    if (obj instanceof List) {
                        listaUnificada.addAll((List<Pronostico>) obj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return listaUnificada;
    }

    private android.graphics.drawable.Drawable obtenerBanderaDesdeAssets(String pais) {
        String codigoArchivo = "";

        switch (pais) {
            case "Alemania": codigoArchivo = "de"; break;
            case "Arabia Saudita": codigoArchivo = "sa"; break;
            case "Argelia": codigoArchivo = "dz"; break;
            case "Argentina": codigoArchivo = "ar"; break;
            case "Australia": codigoArchivo = "au"; break;
            case "Austria": codigoArchivo = "at"; break;
            case "Bélgica": codigoArchivo = "be"; break;
            case "Bosnia y Herzegovina": codigoArchivo = "ba"; break;
            case "Brasil": codigoArchivo = "br"; break;
            case "Cabo Verde": codigoArchivo = "cv"; break;
            case "Canadá": codigoArchivo = "ca"; break;
            case "Catar": codigoArchivo = "qa"; break;
            case "Chequia": codigoArchivo = "cz"; break;
            case "Colombia": codigoArchivo = "co"; break;
            case "Corea del Sur": codigoArchivo = "kr"; break;
            case "Costa de Marfil": codigoArchivo = "ci"; break;
            case "Croacia": codigoArchivo = "hr"; break;
            case "Curazao": codigoArchivo = "cw"; break;
            case "Ecuador": codigoArchivo = "ec"; break;
            case "Egipto": codigoArchivo = "eg"; break;
            case "Escocia": codigoArchivo = "gb_sct"; break;
            case "España": codigoArchivo = "es"; break;
            case "Estados Unidos": codigoArchivo = "us"; break;
            case "Francia": codigoArchivo = "fr"; break;
            case "Ghana": codigoArchivo = "gh"; break;
            case "Haití": codigoArchivo = "ht"; break;
            case "Inglaterra": codigoArchivo = "gb_eng"; break;
            case "Irak": codigoArchivo = "iq"; break;
            case "Irán": codigoArchivo = "ir"; break;
            case "Japón": codigoArchivo = "jp"; break;
            case "Jordania": codigoArchivo = "jo"; break;
            case "Marruecos": codigoArchivo = "ma"; break;
            case "México": codigoArchivo = "mx"; break;
            case "Noruega": codigoArchivo = "no"; break;
            case "Nueva Zelanda": codigoArchivo = "nz"; break;
            case "Países Bajos": codigoArchivo = "nl"; break;
            case "Panamá": codigoArchivo = "pa"; break;
            case "Paraguay": codigoArchivo = "py"; break;
            case "Portugal": codigoArchivo = "pt"; break;
            case "República Democrática del Congo": codigoArchivo = "cd"; break;
            case "Senegal": codigoArchivo = "sn"; break;
            case "Sudáfrica": codigoArchivo = "za"; break;
            case "Suecia": codigoArchivo = "se"; break;
            case "Suiza": codigoArchivo = "ch"; break;
            case "Túnez": codigoArchivo = "tn"; break;
            case "Turquía": codigoArchivo = "tr"; break;
            case "Uruguay": codigoArchivo = "uy"; break;
            case "Uzbekistán": codigoArchivo = "uz"; break;
            default: codigoArchivo = ""; break;
        }

        try {
            java.io.InputStream inputStream = getAssets().open("banderas/" + codigoArchivo + ".png");
            return android.graphics.drawable.Drawable.createFromStream(inputStream, null);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return getResources().getDrawable(android.R.drawable.ic_menu_camera, null);
        }
    }
}