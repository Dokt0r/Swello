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
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String size = prefs.getString("text_size", "medium");

        if (size.equals("small"))
            setTheme(R.style.Theme_Swello_Small);
        else if (size.equals("large"))
            setTheme(R.style.Theme_Swello_Large);
        else
            setTheme(R.style.Theme_Swello_Medium);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Al iniciar, mostramos el LoginFragment
        if (savedInstanceState == null) {
            showFragment(new LoginFragment(), false);
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
        // Puedes pasar datos al fragment usando Bundle
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario", usuario);
        fragment.setArguments(bundle);

        // Mostrar el fragment
        showFragment(fragment, false);
    }
}
