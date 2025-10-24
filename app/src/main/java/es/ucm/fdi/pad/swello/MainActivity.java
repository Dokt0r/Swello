package es.ucm.fdi.pad.swello;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.AppCompatEditText;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppCompatEditText searchInput;
    private ImageButton btnFilter;
    private RecyclerView recyclerResults;
    private SimpleAdapter adapter;
    private List<ItemData> allItems = new ArrayList<>(); // 🔹 Inicializada aquí

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchInput = findViewById(R.id.search_input);
        btnFilter = findViewById(R.id.btn_filter);
        recyclerResults = findViewById(R.id.recycler_results);

        // 🔹 Datos de ejemplo
        allItems.add(new ItemData("Elemento 1", "Descripción del elemento 1"));
        allItems.add(new ItemData("Elemento 2", "Otra descripción"));
        allItems.add(new ItemData("Elemento 3", "Más texto de prueba"));
        allItems.add(new ItemData("Swello", "Tu asistente inteligente"));

        // 🔹 Configuración del RecyclerView
        adapter = new SimpleAdapter(new ArrayList<>(allItems));
        recyclerResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerResults.setAdapter(adapter);

        // 🔹 Filtro de texto en tiempo real
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

        // 🔹 Acción del botón de filtro
        btnFilter.setOnClickListener(v -> {
            // Aquí puedes abrir un diálogo o fragmento con filtros avanzados
            System.out.println("Filtro presionado");
        });
    }

    // 🔹 Función de filtrado
    private void filterResults(String query) {
        if (query == null || query.trim().isEmpty()) {
            // Si no hay texto, mostramos todos
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
