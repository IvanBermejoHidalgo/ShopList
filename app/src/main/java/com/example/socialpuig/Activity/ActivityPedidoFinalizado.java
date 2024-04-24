package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialpuig.Adapter.CartAdapter;
import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.Helper.ManagmentCart;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityPedidoFinalizadoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityPedidoFinalizado extends AppCompatActivity {

    private ActivityPedidoFinalizadoBinding binding;
    private FirebaseAuth firebaseAuth;
    //private ArrayList<Model_Camisetas_Tienda> camisetasTiendaList;
    private ArrayList<ItemsDomain> camisetasTiendaList;
    //private Adapter_Camisetas_Carrito adapterCamisetasCarrito;
    double precioTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPedidoFinalizadoBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarCamisetasCarrito();

        binding.botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpiar el carrito localmente
                ManagmentCart managmentCart = new ManagmentCart(ActivityPedidoFinalizado.this);
                managmentCart.clearCart();

                // Mostrar un mensaje de éxito
                Toast.makeText(ActivityPedidoFinalizado.this, "Carrito limpiado exitosamente", Toast.LENGTH_SHORT).show();

                // Aquí puedes iniciar la actividad de inicio u otras acciones según tus necesidades
                // Intent intent = new Intent(ActivityPedidoFinalizado.this, Activity_Destinos_Principales.class);
                // startActivity(intent);
            }
        });

    }

    private void cargarCamisetasCarrito() {
        camisetasTiendaList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Carrito")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        camisetasTiendaList.clear();
                        precioTotal = 0.0;


                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String idCamiseta = "" + dataSnapshot.child("idCamiseta").getValue();
                            ItemsDomain modelCamisetasTienda = new ItemsDomain();
                            modelCamisetasTienda.setId(idCamiseta);
                            double precio = Double.parseDouble("" + dataSnapshot.child("precioTotal").getValue());

                            precioTotal += precio;

                            camisetasTiendaList.add(modelCamisetasTienda);
                        }
                        binding.numero.setText("" + camisetasTiendaList.size());
                        //adapterCamisetasCarrito = new Adapter_Camisetas_Carrito(ActivityPedidoFinalizado.this, camisetasTiendaList);
                        //binding.recyclerCamisetasCarrito.setAdapter(adapterCamisetasCarrito);

                        binding.importe.setText(String.valueOf(precioTotal) + "€");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void vaciarCarrito(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Cart")
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ActivityPedidoFinalizado.this,"Camiseta eliminada del carrito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityPedidoFinalizado.this,"Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    //@Override
    //public void onBackPressed() {
//
  //  }
}