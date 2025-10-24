package es.ucm.fdi.pad.swello;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    // Llamado desde LoginFragment cuando el usuario inicia sesión correctamente
    public void onLoginSuccess() {
        showFragment(new MainFragment(), false);
    }
}
