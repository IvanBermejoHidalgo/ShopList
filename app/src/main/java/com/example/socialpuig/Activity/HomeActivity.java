package com.example.socialpuig.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialpuig.AppViewModel;
import com.example.socialpuig.Post;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityHomeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {
    NavController navController;
    public AppViewModel appViewModel;
    private AppBarConfiguration mAppBarConfiguration;

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Setup Toolbar and Drawer Layout
        setSupportActionBar(binding.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout2);
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
                    Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.drawer_layout3) {
                    Intent intent = new Intent(HomeActivity.this, PedidosRealizadosActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.listas) {
                    Intent intent = new Intent(HomeActivity.this, ListasActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.maint) {
                    Intent intent = new Intent(HomeActivity.this, TiendaActivity.class);
                    startActivity(intent);
                } else if (id == R.id.maincarrito) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                } else if (id == R.id.ConfiguracionActivity) {
                    Intent intent = new Intent(HomeActivity.this, ConfiguracionActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Recuperar referencias de vistas y realizar configuraciones necesarias
        // por ejemplo, configurar el RecyclerView y adaptador
        RecyclerView postsRecyclerView = findViewById(R.id.postsRecyclerView);



        Query query = FirebaseFirestore.getInstance().collection("posts").orderBy("timeStamp", Query.Direction.DESCENDING).limit(50);

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();

        postsRecyclerView.setAdapter(new PostsAdapter(options, this));

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        // Agregar el OnClickListener al FloatingActionButton
        FloatingActionButton gotoNewPostFragmentButton = findViewById(R.id.gotoNewPostFragmentButton);
        gotoNewPostFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar NewPostActivity
                Intent intent = new Intent(HomeActivity.this, NewPostActivity.class);
                startActivity(intent);
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
                        Glide.with(HomeActivity.this)
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

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.PostViewHolder> {
        private Context context;
        public PostsAdapter(@NonNull FirestoreRecyclerOptions<Post> options, Context context) {
            super(options);
            this.context = context;
        }

        @NonNull
        @Override
        public HomeActivity.PostsAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HomeActivity.PostsAdapter.PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_post, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull HomeActivity.PostsAdapter.PostViewHolder holder, int position, @NonNull final Post post) {
            if (post.authorPhotoUrl != null) {
                // Utiliza Glide con el contexto correcto (this.context)
                Glide.with(this.context)
                        .load(post.authorPhotoUrl)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Estrategia de almacenamiento en caché
                        .into(holder.authorPhotoImageView);
            } else {
                holder.authorPhotoImageView.setImageResource(R.drawable.user);
            }
            holder.authorTextView.setText(post.author);
            holder.contentTextView.setText(post.content);

            // Gestion de likes
            final String postKey = getSnapshots().getSnapshot(position).getId();
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(post.likes.containsKey(uid))
                holder.likeImageView.setImageResource(R.drawable.like_on);
            else
                holder.likeImageView.setImageResource(R.drawable.like_off);
            holder.numLikesTextView.setText(String.valueOf(post.likes.size()));
            holder.likeImageView.setOnClickListener(view -> {
                FirebaseFirestore.getInstance().collection("posts")
                        .document(postKey)
                        .update("likes."+uid, post.likes.containsKey(uid) ?
                                FieldValue.delete() : true);
            });

            // Fecha
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(post.timeStamp);

            //SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String fecha = format.format(calendar.getTime());

            holder.dateTextView.setText(fecha);

            // Miniatura de media
            if (post.mediaUrl != null) {
                holder.mediaImageView.setVisibility(View.VISIBLE);
                if ("audio".equals(post.mediaType)) {
                    Glide.with(context).load(R.drawable.audio).centerCrop().into(holder.mediaImageView);
                } else {
                    Glide.with(context).load(post.mediaUrl).centerCrop().into(holder.mediaImageView);
                }
                /*holder.mediaImageView.setOnClickListener(view -> {
                    appViewModel.postSeleccionado.setValue(post);
                    //navController.navigate(R.id.mediaFragment);
                    //Intent intent = new Intent(HomeActivity.this, MediaActivity.class);
                    //startActivity(intent);

                });*/
            } else {
                holder.mediaImageView.setVisibility(View.GONE);
            }



            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (post.uid.equals(currentUserId)) {
                holder.deletePostImageView.setVisibility(View.VISIBLE);
            } else {
                holder.deletePostImageView.setVisibility(View.GONE);
            }

            holder.deletePostImageView.setOnClickListener(view -> {
                FirebaseFirestore.getInstance().collection("posts")
                        .document(postKey)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Publicación eliminada", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Error deleting post", Toast.LENGTH_SHORT).show();
                        });
            });

        }


        class PostViewHolder extends RecyclerView.ViewHolder {
            ImageView authorPhotoImageView, likeImageView, mediaImageView, deletePostImageView;
            TextView authorTextView, contentTextView, numLikesTextView, dateTextView;

            PostViewHolder(@NonNull View itemView) {
                super(itemView);

                authorPhotoImageView = itemView.findViewById(R.id.authorPhotoImageView);
                likeImageView = itemView.findViewById(R.id.likeImageView);
                mediaImageView = itemView.findViewById(R.id.mediaImage);
                authorTextView = itemView.findViewById(R.id.authorTextView);
                contentTextView = itemView.findViewById(R.id.contentTextView);
                numLikesTextView = itemView.findViewById(R.id.numLikesTextView);
                deletePostImageView = itemView.findViewById(R.id.deletePostImageView);
                dateTextView = itemView.findViewById(R.id.dateTextView);

            }
        }

    }
    /*@Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/
}