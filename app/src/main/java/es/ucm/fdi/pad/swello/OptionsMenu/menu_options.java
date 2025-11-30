package es.ucm.fdi.pad.swello.OptionsMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import java.util.Locale;

import es.ucm.fdi.pad.swello.MainActivity;
import es.ucm.fdi.pad.swello.R;

public class menu_options extends AppCompatActivity {

    private MaterialToolbar toolbarOptions;
    private RecyclerView recyclerOptions;
    private OptionsAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("language", "es");
        super.attachBaseContext(applyLocale(newBase, lang));
    }

    private Context applyLocale(Context base, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration(base.getResources().getConfiguration());
        config.setLocale(locale);
        config.setLayoutDirection(locale); // por si cambias a idiomas RTL

        // MUY IMPORTANTE: devolvemos un *contexto envuelto* con el locale aplicado
        return base.createConfigurationContext(config);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String size = prefs.getString("text_size", "medium");

        if (size.equals("small"))
            setTheme(R.style.Theme_Swello_Small);
        else if (size.equals("large"))
            setTheme(R.style.Theme_Swello_Large);
        else
            setTheme(R.style.Theme_Swello_Medium);

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

        List<OptionItem> options = new ArrayList<>();
        options.add(new OptionItem(getString(R.string.opcion_general), R.drawable.ic_settings, false));
        options.add(new OptionItem(getString(R.string.opcion_tam_texto), R.drawable.ic_text_size, false));
        options.add(new OptionItem(getString(R.string.opcion_informacion), R.drawable.ic_info, false));
        options.add(new OptionItem(getString(R.string.opcion_logout), R.drawable.ic_logout, false));

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
