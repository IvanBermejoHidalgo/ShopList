package com.example.socialpuig;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialpuig.Activity.CartActivity;
import com.example.socialpuig.Activity.TiendaActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialpuig.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        //getSupportActionBar().hide();


       /* binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Button btnIniciaSesion = findViewById(R.id.btnIniciaSesin);

        btnIniciaSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el NavController desde la actividad que contiene esta vista
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);

                // Navegar a otra pantalla cuando se haga clic en el botón
                navController.navigate(R.id.action_logSignFragment_to_signInFragment);
            }
        });

        Button registrarse = findViewById(R.id.txtREGISTRATE);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el NavController desde la actividad que contiene esta vista
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);

                // Navegar a otra pantalla cuando se haga clic en el botón
                navController.navigate(R.id.action_logSignFragment_to_registerFragment);
            }
        });



        /*Button REGISTRATE = findViewById(R.id.txtREGISTRATE);

        REGISTRATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el NavController desde la actividad que contiene esta vista
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);

                // Navegar a otra pantalla cuando se haga clic en el botón
                navController.navigate(R.id.action_logSignFragment_to_signUpFragment);
            }
        });*/


        DrawerLayout drawer = binding.drawerLayout;
        //NavigationView navigationView = binding.navView;
        navigationView = binding.navView; // Inicializar la referencia al NavigationView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.pilotosmotogpFragment,
                R.id.equiposmotogpFragment, R.id.configuracionFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View header = navigationView.getHeaderView(0);
        final ImageView photo = header.findViewById(R.id.imageView);
        final TextView name = header.findViewById(R.id.displayNameTextView);
        final TextView email = header.findViewById(R.id.emailTextView);

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    if (user.getPhotoUrl()!= null) {
                        Glide.with(MainActivity.this)
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

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.signInFragment
                        || destination.getId() == R.id.logSignFragment || destination.getId() == R.id.cambioContraFragment || destination.getId() == R.id.registerFragment) {
                    binding.appBarMain.toolbar.setVisibility(View.GONE);
                } else {
                    binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
                }


            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.maint) { // ID del elemento de menú "General"
                    // Abrir la actividad de la tienda
                    Intent intent = new Intent(MainActivity.this, TiendaActivity.class);
                    startActivity(intent);
                    return true;
                }

                if (id == R.id.maincarrito) { // ID del elemento de menú "General"
                    // Abrir la actividad de la tienda
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                    return true;
                }

                // Agregar el resto de lógica de manejo de selección de menú aquí si es necesario

                return false;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateNavigationHeaderName(String newName) {
        View headerView = navigationView.getHeaderView(0);
        TextView nameTextView = headerView.findViewById(R.id.displayNameTextView);
        nameTextView.setText(newName);
    }

    public void updateNavigationHeaderPhoto(Uri photoUri) {
        View headerView = navigationView.getHeaderView(0);
        ImageView photoImageView = headerView.findViewById(R.id.imageView);
        Glide.with(this)
                .load(photoUri)
                .circleCrop()
                .into(photoImageView);
    }

}