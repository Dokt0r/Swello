package es.ucm.fdi.pad.swello;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

import es.ucm.fdi.pad.swello.UsuarioAdapter.ItemUsuario;

public class MainActivity extends AppCompatActivity {

    private void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("language", "es"); // español por defecto

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Al iniciar, mostramos el LoginFragment
        if (savedInstanceState == null) {
            showFragment(new MainFragment(), false);
        }
    }

    // Cambia el fragmento mostrado
    public void showFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Animación de transición
        transaction.setCustomAnimations(
                android.R.anim.fade_in,  // entrada
                android.R.anim.fade_out  // salida
        );

        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    // Método que recibe el usuario logueado
    // Llamado desde LoginFragment cuando el usuario inicia sesión correctamente
    public void onLoginSuccess(ItemUsuario usuario) {
        // Crear un Intent para iniciar UserActivity
        Intent intent = new Intent(MainActivity.this, UserActivity.class);

        // Pasar el objeto usuario a través del Intent (ahora que ItemUsuario es Serializable)
        intent.putExtra("usuario", usuario);

        // Iniciar UserActivity
        startActivity(intent);
    }
}
