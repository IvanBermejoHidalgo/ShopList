package com.example.socialpuig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.socialpuig.Activity.ContenidoListaActivity;
import com.example.socialpuig.Adapter.ListasAdapter;
import com.example.socialpuig.Adapter.PopularAdapter;
import com.example.socialpuig.Adapter.ProductosAdapter;
import com.example.socialpuig.Domain.ItemsDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PedidosDentroActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListasAdapter adapter;
    private List<ItemsDomain> productosList;
    private DatabaseReference mDatabase;
    private PopularAdapter productosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_dentro);


        recyclerView = findViewById(R.id.productosRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la referencia a la base de datos Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getProductos();
/*
        productosList = new ArrayList<>();
        adapter = new ListasAdapter(productosList);
        recyclerView.setAdapter(adapter);

        String cartId = getIntent().getStringExtra("cartId");

        DatabaseReference productosRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Cart")
                .child(cartId);

        productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productoSnapshot : dataSnapshot.getChildren()) {
                    ItemsDomain producto = productoSnapshot.getValue(ItemsDomain.class);
                    productosList.add(producto);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error
            }
        });
        */

    }



    private void getProductos() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(userId)
                    .child("Cart");

            userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Si existen compras, recuperar los productos de cada una
                        for (DataSnapshot compraSnapshot : dataSnapshot.getChildren()) {
                            // Obtener los productos de la compra actual
                            ArrayList<ItemsDomain> productos = new ArrayList<>();
                            for (DataSnapshot productoSnapshot : compraSnapshot.getChildren()) {
                                // Obtener cada producto y agregarlo a la lista
                                ItemsDomain producto = productoSnapshot.getValue(ItemsDomain.class);
                                productos.add(producto);
                            }
                            // Mostrar los productos de la compra en el RecyclerView
                            mostrarProductos(productos);
                        }
                    } else {
                        // Si el usuario no tiene compras, mostrar un mensaje
                        Toast.makeText(PedidosDentroActivity.this, "No se encontraron productos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar cualquier error en la consulta a la base de datos
                    Toast.makeText(PedidosDentroActivity.this, "Error al recuperar los productos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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