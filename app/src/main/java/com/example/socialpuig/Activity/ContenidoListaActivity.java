package com.example.socialpuig.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialpuig.Adapter.PopularAdapter;
import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityContenidoListaBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContenidoListaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PopularAdapter productosAdapter;
    private DatabaseReference mDatabase;

    ActivityContenidoListaBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_lista);

        // Obtener el título de la lista enviado desde la actividad anterior
        String tituloLista = getIntent().getStringExtra("tituloLista");

        // Mostrar el título de la lista en un TextView
        TextView textViewTituloLista = findViewById(R.id.textViewTituloLista);
        textViewTituloLista.setText(tituloLista);

        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la referencia a la base de datos Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Recuperar y mostrar los productos asociados a la lista seleccionada
        getProductos(tituloLista);
    }

    private void getProductos(String tituloLista) {
        // Obtener el usuario actualmente autenticado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            // Utilizar userId en lugar de getUid() en tu consulta a Firebase
            Query query = mDatabase.child("Users").child(userId).child("Listas").orderByChild("nombre").equalTo(tituloLista);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Si la lista existe, recuperar los productos asociados a ella
                        for (DataSnapshot listaSnapshot : dataSnapshot.getChildren()) {
                            // Obtener la lista de productos de la instantánea de datos
                            Iterable<DataSnapshot> productosSnapshots = listaSnapshot.child("Productos").getChildren();
                            ArrayList<ItemsDomain> productos = new ArrayList<>();
                            for (DataSnapshot productoSnapshot : productosSnapshots) {
                                // Obtener cada producto y agregarlo a la lista
                                ItemsDomain producto = productoSnapshot.getValue(ItemsDomain.class);
                                productos.add(producto);
                            }
                            // Mostrar los productos en el RecyclerView
                            mostrarProductos(productos);
                        }
                    } else {
                        // Si la lista no existe o no tiene productos asociados, mostrar un mensaje
                        Toast.makeText(ContenidoListaActivity.this, "La lista no tiene productos asociados", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar cualquier error en la consulta a la base de datos
                    Toast.makeText(ContenidoListaActivity.this, "Error al recuperar los productos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Manejar el caso en que el usuario no esté autenticado
            // Por ejemplo, mostrar un mensaje o redirigir a la pantalla de inicio de sesión
        }
    }


    private void mostrarProductos(ArrayList<ItemsDomain> productos) {
        // Configurar el adaptador del RecyclerView con la lista de productos
        productosAdapter = new PopularAdapter(productos);
        recyclerView.setAdapter(productosAdapter);
    }
}