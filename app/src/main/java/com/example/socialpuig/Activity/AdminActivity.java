package com.example.socialpuig.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialpuig.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Obtener la referencia de la base de datos para las compras
        DatabaseReference comprasRef = FirebaseDatabase.getInstance().getReference().child("Compras");

        // Escuchar los cambios en la base de datos de compras
        comprasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalCompras = 0;
                for (DataSnapshot compraSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot totalCompraSnapshot = compraSnapshot.child("TotalCompra");
                    if (totalCompraSnapshot.exists()) {
                        int totalCompra = totalCompraSnapshot.getValue(Integer.class);
                        totalCompras += totalCompra;
                    }
                }

                // Actualizar la UI con el total de compras
                TextView totalComprasTextView = findViewById(R.id.totalComprasTextView);
                totalComprasTextView.setText("Total de compras: " + totalCompras);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error al leer los datos de compras", databaseError.toException());
            }
        });

        // Obtener la referencia de la base de datos para los usuarios
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Escuchar los cambios en la base de datos de usuarios
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numeroUsuarios = dataSnapshot.getChildrenCount();

                // Actualizar la UI con el número de usuarios
                TextView numeroUsuariosTextView = findViewById(R.id.numeroUsuariosTextView);
                numeroUsuariosTextView.setText("Número de usuarios registrados: " + numeroUsuarios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error al leer los datos de usuarios", databaseError.toException());
            }
        });
    }
}
