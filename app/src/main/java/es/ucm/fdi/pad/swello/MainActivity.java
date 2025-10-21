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
    private List<String> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchInput = findViewById(R.id.search_input);
        btnFilter = findViewById(R.id.btn_filter);
        recyclerResults = findViewById(R.id.recycler_results);

        // Datos de ejemplo
        allItems = new ArrayList<>();
        allItems.add("Café");
        allItems.add("Capuchino");
        allItems.add("Chocolate");
        allItems.add("Té verde");
        allItems.add("Latte");
        allItems.add("Espresso");

        adapter = new SimpleAdapter(allItems);
        recyclerResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerResults.setAdapter(adapter);

        // 🔹 Filtro de texto
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

        // 🔹 Botón de filtro (a definir su acción)
        btnFilter.setOnClickListener(v ->
                // Aquí podrías abrir un diálogo de filtros o menú lateral
                System.out.println("Filtro presionado")
        );
    }

    private void filterResults(String query) {
        List<String> filtered = new ArrayList<>();
        for (String item : allItems) {
            if (item.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        adapter.updateList(filtered);
    }
}
