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

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private AppCompatEditText searchInput;
    private ImageButton btnFilter;
    private RecyclerView recyclerResults;
    private SimpleAdapter adapter;
    private MaterialToolbar topAppBar; // 🔹 Toolbar como variable privada
    private List<ItemData> allItems = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        try {
            // 🔹 Inicializar componentes
            searchInput = view.findViewById(R.id.search_input);
            btnFilter = view.findViewById(R.id.btn_filter);
            recyclerResults = view.findViewById(R.id.recycler_results);
            topAppBar = view.findViewById(R.id.topAppBar);

            // 🔹 Datos de ejemplo
            allItems.add(new ItemData("Elemento 1", "Descripción del elemento 1"));
            allItems.add(new ItemData("Elemento 2", "Otra descripción"));
            allItems.add(new ItemData("Elemento 3", "Más texto de prueba"));
            allItems.add(new ItemData("Swello", "Tu asistente inteligente"));

            // 🔹 Configuración del RecyclerView
            adapter = new SimpleAdapter(new ArrayList<>(allItems));
            recyclerResults.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerResults.setAdapter(adapter);

            // 🔹 Filtro de texto en tiempo real
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

            // 🔹 Acción del botón de filtro
            btnFilter.setOnClickListener(v -> {
                try {
                    Log.d(TAG, "Abrir filtros");
                    Filtro filtroDialog = new Filtro();
                    filtroDialog.setOnFiltroAplicadoListener(filtros -> {
                        Log.d(TAG, "Recibidos filtros: " + filtros);

                        // 🔹 Aquí podrías usar los filtros en tu query
                        String query = buildQueryWithFilters(searchInput.getText().toString(), filtros);
                        Log.d(TAG, "Query generada: " + query);

                        // Si quieres, podrías filtrar tu lista local con esos criterios
                        filterResults(query);
                    });

                    filtroDialog.show(getParentFragmentManager(), "FiltroDialog");

                } catch (Exception e) {
                    Log.e(TAG, "Error al abrir FiltroDialog", e);
                }
            });

            // 🔹 Manejar click del item de menú "Opciones"
            topAppBar = view.findViewById(R.id.top_bar); // ✅ Toolbar correcta
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

    // 🔹 Función de filtrado
    private void filterResults(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.updateList(new ArrayList<>(allItems));
            return;
        }

        List<ItemData> filtered = new ArrayList<>();
        for (ItemData item : allItems) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }

        adapter.updateList(filtered);
    }

    private String buildQueryWithFilters(String baseQuery, FiltroData filtros) {
        StringBuilder query = new StringBuilder(baseQuery.trim());

        if (!filtros.tiposOla.isEmpty()) {
            query.append(" +olas:").append(filtros.tiposOla);
        }
        query.append(" +tamanoMax:").append(filtros.tamanoMaximo);

        if (!filtros.direccionViento.isEmpty()) {
            query.append(" +viento:").append(filtros.direccionViento);
        }

        return query.toString();
    }
}
