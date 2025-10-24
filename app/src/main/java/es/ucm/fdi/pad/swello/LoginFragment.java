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

public class LoginFragment extends Fragment {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameField = view.findViewById(R.id.edit_user);
        passwordField = view.findViewById(R.id.edit_pass);
        loginButton = view.findViewById(R.id.btn_login);

        loginButton.setOnClickListener(v -> {
            String user = usernameField.getText().toString().trim();
            String pass = passwordField.getText().toString().trim();

            if (user.equals("admin") && pass.equals("1234")) {
                // Llamamos al MainActivity para cambiar el fragment
                ((MainActivity) requireActivity()).onLoginSuccess();
            } else {
                Toast.makeText(getContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
