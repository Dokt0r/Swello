package es.ucm.fdi.pad.swello;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private AppCompatEditText searchInput;
    private ImageButton btnFilter;
    private RecyclerView recyclerResults;
    private SimpleAdapter adapter;
    private List<ItemData> allItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Referencias a los componentes
        searchInput = view.findViewById(R.id.search_input);
        btnFilter = view.findViewById(R.id.btn_filter);
        recyclerResults = view.findViewById(R.id.recycler_results);

        // Datos de ejemplo
        allItems.add(new ItemData("Elemento 1", "Descripción del elemento 1"));
        allItems.add(new ItemData("Elemento 2", "Otra descripción"));
        allItems.add(new ItemData("Elemento 3", "Más texto de prueba"));
        allItems.add(new ItemData("Swello", "Tu asistente inteligente"));

        // Configuración del RecyclerView
        adapter = new SimpleAdapter(new ArrayList<>(allItems));
        recyclerResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerResults.setAdapter(adapter);

        // Filtro de texto en tiempo real
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

        // Acción del botón de filtro
        btnFilter.setOnClickListener(v -> {
            // Aquí puedes abrir un diálogo o menú lateral de filtros
            System.out.println("Filtro presionado");
        });

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
}
