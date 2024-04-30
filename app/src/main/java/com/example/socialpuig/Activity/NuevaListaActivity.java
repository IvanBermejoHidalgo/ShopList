package com.example.socialpuig.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialpuig.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NuevaListaActivity extends AppCompatActivity {
    private EditText etNombreLista;
    private Button btnGuardarLista;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_lista);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etNombreLista = findViewById(R.id.etNombreLista);
        btnGuardarLista = findViewById(R.id.btnGuardarLista);

        btnGuardarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNuevaLista();
            }
        });
    }

    private void guardarNuevaLista() {
        String nombreLista = etNombreLista.getText().toString().trim();

        if (!nombreLista.isEmpty()) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                String listaId = mDatabase.child("Users").child(userId).child("Listas").push().getKey();
                if (listaId != null) {
                    // Guardar la nueva lista en la base de datos
                    mDatabase.child("Users").child(userId).child("Listas").child(listaId).child("nombre").setValue(nombreLista);

                    Toast.makeText(NuevaListaActivity.this, "Nueva lista creada con éxito", Toast.LENGTH_SHORT).show();
                    finish(); // Finalizar la actividad después de guardar la lista
                }
            }
        } else {
            Toast.makeText(NuevaListaActivity.this, "Por favor, introduce un nombre para la lista", Toast.LENGTH_SHORT).show();
        }
    }
}
