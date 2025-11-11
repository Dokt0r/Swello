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

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.pad.swello.Filtros.Filtro;
import es.ucm.fdi.pad.swello.Filtros.FiltroData;
import es.ucm.fdi.pad.swello.OptionsMenu.menu_options;
import es.ucm.fdi.pad.swello.PlayaAdapter.PlayaAdapter;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private AppCompatEditText searchInput;
    private ImageButton btnFilter;
    private RecyclerView recyclerResults;
    private PlayaAdapter adapter;
    private MaterialToolbar topAppBar;
    private List<ItemPlaya> allItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        try {
            searchInput = view.findViewById(R.id.search_input);
            btnFilter = view.findViewById(R.id.btn_filter);
            recyclerResults = view.findViewById(R.id.recycler_results);
            topAppBar = view.findViewById(R.id.topAppBar);

            // --- Datos de prueba ---
            allItems.add(new ItemPlaya("Playa de la Concha", 1.2, "NE", 5.0, "Playa urbana famosa por su arena dorada"));
            allItems.add(new ItemPlaya("Playa de Somo", 1.8, "N", 10.0, "Ideal para surfistas de nivel medio"));
            allItems.add(new ItemPlaya("Playa de Zarautz", 2.5, "O", 12.0, "Playa larga con buenas olas para surf"));
            allItems.add(new ItemPlaya("Playa de Rodiles", 1.0, "SO", 15.0, "Playa tranquila con oleaje suave"));
            allItems.add(new ItemPlaya("Playa de Mundaka", 3.0, "N", 20.0, "Reconocida por su ola izquierda de clase mundial"));

            // --- Configuración del RecyclerView ---
            adapter = new PlayaAdapter(new ArrayList<>(allItems));
            recyclerResults.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerResults.setAdapter(adapter);

            // --- Filtro de texto en tiempo real ---
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        filterResults(s.toString());
                    } catch (Exception e) {
                        Log.e(TAG, "Error filtrando resultados", e);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });

            // --- Acción del botón de filtro ---
            btnFilter.setOnClickListener(v -> {
                try {
                    Log.d(TAG, "Abrir filtros");
                    Filtro filtroDialog = new Filtro();
                    filtroDialog.setOnFiltroAplicadoListener(filtros -> {
                        Log.d(TAG, "Recibidos filtros: " + filtros);

                        // Aquí podrías usar los filtros en tu query
                        String query = buildQueryWithFilters(searchInput.getText().toString(), filtros);
                        Log.d(TAG, "Query generada: " + query);

                        // Filtrado local basado en criterios simples (ejemplo)
                        filterWithFilters(filtros);
                    });

                    filtroDialog.show(getParentFragmentManager(), "FiltroDialog");

                } catch (Exception e) {
                    Log.e(TAG, "Error al abrir FiltroDialog", e);
                }
            });

            // --- Manejar click del item de menu "Opciones" ---
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

    // --- Filtrado por texto ---
    private void filterResults(String query) {
        if (query == null || query.trim().isEmpty()) {
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

    // --- Filtrado local simple por filtros ---
    private void filterWithFilters(FiltroData filtros) {
        List<ItemPlaya> filtered = new ArrayList<>();

        for (ItemPlaya item : allItems) {
            boolean include = true;

            // Distancia
            if (!(filtros.distanciaMinima <= 0f && filtros.distanciaMaxima >= 200f)) {
                if (item.getDistancia() < filtros.distanciaMinima || item.getDistancia() > filtros.distanciaMaxima) {
                    include = false;
                }
            }

            // Altura de ola
            if (!(filtros.tamanoMinimo <= 0.5f && filtros.tamanoMaximo >= 5.0f)) {
                if (item.getAlturaOla() < filtros.tamanoMinimo || item.getAlturaOla() > filtros.tamanoMaximo) {
                    include = false;
                }
            }

            // Dirección
            if (filtros.direccionOlas != null && !filtros.direccionOlas.isEmpty()) {
                if (!item.getDireccionOla().equalsIgnoreCase(filtros.direccionOlas)) {
                    include = false;
                }
            }

            if (include) filtered.add(item);
        }

        adapter.updateList(filtered);
    }

    // --- Generación de query (ejemplo para SQL o API) ---
    private String buildQueryWithFilters(String baseQuery, FiltroData filtros) {
        StringBuilder query = new StringBuilder("SELECT * FROM playas WHERE 1=1");

        if (baseQuery != null && !baseQuery.trim().isEmpty()) {
            query.append(" AND nombre LIKE '%").append(baseQuery.trim()).append("%'");
        }

        if (!(filtros.distanciaMinima <= 0f && filtros.distanciaMaxima >= 200f)) {
            query.append(" AND distancia_km BETWEEN ")
                    .append(filtros.distanciaMinima).append(" AND ").append(filtros.distanciaMaxima);
        }

        if (!(filtros.tamanoMinimo <= 0.5f && filtros.tamanoMaximo >= 5.0f)) {
            query.append(" AND altura_olas BETWEEN ")
                    .append(filtros.tamanoMinimo).append(" AND ").append(filtros.tamanoMaximo);
        }

        if (!(filtros.periodoMinimo <= 5f && filtros.periodoMaximo >= 12f)) {
            query.append(" AND periodo_olas BETWEEN ")
                    .append(filtros.periodoMinimo).append(" AND ").append(filtros.periodoMaximo);
        }

        if (filtros.direccionOlas != null && !filtros.direccionOlas.isEmpty()) {
            query.append(" AND direccion_olas = '").append(filtros.direccionOlas).append("'");
        }

        if (filtros.tempAgua != null && !filtros.tempAgua.isEmpty()) {
            query.append(" AND temperatura_agua = '").append(filtros.tempAgua).append("'");
        }

        if (filtros.nivelSurfista != null && !filtros.nivelSurfista.isEmpty()) {
            query.append(" AND nivel_surfista = '").append(filtros.nivelSurfista).append("'");
        }

        if (filtros.servicios != null && !filtros.servicios.isEmpty()) {
            query.append(" AND (");
            for (int i = 0; i < filtros.servicios.size(); i++) {
                query.append("servicios LIKE '%").append(filtros.servicios.get(i)).append("%'");
                if (i < filtros.servicios.size() - 1) query.append(" OR ");
            }
            query.append(")");
        }

        Log.d(TAG, "Query generada: " + query);
        return query.toString();
    }
}
