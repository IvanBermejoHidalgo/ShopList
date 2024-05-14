package com.example.socialpuig.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialpuig.Adapter.PedidosAdapter;
import androidx.annotation.NonNull;
import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PedidosRealizadosActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> userCarts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_realizados);

        listView = findViewById(R.id.listView);
        userCarts = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userCarts);
        listView.setAdapter(arrayAdapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Cart");

        userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartId = cartSnapshot.getKey();
                    userCarts.add(cartId);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCartId = userCarts.get(position);
                Intent intent = new Intent(PedidosRealizadosActivity.this, CartProductsActivity.class);
                intent.putExtra("cartId", selectedCartId);
                startActivity(intent);
            }
        });
    }
}
