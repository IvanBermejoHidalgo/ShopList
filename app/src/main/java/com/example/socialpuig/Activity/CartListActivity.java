package com.example.socialpuig.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartListActivity extends AppCompatActivity {
    private ListView cartListView;
    private ArrayAdapter<String> arrayAdapter;
    private List<ItemsDomain> allCarts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        cartListView = findViewById(R.id.cartListView);
        allCarts = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        cartListView.setAdapter(arrayAdapter);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DatabaseReference cartRef = userSnapshot.child("Cart").getRef();
                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot productSnapshot : cartSnapshot.getChildren()) {
                                    ItemsDomain product = productSnapshot.getValue(ItemsDomain.class);
                                    allCarts.add(product);
                                }
                            }
                            updateAdapter();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        cartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsDomain selectedProduct = allCarts.get(position);
                Intent intent = new Intent(CartListActivity.this, CartProductsActivity.class);
                intent.putExtra("selectedProduct", selectedProduct);
                startActivity(intent);
            }
        });
    }

    private void updateAdapter() {
        arrayAdapter.clear();
        for (ItemsDomain product : allCarts) {
            arrayAdapter.add(product.getTitle());
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
