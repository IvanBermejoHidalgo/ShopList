package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.Helper.ManagmentCart;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityTramitandoPedidoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Activity_Tramitando_Pedido extends AppCompatActivity {

    private ActivityTramitandoPedidoBinding binding;
    //private ArrayList<Model_Camisetas_Tienda> camisetasTiendaList;
    private ArrayList<ItemsDomain> camisetasTiendaList;
    double precioTotal;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityTramitandoPedidoBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        //cargarCamisetasCarrito();

        // Obtener el precio total del intent
        double precioTotalCarrito = getIntent().getDoubleExtra("precio_total", 0.0);

        // Configurar el precio total en el TextView
        //binding.importe.setText(String.valueOf(precioTotalCarrito));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ManagmentCart managmentCart = new ManagmentCart(Activity_Tramitando_Pedido.this);
                managmentCart.clearCart();

                startActivity(new Intent(Activity_Tramitando_Pedido.this, TiendaActivity.class));
            }
        }, 5000 );



    }

    private void cargarCamisetasCarrito() {
        camisetasTiendaList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Cart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        camisetasTiendaList.clear();
                        precioTotal = 0.0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String idCamiseta = "" + dataSnapshot.child("cartId").getValue();
                            ItemsDomain modelCamisetasTienda = new ItemsDomain();
                            modelCamisetasTienda.setId(idCamiseta);
                            double precio = Double.parseDouble("" + dataSnapshot.child("precioTotal").getValue());

                            precioTotal += precio;
                            camisetasTiendaList.add(modelCamisetasTienda);
                        }

                        //binding.importe.setText(String.valueOf(precioTotal) + " €");
                        //binding.numero.setText(""+camisetasTiendaList.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //@Override
    //public void onBackPressed() {

    //}
}