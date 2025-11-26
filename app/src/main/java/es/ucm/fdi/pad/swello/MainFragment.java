package es.ucm.fdi.pad.swello;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import es.ucm.fdi.pad.swello.API_Queries.PlayaApiClient;
import es.ucm.fdi.pad.swello.Filtros.Filtro;
import es.ucm.fdi.pad.swello.Filtros.FiltroData;
import es.ucm.fdi.pad.swello.Location.LocationPermissions;
import es.ucm.fdi.pad.swello.Location.UserLocation;
import es.ucm.fdi.pad.swello.OptionsMenu.menu_options;
import es.ucm.fdi.pad.swello.PlayaAdapter.ItemPlaya;
import es.ucm.fdi.pad.swello.PlayaAdapter.PlayaAdapter;
import es.ucm.fdi.pad.swello.PlayaDetails.PlayaDetailActivity;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private AppCompatEditText searchInput;
    private ImageButton btnFilter;
    private MaterialButton btnSearch;
    private RecyclerView recyclerResults;
    private MaterialToolbar topAppBar;

    private PlayaAdapter adapter;
    private PlayaApiClient playaApiClient;
    private FiltroData currentFilters = new FiltroData();
    private Filtro filtroDialog = new Filtro();
    private LocationPermissions locationPermissions;
    private ShimmerFrameLayout shimmerLoader;


    private List<ItemPlaya> allItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // --- Inicialización de vistas ---
        searchInput = view.findViewById(R.id.search_input);
        btnFilter = view.findViewById(R.id.btn_filter);
        btnSearch = view.findViewById(R.id.btn_search);
        recyclerResults = view.findViewById(R.id.recycler_results);
        topAppBar = view.findViewById(R.id.top_bar);
        shimmerLoader = view.findViewById(R.id.shimmer_loader);


        // --- Inicialización de filtros por defecto ---
        filtroDialog.inicializarValoresPorDefecto();
        Log.d(TAG, "Filtros inicializados con valores por defecto: " + currentFilters);

        // --- Inicialización API ---
        playaApiClient = new PlayaApiClient(requireContext());

        // --- Inicialización LocationPermissions ---
        locationPermissions = new LocationPermissions(requireActivity());
        locationPermissions.requestLocationPermission(new LocationPermissions.LocationPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.d(TAG, "Permisos de ubicación concedidos");

                // Inicializamos UserLocation y pedimos ubicación
                UserLocation.init(requireContext()).setLocationReadyListener((lat, lon) -> {
                    Log.d(TAG, "Ubicación disponible, cargando playas con lat=" + lat + " lon=" + lon);
                    fetchPlayasFromApi("", currentFilters);
                });
                UserLocation.getInstance().actualizarUbicacion();
            }

            @Override
            public void onPermissionDenied() {
                Log.w(TAG, "Permisos de ubicación denegados. Búsqueda por distancia no funcionará.");
            }
        });

        // --- Configuración RecyclerView ---
        adapter = new PlayaAdapter(new ArrayList<>());
        adapter.setOnPlayaClickListener(playa -> {
            Log.d(TAG, "Fragment recibió click: " + playa.getNombre() + " (ID: " + playa.getId() + ")");

            //crear intent para la nueva activity
            Intent intent = new Intent(requireContext(), PlayaDetailActivity.class);

            //pasa los datos a la nueva activity
            intent.putExtra("id", playa.getId());
            intent.putExtra("nombre", playa.getNombre());
            intent.putExtra("alturaOla", playa.getAlturaOla());
            intent.putExtra("direccionOla", playa.getDireccionOla());
            intent.putExtra("distancia", playa.getDistancia());
            intent.putExtra("imagenUrl", playa.getImagenUrl());

            //empezar nueva activity
            startActivity(intent);
        });

        recyclerResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerResults.setAdapter(adapter);

        // --- Filtrado local en tiempo real ---
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterResults(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // --- Botón de filtro ---
        btnFilter.setOnClickListener(v -> {
            Log.d(TAG, "Abriendo diálogo de filtros...");

            filtroDialog.setOnFiltroAplicadoListener(filtros -> {
                currentFilters = filtros;
                Log.d(TAG, "Filtros aplicados: " + filtros);

                String query = searchInput.getText().toString().trim();
                if (UserLocation.isInitialized()) {
                    UserLocation.getInstance().actualizarUbicacion();
                }

                fetchPlayasFromApi(query, currentFilters);

                if (filtros.ordenacion != null && !filtros.ordenacion.isEmpty()) {
                    aplicarOrdenacion(filtros.ordenacion);
                }
            });
            filtroDialog.show(getParentFragmentManager(), "FiltroDialog");
        });

        // --- Botón de búsqueda ---
        btnSearch.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();

            if (UserLocation.isInitialized()) {
                UserLocation.getInstance().actualizarUbicacion();
            }

            fetchPlayasFromApi(query, currentFilters);
        });

        // --- Menú superior (opciones) ---
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_options) {
                startActivity(new Intent(requireContext(), menu_options.class));
                return true;
            }
            return false;
        });

        return view;
    }

    // para aplicar ordenación
    private void aplicarOrdenacion(String tipoOrdenacion) {
        if (allItems.isEmpty()) {
            Log.w(TAG, "No hay playas para ordenar");
            return;
        }

        List<ItemPlaya> ordenadas = new ArrayList<>(allItems);
        switch (tipoOrdenacion) {
            case "distancia":
                Collections.sort(ordenadas, (p1, p2) -> Double.compare(p1.getDistancia(), p2.getDistancia()));
                Log.d(TAG, "Playas ordenadas por distancia");
                break;
            case "nombre":
                Collections.sort(ordenadas, (p1, p2) -> p1.getNombre().compareToIgnoreCase(p2.getNombre()));
                Log.d(TAG, "Playas ordenadas por nombre");
                break;
            case "popularidad":
                // Por ahora, orden alfabético como placeholder
                Collections.sort(ordenadas, (p1, p2) -> p1.getNombre().compareToIgnoreCase(p2.getNombre()));
                Log.d(TAG, "Playas ordenadas por popularidad (placeholder)");
                break;
            case "valoración":
                // Por ahora, orden alfabético como placeholder
                Collections.sort(ordenadas, (p1, p2) -> p1.getNombre().compareToIgnoreCase(p2.getNombre()));
                Log.d(TAG, "Playas ordenadas por valoración (placeholder)");
                break;
        }
        adapter.updateList(ordenadas);
    }
    // --- Llamada HTTP GET a la API ---
    private void fetchPlayasFromApi(String query, FiltroData filtros) {

        // Activar shimmer
        shimmerLoader.setVisibility(View.VISIBLE);
        shimmerLoader.startShimmer();
        recyclerResults.setVisibility(View.GONE);

        playaApiClient.fetchPlayas(query, filtros, new PlayaApiClient.PlayaApiListener() {
            @Override
            public void onSuccess(List<ItemPlaya> playas) {
                allItems = playas;
                adapter.updateList(playas);
                Log.d(TAG, "Recibidas " + playas.size() + " playas de la API");

                // Desactivar shimmer
                shimmerLoader.stopShimmer();
                shimmerLoader.setVisibility(View.GONE);
                recyclerResults.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error obteniendo playas", e);

                // Desactivar shimmer también en error
                shimmerLoader.stopShimmer();
                shimmerLoader.setVisibility(View.GONE);
                recyclerResults.setVisibility(View.VISIBLE);
            }
        });
    }

    // --- Filtrado local ---
    private void filterResults(String query) {
        if (query == null || query.isEmpty()) {
            adapter.updateList(new ArrayList<>(allItems));
            return;
        }

        List<ItemPlaya> filtered = new ArrayList<>();
        for (ItemPlaya item : allItems) {
            if (item.getNombre().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }

        adapter.updateList(filtered);
    }
}
