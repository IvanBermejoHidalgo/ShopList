package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.socialpuig.Adapter.ListasAdapter;
import com.example.socialpuig.Domain.ListaDomain;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityListasBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListasActivity extends BaseActivity {
    ActivityListasBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button btnCrearLista;
    private ListasAdapter listasAdapter;
    private ArrayList<String> listaTitulos;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Inicializar RecyclerView
        recyclerView = binding.productosView;

        // Configurar el botón para crear una nueva lista
        btnCrearLista = binding.btnCrearLista;
        btnCrearLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción cuando se hace clic en el botón para crear una nueva lista
                crearNuevaLista();
            }
        });

        cargarTitulosListas();
    }

    private void cargarTitulosListas() {
        listaTitulos = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Query query = mDatabase.child("Users").child(userId).child("Listas");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaTitulos.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Verifica si el valor es de tipo String
                        if (snapshot.getValue() instanceof String) {
                            String titulo = (String) snapshot.getValue();
                            listaTitulos.add(titulo);
                        } else {
                            // Si no es de tipo String, muestra un mensaje de error o maneja el caso según sea necesario
                            Log.e("ListasActivity", "El valor no es de tipo String");
                        }
                    }
                    mostrarTitulosListas();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListasActivity.this, "Error al cargar los títulos de las listas: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void mostrarTitulosListas() {
        ArrayList<ListaDomain> listaDeListas = new ArrayList<>();
        for (String titulo : listaTitulos) {
            ListaDomain lista = new ListaDomain(titulo);
            listaDeListas.add(lista);
        }

        listasAdapter = new ListasAdapter(listaDeListas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listasAdapter);

        listasAdapter.setOnItemClickListener(new ListasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String tituloLista = listaTitulos.get(position);
                Toast.makeText(ListasActivity.this, "Has seleccionado la lista: " + tituloLista, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearNuevaLista() {
        Intent intent = new Intent(ListasActivity.this, NuevaListaActivity.class);
        startActivity(intent);
    }
}
