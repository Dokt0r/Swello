package es.ucm.fdi.pad.swello;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

public class MainActivity extends AppCompatActivity {

    private SearchBar searchBar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.search_bar);
        searchView = findViewById(R.id.search_view);

        // Conecta el SearchView con el SearchBar (animaciones automáticas)
        searchView.setupWithSearchBar(searchBar);

        // Ejemplo: manejar el texto
        searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            searchBar.setText(searchView.getText());
            searchView.hide();
            return false;
        });
    }
}
