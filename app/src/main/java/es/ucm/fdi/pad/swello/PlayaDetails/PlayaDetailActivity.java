package es.ucm.fdi.pad.swello.PlayaDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.pad.swello.R;

public class PlayaDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playa_detail);

        //recupera el intento que abrió esta actividad
        Intent intent = getIntent();

        //recupera los datos
        String id = intent.getStringExtra("id");
        String nombre = intent.getStringExtra("nombre");
        double alturaOla = intent.getDoubleExtra("alturaOla", 0.0);
        String direccionOla = intent.getStringExtra("direccionOla");
        double distancia = intent.getDoubleExtra("distancia", 0.0);
        String imagenUrl = intent.getStringExtra("imagenUrl");


        //SOLO PER LA VERIFICA
        Log.d("PlayaDetailActivity", "ID: " + id);
        Log.d("PlayaDetailActivity", "Nombre: " + nombre);
        Log.d("PlayaDetailActivity", "Altura Ola: " + alturaOla);
        Log.d("PlayaDetailActivity", "Direccion Ola: " + direccionOla);
        Log.d("PlayaDetailActivity", "Distancia: " + distancia);
        Log.d("PlayaDetailActivity", "Imagen URL: " + imagenUrl);


        //manejar el boton para volver a la pantalla anterior
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() { //cuando clicas sobre el boton
            @Override
            public void onClick(View v) {
                //volver a la pagina anterior
                finish();
            }
        });
    }
}