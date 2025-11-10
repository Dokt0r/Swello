package es.ucm.fdi.pad.swello.OptionsMenu;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.pad.swello.MainActivity;
import es.ucm.fdi.pad.swello.R;

public class menu_options extends AppCompatActivity {

    private MaterialToolbar toolbarOptions;
    private RecyclerView recyclerOptions;
    private OptionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options);

        toolbarOptions = findViewById(R.id.toolbar_options);
        recyclerOptions = findViewById(R.id.recycler_options);

        // Configurar Toolbar como ActionBar
        setSupportActionBar(toolbarOptions);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Datos de ejemplo
        List<OptionItem> options = new ArrayList<>();
        options.add(new OptionItem("General", R.drawable.ic_settings, false));
        options.add(new OptionItem(getString(R.string.opcion_informacion), R.drawable.ic_info, false));

        adapter = new OptionsAdapter(this, options);
        recyclerOptions.setLayoutManager(new LinearLayoutManager(this));
        recyclerOptions.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d("menu_options", getString(R.string.flecha_volver_pulsada));

            Intent intent = new Intent(menu_options.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
