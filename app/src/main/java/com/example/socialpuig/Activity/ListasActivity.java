package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialpuig.Adapter.ListasAdapter;
import com.example.socialpuig.Domain.ListaDomain;
import com.example.socialpuig.MainActivity;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityListasBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

public class ListasActivity extends BaseActivity {
    ActivityListasBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button btnCrearLista;
    private ListasAdapter listasAdapter;
    private ArrayList<String> listaTitulos;
    private RecyclerView recyclerView;
    private AppBarConfiguration mAppBarConfiguration;

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar and Drawer Layout
        setSupportActionBar(binding.toolbar);
        drawerLayout = findViewById(R.id.listas);
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.homeActivity) {
                    Intent intent = new Intent(ListasActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.drawer_layout3) { // ID del elemento de menú "General"
                    // Abrir la actividad de la tienda
                    Intent intent = new Intent(ListasActivity.this, PedidosRealizadosActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.listas) {
                    Intent intent = new Intent(ListasActivity.this, ListasActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.maint) {
                    Intent intent = new Intent(ListasActivity.this, TiendaActivity.class);
                    startActivity(intent);
                } else if (id == R.id.maincarrito) {
                    Intent intent = new Intent(ListasActivity.this, CartActivity.class);
                    startActivity(intent);
                } else if (id == R.id.ConfiguracionActivity) {
                    Intent intent = new Intent(ListasActivity.this, ConfiguracionActivity.class);
                    startActivity(intent);
                }

                DrawerLayout drawer = findViewById(R.id.listas);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);
        final ImageView photo = header.findViewById(R.id.imageView);
        final TextView name = header.findViewById(R.id.displayNameTextView);

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    if (user.getPhotoUrl()!= null) {
                        Glide.with(ListasActivity.this)
                                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString())
                                .circleCrop()
                                .into(photo);
                    }
                    if (user.getDisplayName()!= null) {
                        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    }
                }
            }
        });

        FirebaseFirestore.getInstance().setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        recyclerView = binding.productosView;

        btnCrearLista = binding.btnCrearLista;
        btnCrearLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevaLista();
            }
        });

        printListas();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void printListas() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            cargarTitulosListas();
        }
    }

    private void cargarTitulosListas() {
        listaTitulos = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference listasRef = mDatabase.child("Users").child(userId).child("Listas");
            listasRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaTitulos.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String titulo = snapshot.child("nombre").getValue(String.class);
                        if (titulo != null) {
                            listaTitulos.add(titulo);
                        }
                    }
                    mostrarTitulosListas();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListasActivity.this, "Error al cargar los títulos de las listas: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void mostrarTitulosListas() {
        ArrayList<ListaDomain> listaDeListas = new ArrayList<>();
        for (String titulo : listaTitulos) {
            ListaDomain lista = new ListaDomain(titulo);
            listaDeListas.add(lista);
        }

        listasAdapter = new ListasAdapter(listaDeListas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listasAdapter);

        listasAdapter.setOnItemClickListener(new ListasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String tituloLista = listaTitulos.get(position);
                abrirContenidoLista(tituloLista);
            }
        });
    }

    private void crearNuevaLista() {
        Intent intent = new Intent(ListasActivity.this, NuevaListaActivity.class);
        startActivity(intent);
    }

    private void abrirContenidoLista(String tituloLista) {
        // Aquí debes implementar la navegación al contenido de la lista
        // Por ejemplo:
        Intent intent = new Intent(ListasActivity.this, ContenidoListaActivity.class);
        intent.putExtra("tituloLista", tituloLista);
        startActivity(intent);
    }
}
