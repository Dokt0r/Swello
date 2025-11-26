package es.ucm.fdi.pad.swello;

import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterFragment extends Fragment {

    private EditText usernameField, emailField, passwordField, birthDateField;
    private Button registerButton, backLoginButton;
    private LoginApi loginApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        loginApi = new LoginApi(getContext());

        usernameField = view.findViewById(R.id.edit_user);
        emailField = view.findViewById(R.id.edit_email);
        passwordField = view.findViewById(R.id.edit_pass);
        birthDateField = view.findViewById(R.id.edit_birthdate);
        registerButton = view.findViewById(R.id.btn_register_submit);
        backLoginButton = view.findViewById(R.id.btn_back_login);

        registerButton.setOnClickListener(v -> {
            String nombre = usernameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String fechaNacimiento = birthDateField.getText().toString().trim();

            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email)
                    || TextUtils.isEmpty(password) || TextUtils.isEmpty(fechaNacimiento)) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            loginApi.registerUser(nombre, email, password, fechaNacimiento, new LoginApi.RegisterCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    // Volver al login automáticamente
                    ((MainActivity) getActivity()).showFragment(new LoginFragment(), false);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            });

        });

        backLoginButton.setOnClickListener(v -> {
            // Volver al LoginFragment
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showFragment(new LoginFragment(), false);
            }
        });

        return view;
    }

}
