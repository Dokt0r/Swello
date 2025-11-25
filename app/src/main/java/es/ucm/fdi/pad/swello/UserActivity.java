package es.ucm.fdi.pad.swello;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import es.ucm.fdi.pad.swello.UsuarioAdapter.ItemUsuario;

public class UserActivity extends AppCompatActivity {

    private TextView nombreTextView;
    private TextView emailTextView;
    private TextView fechaNacimientoTextView;
    private ImageView fotoPerfilImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);  // Asegúrate de tener este layout

        // Obtener el ItemUsuario desde el Intent
        ItemUsuario usuario = (ItemUsuario) getIntent().getSerializableExtra("usuario");

        if (usuario != null) {
            // Iniciar las vistas
            nombreTextView = findViewById(R.id.nombre_usuario);
            emailTextView = findViewById(R.id.email_usuario);
            fechaNacimientoTextView = findViewById(R.id.fecha_nacimiento_usuario);
            fotoPerfilImageView = findViewById(R.id.foto_perfil_usuario);

            // Mostrar los datos del usuario
            nombreTextView.setText(usuario.getNombre());
            emailTextView.setText(usuario.getEmail());
            fechaNacimientoTextView.setText(usuario.getFechaNacimiento());

            // Cargar la foto de perfil usando Glide
            if (usuario.getFotoPerfilUrl() != null && !usuario.getFotoPerfilUrl().isEmpty()) {
                Glide.with(this)
                        .load(usuario.getFotoPerfilUrl())
                        .into(fotoPerfilImageView);
            } else {
                // Imagen por defecto si no tiene foto
                fotoPerfilImageView.setImageResource(R.drawable.ic_default_profile_picture);
            }
        } else {
            // Mostrar un mensaje si no se pudo obtener el usuario
            Toast.makeText(this, "No se pudo cargar el usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
