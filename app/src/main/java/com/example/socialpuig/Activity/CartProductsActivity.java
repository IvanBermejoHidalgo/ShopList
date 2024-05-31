package com.example.socialpuig.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialpuig.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartProductsActivity extends AppCompatActivity {
    private ListView productsListView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> cartProducts;
    private DatabaseReference cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_products);

        productsListView = findViewById(R.id.productsListView);
        cartProducts = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartProducts);
        productsListView.setAdapter(arrayAdapter);

        String selectedCartId = getIntent().getStringExtra("cartId");
        if (selectedCartId != null) {
            cartRef = FirebaseDatabase.getInstance().getReference().child(selectedCartId);
            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cartProducts.clear();
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        String productName = productSnapshot.child("title").getValue(String.class);
                        if (productName != null) {
                            cartProducts.add(productName);
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}
