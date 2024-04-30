package com.example.socialpuig.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialpuig.R;

public class ContenidoListaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_lista);

        // Obtener el título de la lista enviado desde la actividad anterior
        String tituloLista = getIntent().getStringExtra("tituloLista");

        // Mostrar el título de la lista en un TextView
        TextView textViewTituloLista = findViewById(R.id.textViewTituloLista);
        textViewTituloLista.setText(tituloLista);
    }
}