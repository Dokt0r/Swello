package es.ucm.fdi.pad.swello.PlayaDetails;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import es.ucm.fdi.pad.swello.API_Queries.PlayaApiClient;
import es.ucm.fdi.pad.swello.R;
import es.ucm.fdi.pad.swello.PlayaAdapter.ItemPlaya;

public class PlayaDetailActivity extends AppCompatActivity {

    private static final String TAG = "PlayaDetailActivity";

    private ImageView imgHeader;
    private TextView txtNombreHeader, txtDescripcion;
    private TextView txtAlturaOla, txtPeriodoOla, txtDireccionOla, txtTempAgua;
    private TextView txtVelocidadCorriente, txtDireccionCorriente;
    private TextView txtValoracion;
    private LinearLayout galeriaContainer;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate iniciado");

        setContentView(R.layout.activity_playa_detail);

        // Inicialización con logs
        imgHeader = findAndLog(R.id.img_header, "imgHeader");
        txtNombreHeader = findAndLog(R.id.txt_nombre_header, "txtNombreHeader");
        txtDescripcion = findAndLog(R.id.txt_descripcion, "txtDescripcion");

        txtAlturaOla = findAndLog(R.id.txt_altura_ola, "txtAlturaOla");
        txtPeriodoOla = findAndLog(R.id.txt_periodo, "txtPeriodoOla");
        txtDireccionOla = findAndLog(R.id.text_direccion_ola, "txtDireccionOla");
        txtTempAgua = findAndLog(R.id.txt_temp_agua, "txtTempAgua");

        txtVelocidadCorriente = findAndLog(R.id.txt_corriente_vel, "txtVelocidadCorriente");
        txtDireccionCorriente = findAndLog(R.id.txt_corriente_dir, "txtDireccionCorriente");

        txtValoracion = findAndLog(R.id.txt_valoracion, "txtValoracion");

        galeriaContainer = findAndLog(R.id.container_galeria, "galeriaContainer");

        btnBack = findAndLog(R.id.btn_back, "btnBack");
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Obtener idPlaya
        String idPlaya = getIntent().getStringExtra("idPlaya");
        if (idPlaya != null) {
            Log.d(TAG, "Recibido idPlaya=" + idPlaya);
            cargarDatosDesdeApi(idPlaya);
        } else {
            Log.w(TAG, "No se recibió idPlaya en el intent");
        }
    }

    private <T> T findAndLog(int id, String name) {
        T view = (T) findViewById(id);
        Log.d(TAG, "findViewById(" + name + ") = " + (view != null));
        return view;
    }

    private void cargarDatosDesdeApi(String idPlaya) {
        PlayaApiClient apiClient = new PlayaApiClient(this);
        apiClient.getPlayaById(idPlaya, new PlayaApiClient.PlayaByIdListener() {
            @Override
            public void onSuccess(ItemPlaya playa) {
                Log.d(TAG, "Datos recibidos: " + playa.getNombre());

                // TEXTOS
                safeSet(txtNombreHeader, playa.getNombre());
                safeSet(txtDescripcion, playa.getDescripcion());
                safeSet(txtAlturaOla, playa.getAlturaOla() + " m");
                safeSet(txtPeriodoOla, playa.getPeriodoOla() + " s");
                safeSet(txtDireccionOla, playa.getDireccionOla());
                safeSet(txtTempAgua, playa.getTempAgua() + " °C");
                safeSet(txtVelocidadCorriente, playa.getOceanCurrentVelocity() + " m/s");
                safeSet(txtDireccionCorriente, playa.getOceanCurrentDirection());
                safeSet(txtValoracion, String.format("%.1f ⭐", playa.getValoracion()));

                cargarImagenPrincipal(playa);
                cargarGaleria(playa);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al obtener la playa", e);
            }
        });
    }

    private void safeSet(TextView tv, String text) {
        if (tv != null) tv.setText(text != null ? text : "—");
    }

    // -----------------------------
    //  IMAGEN PRINCIPAL
    // -----------------------------
    private void cargarImagenPrincipal(ItemPlaya playa) {
        if (imgHeader == null) return;

        String img = playa.getImagenPrincipal();
        Log.d(TAG, "cargarImagenPrincipal: " + img);

        // Si está vacía usar la primera imagen de la galería
        if ((img == null || img.isEmpty()) && playa.getImagenes() != null) {
            playa.getImagenes();
        }

        if (img == null || img.isEmpty()) {
            Log.e(TAG, "No hay ninguna imagen para mostrar");
            return;
        }

        img = normalizarRuta(img);

        Log.d(TAG, "Cargando imagen principal: " + img);

        Glide.with(this)
                .load(img)
                .centerCrop()
                .placeholder(R.color.dark_cool_blue)
                .error(R.color.dark_cool_blue)
                .into(imgHeader);
    }

    // -----------------------------
    //         GALERÍA
    // -----------------------------
    private void cargarGaleria(ItemPlaya playa) {
        if (galeriaContainer == null) return;
        if (playa.getImagenes() == null) return;

        galeriaContainer.removeAllViews();

        for (String rawUrl : playa.getImagenes()) {
            String clean = limpiarJsonQuotes(rawUrl);
            clean = normalizarRuta(clean);

            Log.d(TAG, "Agregando imagen galería: " + clean);

            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 400
            );
            lp.setMargins(0, 8, 0, 8);
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(this)
                    .load(clean)
                    .placeholder(R.color.dark_cool_blue)
                    .error(R.color.dark_cool_blue)
                    .into(iv);

            galeriaContainer.addView(iv);
        }
    }

    // Limpia rutas del backend como ["uploads/x.jpg"]
    private String limpiarJsonQuotes(String url) {
        if (url == null) return "";
        return url.replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .replace("\\/", "/");
    }

    // Normaliza localhost → 10.0.2.2 y mantiene /uploads/
    private String normalizarRuta(String url) {
        if (url == null) return "";

        if (url.contains("localhost"))
            url = url.replace("localhost", "10.0.2.2");

        // Si ya es URL completa, dejarla
        if (url.startsWith("http://") || url.startsWith("https://"))
            return url;

        // Si es solo nombre de archivo → usar /uploads/
        return "http://10.0.2.2:3000/uploads/" + url;
    }
}
