package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialpuig.AppViewModel;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityConfiguracionBinding;
import com.example.socialpuig.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class ConfiguracionActivity extends AppCompatActivity {

    private NavController navController;
    public AppViewModel appViewModel;
    private AppBarConfiguration mAppBarConfiguration;

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private ActivityConfiguracionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        binding = ActivityConfiguracionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        drawerLayout = findViewById(R.id.ConfiguracionActivity);
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

        //navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.homeActivity) {
                    Intent intent = new Intent(ConfiguracionActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.listas) { // ID del elemento de menú "General"
                    // Abrir la actividad de la tienda
                    Intent intent = new Intent(ConfiguracionActivity.this, ListasActivity.class);
                    startActivity(intent);
                    return true;
                }else if (id == R.id.maint) {
                    Intent intent = new Intent(ConfiguracionActivity.this, TiendaActivity.class);
                    startActivity(intent);
                } else if (id == R.id.maincarrito) {
                    Intent intent = new Intent(ConfiguracionActivity.this, CartActivity.class);
                    startActivity(intent);
                } else if (id == R.id.ConfiguracionActivity) {
                    // Manejar la selección de RecyclerView
                    //NavController navController = Navigation.findNavController(HomeActivity.this, R.id.nav_host_fragment_content_main);
                    //navController.navigate(R.id.configuracionFragment);
                    Intent intent = new Intent(ConfiguracionActivity.this, ConfiguracionActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
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
                        Glide.with(ConfiguracionActivity.this)
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


        //navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        findViewById(R.id.configuracionuno).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navController.navigate(R.id.profileFragment);
                Intent intent = new Intent(ConfiguracionActivity.this, MiCuentaActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.configuraciondos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navController.navigate(R.id.ajustesFragment);
                Intent intent = new Intent(ConfiguracionActivity.this, PedidosRealizadosActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.configuraciontres).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navController.navigate(R.id.soporteFragment);
                Intent intent = new Intent(ConfiguracionActivity.this, SoporteActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navController.navigate(R.id.signOutFragment);
                Intent intent = new Intent(ConfiguracionActivity.this, SignOutActivity.class);
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
