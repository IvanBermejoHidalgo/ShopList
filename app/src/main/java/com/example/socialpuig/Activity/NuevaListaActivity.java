package com.example.socialpuig.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialpuig.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NuevaListaActivity extends AppCompatActivity {
    private EditText etNombreLista;
    private Button btnGuardarLista;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private AppBarConfiguration mAppBarConfiguration;

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

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
                String listaId = nombreLista; // Usar el nombre de la lista como el ID
                // Guardar la nueva lista en la base de datos
                mDatabase.child("Users").child(userId).child("Listas").child(listaId).child("nombre").setValue(nombreLista)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(NuevaListaActivity.this, "Nueva lista creada con éxito", Toast.LENGTH_SHORT).show();
                                finish(); // Finalizar la actividad después de guardar la lista
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NuevaListaActivity.this, "Error al guardar la lista: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(NuevaListaActivity.this, "Por favor, introduce un nombre para la lista", Toast.LENGTH_SHORT).show();
        }
    }

}
