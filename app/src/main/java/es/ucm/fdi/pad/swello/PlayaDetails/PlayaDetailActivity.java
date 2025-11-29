package es.ucm.fdi.pad.swello.PlayaDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.pad.swello.API_Queries.PlayaApiClient;
import es.ucm.fdi.pad.swello.R;
import es.ucm.fdi.pad.swello.PlayaAdapter.ItemPlaya;

public class PlayaDetailActivity extends AppCompatActivity {

    private static final String TAG = "PlayaDetailActivity";

    // MAIN VIEWS
    private TextView txtNombreHeader, txtDescripcion;
    private TextView txtAlturaOla, txtPeriodoOla, txtDireccionOla, txtTempAgua;
    private TextView txtVelocidadCorriente, txtDireccionCorriente;
    private TextView txtValoracion;
    private ImageButton btnBack;

    // NEW WEATHER VIEWS
    private TextView txtWeatherWaveHeight, txtWeatherWavePeriod, txtWeatherWaterTemp, txtWeatherWaveDirection;

    // NEW GALLERY VIEWS
    private ViewPager2 viewPagerImages;
    private LinearLayout layoutDots;
    private RecyclerView recyclerGaleria;

    // ADAPTERS
    private ImagePagerAdapter imagePagerAdapter;
    private GalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate iniciado");

        setContentView(R.layout.activity_playa_detail);

        // Initialize all views with logging
        initializeViews();

        // Setup back button
        btnBack.setOnClickListener(v -> finish());

        // Get playa ID from intent
        String idPlaya = getIntent().getStringExtra("idPlaya");
        if (idPlaya != null) {
            Log.d(TAG, "Recibido idPlaya=" + idPlaya);
            cargarDatosDesdeApi(idPlaya);
        } else {
            Log.w(TAG, "No se recibió idPlaya en el intent");
        }
    }

    private void initializeViews() {
        Log.d(TAG, "Initializing views...");

        // Main views
        txtNombreHeader = findAndLog(R.id.txt_nombre_header, "txtNombreHeader");
        txtDescripcion = findAndLog(R.id.txt_descripcion, "txtDescripcion");

        txtAlturaOla = findAndLog(R.id.txt_altura_ola, "txtAlturaOla");
        txtPeriodoOla = findAndLog(R.id.txt_periodo, "txtPeriodoOla");
        txtDireccionOla = findAndLog(R.id.text_direccion_ola, "txtDireccionOla");
        txtTempAgua = findAndLog(R.id.txt_temp_agua, "txtTempAgua");

        txtVelocidadCorriente = findAndLog(R.id.txt_corriente_vel, "txtVelocidadCorriente");
        txtDireccionCorriente = findAndLog(R.id.txt_corriente_dir, "txtDireccionCorriente");

        txtValoracion = findAndLog(R.id.txt_valoracion, "txtValoracion");

        btnBack = findAndLog(R.id.btn_back, "btnBack");

        // NEW: Weather views
        txtWeatherWaveHeight = findAndLog(R.id.txt_weather_wave_height, "txtWeatherWaveHeight");
        txtWeatherWavePeriod = findAndLog(R.id.txt_weather_wave_period, "txtWeatherWavePeriod");
        txtWeatherWaterTemp = findAndLog(R.id.txt_weather_water_temp, "txtWeatherWaterTemp");
        txtWeatherWaveDirection = findAndLog(R.id.txt_weather_wave_direction, "txtWeatherWaveDirection");

        // NEW: Gallery views
        viewPagerImages = findAndLog(R.id.viewPagerImages, "viewPagerImages");
        layoutDots = findAndLog(R.id.layoutDots, "layoutDots");
        recyclerGaleria = findAndLog(R.id.recycler_galeria, "recyclerGaleria");

        // Setup RecyclerView for horizontal gallery
        if (recyclerGaleria != null) {
            recyclerGaleria.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false
            ));
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
                updateUI(playa);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al obtener la playa", e);
            }
        });
    }

    private void updateUI(ItemPlaya playa) {
        // Update main text views
        safeSet(txtNombreHeader, playa.getNombre());
        safeSet(txtDescripcion, playa.getDescripcion());
        safeSet(txtAlturaOla, playa.getAlturaOla() + " m");
        safeSet(txtPeriodoOla, playa.getPeriodoOla() + " s");
        safeSet(txtDireccionOla, playa.getDireccionOla());
        safeSet(txtTempAgua, playa.getTempAgua() + " °C");
        safeSet(txtVelocidadCorriente, playa.getOceanCurrentVelocity() + " m/s");
        safeSet(txtDireccionCorriente, playa.getOceanCurrentDirection());
        safeSet(txtValoracion, String.format("%.1f ⭐", playa.getValoracion()));

        // NEW: Update weather section
        updateWeatherData(playa);

        // NEW: Update image galleries
        updateImageGalleries(playa);
    }

    // NEW: Update weather data in dedicated section
    private void updateWeatherData(ItemPlaya playa) {
        safeSet(txtWeatherWaveHeight, String.format("%.1f m", playa.getAlturaOla()));
        safeSet(txtWeatherWavePeriod, String.format("%.1f s", playa.getPeriodoOla()));
        safeSet(txtWeatherWaterTemp, String.format("%.1f °C", playa.getTempAgua()));
        safeSet(txtWeatherWaveDirection, playa.getDireccionOla());
    }

    // NEW: Completely rewritten image gallery system
    private void updateImageGalleries(ItemPlaya playa) {
        List<String> imageUrls = getCleanImageUrls(playa);

        Log.d(TAG, "Processing " + imageUrls.size() + " images for gallery");

        if (imageUrls.isEmpty()) {
            // Hide gallery sections if no images
            hideGallerySections();
            return;
        }

        // Setup ViewPager for main image gallery
        setupViewPager(imageUrls);

        // Setup horizontal gallery
        setupHorizontalGallery(imageUrls);

        // Setup dots indicator
        setupDotsIndicator(imageUrls.size());
    }

    // NEW: Extract and clean image URLs
    private List<String> getCleanImageUrls(ItemPlaya playa) {
        List<String> imageUrls = new ArrayList<>();

        // Add main image first
        String mainImage = playa.getImgPrincipal();
        if (mainImage != null && !mainImage.isEmpty()) {
            imageUrls.add(normalizarRuta(mainImage));
        }

        // Add other images from array
        if (playa.getImagenesArray() != null) {
            for (String rawUrl : playa.getImagenesArray()) {
                String cleanUrl = normalizarRuta(limpiarJsonQuotes(rawUrl));
                if (!cleanUrl.isEmpty() && !imageUrls.contains(cleanUrl)) {
                    imageUrls.add(cleanUrl);
                }
            }
        }

        return imageUrls;
    }

    // NEW: Setup ViewPager for main image display
    private void setupViewPager(List<String> imageUrls) {
        imagePagerAdapter = new ImagePagerAdapter(imageUrls);
        viewPagerImages.setAdapter(imagePagerAdapter);

        // Add page change callback for dots indicator
        viewPagerImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDotsIndicator(position);
            }
        });
    }

    // NEW: Setup horizontal gallery RecyclerView
    private void setupHorizontalGallery(List<String> imageUrls) {
        galleryAdapter = new GalleryAdapter(imageUrls, (imageUrl, position) -> {
            // When user clicks on gallery image, show it in ViewPager
            viewPagerImages.setCurrentItem(position, true);
        });
        recyclerGaleria.setAdapter(galleryAdapter);
    }

    // NEW: Setup dots indicator for ViewPager
    private void setupDotsIndicator(int count) {
        layoutDots.removeAllViews();

        if (count <= 1) {
            layoutDots.setVisibility(View.GONE);
            return;
        }

        layoutDots.setVisibility(View.VISIBLE);

        // Create dots for each image
        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.orange_circle);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    16, 16
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);

            layoutDots.addView(dot);
        }

        // Set first dot as active
        updateDotsIndicator(0);
    }

    // NEW: Update dots indicator based on current page
    private void updateDotsIndicator(int position) {
        int childCount = layoutDots.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView dot = (ImageView) layoutDots.getChildAt(i);
            if (i == position) {
                dot.setAlpha(1.0f); // Active dot
            } else {
                dot.setAlpha(0.3f); // Inactive dot
            }
        }
    }

    // NEW: Hide gallery sections when no images
    private void hideGallerySections() {
        if (viewPagerImages != null) viewPagerImages.setVisibility(View.GONE);
        if (layoutDots != null) layoutDots.setVisibility(View.GONE);
        if (recyclerGaleria != null) recyclerGaleria.setVisibility(View.GONE);
    }

    private void safeSet(TextView tv, String text) {
        if (tv != null) tv.setText(text != null ? text : "—");
    }

    // -----------------------------
    //  IMAGE URL CLEANING METHODS
    // -----------------------------

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