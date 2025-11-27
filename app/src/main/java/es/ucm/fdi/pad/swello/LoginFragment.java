package es.ucm.fdi.pad.swello;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import es.ucm.fdi.pad.swello.API_Queries.LoginApi;
import es.ucm.fdi.pad.swello.UsuarioAdapter.ItemUsuario;
import es.ucm.fdi.pad.swello.Login.TokenManager;


public class LoginFragment extends Fragment {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private Button registerButton;  // Nuevo botón de registro

    private LoginApi loginApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TokenManager tm = new TokenManager(requireContext());
        String savedToken = tm.getToken();

        if (savedToken != null && false) {
            // comprobar si el token sigue siendo válido
            loginApi.getUsuarioData(savedToken, new LoginApi.UsuarioCallback() {
                @Override
                public void onSuccess(ItemUsuario usuario) {
                    // El token es válido → saltar login
                    ((MainActivity) requireActivity()).onLoginSuccess(usuario);
                }

                @Override
                public void onError(String error) {
                    // Token inválido → seguir con el login
                }
            });
        }


        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameField = view.findViewById(R.id.edit_user);
        passwordField = view.findViewById(R.id.edit_pass);
        loginButton = view.findViewById(R.id.btn_login);
        registerButton = view.findViewById(R.id.btn_register); // Asumimos que agregaste el botón en el XML

        loginApi = new LoginApi(getContext());

        // --- Login ---
        loginButton.setOnClickListener(v -> {
            String user = usernameField.getText().toString().trim();
            String pass = passwordField.getText().toString().trim();

            loginApi.loginUser(user, pass, new LoginApi.LoginCallback() {
                @Override
                public void onSuccess(String token) {
                    new TokenManager(requireContext()).saveToken(token);
                    loginApi.getUsuarioData(token, new LoginApi.UsuarioCallback() {
                        @Override
                        public void onSuccess(ItemUsuario usuario) {
                            ((MainActivity) requireActivity()).onLoginSuccess(usuario);
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), "Error al obtener datos: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), "Error en login: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // --- Registro ---
        registerButton.setOnClickListener(v -> {
            // Mostrar el fragmento de registro
            ((MainActivity) requireActivity()).showFragment(new RegisterFragment(), true);
        });

        return view;
    }
}
