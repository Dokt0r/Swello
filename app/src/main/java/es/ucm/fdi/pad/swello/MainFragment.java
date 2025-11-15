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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.pad.swello.API_Queries.PlayaApiClient;
import es.ucm.fdi.pad.swello.Filtros.Filtro;
import es.ucm.fdi.pad.swello.Filtros.FiltroData;
import es.ucm.fdi.pad.swello.Location.LocationPermissions;
import es.ucm.fdi.pad.swello.Location.UserLocation;
import es.ucm.fdi.pad.swello.OptionsMenu.menu_options;
import es.ucm.fdi.pad.swello.PlayaAdapter.PlayaAdapter;

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

    private List<ItemPlaya> allItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        try {
            // --- Inicialización de vistas ---
            searchInput = view.findViewById(R.id.search_input);
            btnFilter = view.findViewById(R.id.btn_filter);
            btnSearch = view.findViewById(R.id.btn_search);
            recyclerResults = view.findViewById(R.id.recycler_results);
            topAppBar = view.findViewById(R.id.topAppBar);

            // --- Inicialización de filtros por defecto ---
            filtroDialog.inicializarValoresPorDefecto();
            Log.d(TAG, "Filtros inicializados con valores por defecto: " + currentFilters);

            // --- Inicialización API ---
            playaApiClient = new PlayaApiClient(requireContext());

            // --- Inicialización de LocationPermissions ---
            locationPermissions = new LocationPermissions(requireActivity());
            locationPermissions.requestLocationPermission(new LocationPermissions.LocationPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Log.d(TAG, "Permisos de ubicación concedidos");
                    UserLocation.init(requireContext()); // inicializamos UserLocation solo si hay permisos
                }

                @Override
                public void onPermissionDenied() {
                    Log.w(TAG, "Permisos de ubicación denegados. Búsqueda por distancia no funcionará.");
                }
            });

            // --- Configuración RecyclerView ---
            adapter = new PlayaAdapter(new ArrayList<>());
            recyclerResults.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerResults.setAdapter(adapter);

            // --- Filtrado local en tiempo real ---
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterResults(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });

            // --- Botón de filtro ---
            btnFilter.setOnClickListener(v -> {
                Log.d(TAG, "Abriendo diálogo de filtros...");

                filtroDialog.setOnFiltroAplicadoListener(filtros -> {
                    currentFilters = filtros;
                    Log.d(TAG, "Filtros aplicados: " + filtros);
                });

                filtroDialog.show(getParentFragmentManager(), "FiltroDialog");
            });

            // --- Botón de búsqueda ---
            btnSearch.setOnClickListener(v -> {
                String query = searchInput.getText().toString().trim();
                Log.d(TAG, "Botón de búsqueda pulsado. Query: " + query);

                // Verificar que UserLocation ya esté inicializado
                if (UserLocation.isInitialized()) {
                    UserLocation.getInstance().actualizarUbicacion();
                } else {
                    Log.w(TAG, "UserLocation no inicializado. Se realizará búsqueda sin coordenadas.");
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

        } catch (Exception e) {
            Log.e(TAG, "Error inicializando MainFragment", e);
        }

        return view;
    }


    // --- Llamada HTTP GET a la API ---
    private void fetchPlayasFromApi(String query, FiltroData filtros) {
        playaApiClient.fetchPlayas(query, filtros, new PlayaApiClient.PlayaApiListener() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    List<ItemPlaya> playas = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);

                        String nombre = obj.optString("nombre", "Sin nombre");
                        double altura = obj.optDouble("alturaOla", 0.0);
                        String direccion = obj.optString("direccionOla", "");
                        double distancia = obj.optDouble("distancia", 0.0);
                        String descripcion = obj.optString("descripcion", "");

                        playas.add(new ItemPlaya(nombre, altura, direccion, distancia, descripcion));
                    }

                    allItems = playas;
                    adapter.updateList(playas);
                    Log.d(TAG, "Recibidas " + playas.size() + " playas de la API");
                } catch (JSONException e) {
                    Log.e(TAG, "Error parseando respuesta JSON", e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al obtener playas de la API", e);
            }
        });
    }

    // --- Filtrado local mientras se escribe ---
    private void filterResults(String query) {
        if (query == null || query.isEmpty() || allItems.isEmpty()) {
            adapter.updateList(new ArrayList<>(allItems));
            return;
        }

        List<ItemPlaya> filtered = new ArrayList<>();
        for (ItemPlaya item : allItems) {
            if (item.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                    item.getDescripcion().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }

        adapter.updateList(filtered);
    }
}
