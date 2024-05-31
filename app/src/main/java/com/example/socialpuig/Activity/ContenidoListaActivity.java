package com.example.socialpuig.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private String tituloLista;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_lista);

        // Obtener el título de la lista enviado desde la actividad anterior
        tituloLista = getIntent().getStringExtra("tituloLista");

        // Mostrar el título de la lista en un TextView
        TextView textViewTituloLista = findViewById(R.id.textViewTituloLista);
        textViewTituloLista.setText(tituloLista);

        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        ImageView deletePostImageView = findViewById(R.id.deletePostImageView);
        deletePostImageView.setOnClickListener(v -> eliminarLista());

        // Recuperar y mostrar los productos asociados a la lista seleccionada
        getProductos(tituloLista);
    }

    private void getProductos(String tituloLista) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Query query = mDatabase.child("Users").child(userId).child("Listas").orderByChild("nombre").equalTo(tituloLista);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Si la lista existe, recuperar los productos asociados a ella
                        for (DataSnapshot listaSnapshot : dataSnapshot.getChildren()) {
                            Iterable<DataSnapshot> productosSnapshots = listaSnapshot.child("Productos").getChildren();
                            ArrayList<ItemsDomain> productos = new ArrayList<>();
                            for (DataSnapshot productoSnapshot : productosSnapshots) {
                                // Obtener cada producto y agregarlo a la lista
                                ItemsDomain producto = productoSnapshot.getValue(ItemsDomain.class);
                                productos.add(producto);
                            }
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

        }
    }


    private void mostrarProductos(ArrayList<ItemsDomain> productos) {
        // Configurar el adaptador del RecyclerView con la lista de productos
        productosAdapter = new PopularAdapter(productos);
        recyclerView.setAdapter(productosAdapter);
    }

    private void eliminarLista() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            mDatabase.child("Users").child(userId).child("Listas").child(tituloLista).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // Eliminación exitosa, mostrar mensaje y volver a la pantalla anterior
                        Toast.makeText(ContenidoListaActivity.this, "Lista eliminada exitosamente", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    })
                    .addOnFailureListener(e -> {
                        // Error al eliminar la lista, mostrar mensaje de error
                        Toast.makeText(ContenidoListaActivity.this, "Error al eliminar la lista: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onBackPressed() {
        // Lógica para volver a la pantalla anterior
        super.onBackPressed();
    }
}