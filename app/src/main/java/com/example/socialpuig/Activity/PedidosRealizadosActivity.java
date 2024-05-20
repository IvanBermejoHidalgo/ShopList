package com.example.socialpuig.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.Glide;
import com.example.socialpuig.AppViewModel;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityHomeBinding;
import com.example.socialpuig.databinding.ActivityPedidosRealizadosBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.List;

public class PedidosRealizadosActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> userCarts;
    private TextView pedidosCountTextView;
    NavController navController;
    public AppViewModel appViewModel;
    private AppBarConfiguration mAppBarConfiguration;

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ActivityPedidosRealizadosBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_realizados);

        binding = ActivityPedidosRealizadosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Setup Toolbar and Drawer Layout
        setSupportActionBar(binding.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout3);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Enable Up Button for Navigation Drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup Navigation withNavController
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeActivity, R.id.maint, R.id.maincarrito
        )
                .setOpenableLayout(drawerLayout)
                .build();



        View header = navigationView.getHeaderView(0);
        final ImageView photo = header.findViewById(R.id.imageView);
        final TextView name = header.findViewById(R.id.displayNameTextView);
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    if (user.getPhotoUrl()!= null) {
                        Glide.with(PedidosRealizadosActivity.this)
                                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString())
                                .circleCrop()
                                .into(photo);
                    }
                    if (user.getDisplayName()!= null) {
                        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    }
                    //email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                }
            }
        });

        FirebaseFirestore.getInstance().setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.homeActivity) {
                    Intent intent = new Intent(PedidosRealizadosActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.listas) { // ID del elemento de menú "General"
                    // Abrir la actividad de la tienda
                    Intent intent = new Intent(PedidosRealizadosActivity.this, ListasActivity.class);
                    startActivity(intent);
                    return true;
                }else if (id == R.id.maint) {
                    Intent intent = new Intent(PedidosRealizadosActivity.this, TiendaActivity.class);
                    startActivity(intent);
                } else if (id == R.id.maincarrito) {
                    Intent intent = new Intent(PedidosRealizadosActivity.this, CartActivity.class);
                    startActivity(intent);
                } else if (id == R.id.ConfiguracionActivity) {
                    Intent intent = new Intent(PedidosRealizadosActivity.this, ConfiguracionActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        listView = findViewById(R.id.listView);
        pedidosCountTextView = findViewById(R.id.pedidosCountTextView);
        userCarts = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userCarts);
        listView.setAdapter(arrayAdapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Cart");

        userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int pedidosCount = 0;
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartId = cartSnapshot.getKey();
                    userCarts.add(cartId);
                    pedidosCount++;
                }
                arrayAdapter.notifyDataSetChanged();
                pedidosCountTextView.setText("Número de pedidos: " + pedidosCount);
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
                Intent intent = new Intent(PedidosRealizadosActivity.this, PedidosDentroActivity.class);
                intent.putExtra("cartId", selectedCartId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
