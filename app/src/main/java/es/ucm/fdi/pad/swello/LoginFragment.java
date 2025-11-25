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

public class LoginFragment extends Fragment {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;

    private LoginApi loginApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameField = view.findViewById(R.id.edit_user);
        passwordField = view.findViewById(R.id.edit_pass);
        loginButton = view.findViewById(R.id.btn_login);

        loginApi = new LoginApi(getContext());

        loginButton.setOnClickListener(v -> {
            String user = usernameField.getText().toString().trim();
            String pass = passwordField.getText().toString().trim();

            // Llamada a la API para hacer login
            loginApi.loginUser(user, pass, new LoginApi.LoginCallback() {
                @Override
                public void onSuccess(String token) {
                    // Después del login exitoso, obtener los datos del usuario
                    loginApi.getUsuarioData(token, new LoginApi.UsuarioCallback() {
                        @Override
                        public void onSuccess(ItemUsuario usuario) {
                            // Aquí pasas los datos del usuario a la siguiente actividad
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

        return view;
    }
}
