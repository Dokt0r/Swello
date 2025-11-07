package es.ucm.fdi.pad.swello.OptionsMenu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.pad.swello.R;

public class menu_options extends AppCompatActivity {

    private MaterialToolbar toolbarOptions;
    private RecyclerView recyclerOptions;
    private OptionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options);

        MaterialToolbar toolbarOptions = findViewById(R.id.toolbar_options);
        RecyclerView recyclerOptions = findViewById(R.id.recycler_options);

        setSupportActionBar(toolbarOptions);

        // Configurar el icono de navegación para volver atrás
        toolbarOptions.setNavigationOnClickListener(v -> finish());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Datos de ejemplo
        List<OptionItem> options = new ArrayList<>();
        options.add(new OptionItem("General", R.drawable.ic_settings));
        options.add(new OptionItem("Notificaciones", R.drawable.ic_notifications));
        options.add(new OptionItem("Información", R.drawable.ic_info));
        /*options.add(new OptionItem("Cuenta", R.drawable.ic_account));
        options.add(new OptionItem("Cerrar sesión", R.drawable.ic_logout));
    */
        // Adapter
        adapter = new OptionsAdapter(options);
        recyclerOptions.setLayoutManager(new LinearLayoutManager(this));
        recyclerOptions.setAdapter(adapter);
    }
}
