package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialpuig.Adapter.PopularAdapter;
import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityPedidosDentroBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidosDentroActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PopularAdapter productosAdapter;
    private DatabaseReference mDatabase;
    private String tituloLista;
    private TextView textViewTotalCompra;
    ActivityPedidosDentroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_dentro);

        // Obtener el título de la lista enviado desde la actividad anterior
        tituloLista = getIntent().getStringExtra("tituloLista");

        // Mostrar el título de la lista en un TextView
        TextView textViewTituloLista = findViewById(R.id.textViewTituloLista);
        textViewTituloLista.setText(tituloLista);

        textViewTotalCompra = findViewById(R.id.textViewTotalCompra);

        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // Inicializar la referencia a la base de datos Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        // Recuperar y mostrar los productos asociados a la lista seleccionada
        getProductos(tituloLista);
    }

    private void getProductos(String cartId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference cartRef = mDatabase.child("Users").child(userId).child("Cart").child(cartId);
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Si el carrito existe, recuperar los productos asociados a él
                        ArrayList<ItemsDomain> productos = new ArrayList<>();
                        double totalCompra = 0.0;
                        for (DataSnapshot productoSnapshot : dataSnapshot.getChildren()) {
                            if (productoSnapshot.getKey().equals("TotalCompra")) {
                                totalCompra = productoSnapshot.getValue(Double.class);
                            } else if (productoSnapshot.getValue() instanceof Map) {
                                ItemsDomain producto = productoSnapshot.getValue(ItemsDomain.class);
                                productos.add(producto);
                            }
                        }
                        // Mostrar los productos en el RecyclerView
                        mostrarProductos(productos);
                        // Mostrar el total de la compra
                        textViewTotalCompra.setText(String.format("Total: %.2f€", totalCompra));
                    } else {
                        // Si el carrito no existe o no tiene productos asociados, mostrar un mensaje
                        Toast.makeText(PedidosDentroActivity.this, "El carrito no existe o no tiene productos asociados", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar cualquier error en la consulta a la base de datos
                    Toast.makeText(PedidosDentroActivity.this, "Error al recuperar los productos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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