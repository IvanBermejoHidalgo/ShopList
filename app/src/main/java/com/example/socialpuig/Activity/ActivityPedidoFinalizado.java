package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
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
    double precioTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPedidoFinalizadoBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpiar el carrito localmente
                ManagmentCart managmentCart = new ManagmentCart(ActivityPedidoFinalizado.this);
                managmentCart.clearCart();

                // Mostrar un mensaje de éxito
                ///Toast.makeText(ActivityPedidoFinalizado.this, "Carrito limpiado exitosamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivityPedidoFinalizado.this, TiendaActivity.class));
                // Aquí puedes iniciar la actividad de inicio u otras acciones según tus necesidades
                // Intent intent = new Intent(ActivityPedidoFinalizado.this, Activity_Destinos_Principales.class);
                // startActivity(intent);
            }
        });

    }

}