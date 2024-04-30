package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.socialpuig.Adapter.SizeAdapter;
import com.example.socialpuig.Adapter.SliderAdapter;
import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.Domain.SliderItems;
import com.example.socialpuig.Fragment.DescriptionFragment;
import com.example.socialpuig.Fragment.ReviewFragment;
import com.example.socialpuig.Fragment.SoldFragment;
import com.example.socialpuig.Helper.ManagmentCart;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityDetailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemsDomain object;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;
    private Handler slideHandle = new Handler();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        binding.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListSelectionDialog();
            }
        });

        getBundles();
        initBanners();
        initSize();
        setupViewPager();

    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");

        binding.recyclerSize.setAdapter(new SizeAdapter(list));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }
        binding.viewpageSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpageSlider));
        binding.viewpageSlider.setClipToPadding(false);
        binding.viewpageSlider.setClipChildren(false);
        //binding.viewpageSlider.setOffscreenPageLimit(3);
        binding.viewpageSlider.setOffscreenPageLimit(2);
        binding.viewpageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundles() {
        object = (ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText(object.getPrice() + "€");
        binding.ratingBar.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating() + " Rating");

        binding.addTocartBtn.setOnClickListener(v -> {
            object.setNumberinCart(numberOrder);
            managmentCart.insertItem(object);
        });
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        //SoldFragment tab3 = new SoldFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        //Bundle bundle3 = new Bundle();

        bundle1.putString("description", object.getDescription());

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        //tab3.setArguments(bundle3);

        adapter.addFrag(tab1,"Descripción");
        adapter.addFrag(tab2,"Reviews");
        //adapter.addFrag(tab3,"Sold");

        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void showListSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Lista");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference listasRef = mDatabase.child("Users").child(currentUser.getUid()).child("Listas");
            listasRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        ArrayList<String> listNames = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String listName = snapshot.child("nombre").getValue(String.class);
                            if (listName != null) {
                                listNames.add(listName);
                            }
                        }

                        if (!listNames.isEmpty()) {
                            builder.setItems(listNames.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String selectedListName = listNames.get(which);
                                    addToSelectedList(selectedListName);
                                }
                            });
                        } else {
                            builder.setMessage("No hay listas disponibles");
                        }
                    } else {
                        builder.setMessage("No hay listas disponibles");
                    }

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DetailActivity.this, "Error al obtener las listas: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // El usuario no está autenticado
            Toast.makeText(DetailActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }


    private void addToSelectedList(String listName) {
        // Aquí implementa la lógica para agregar el producto a la lista seleccionada en Firebase

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference listaSeleccionadaRef = mDatabase.child("Users").child(currentUser.getUid()).child("Listas").child(listName).child("Productos").push();
            listaSeleccionadaRef.setValue(object)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailActivity.this, "Producto agregado a la lista: " + listName, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActivity.this, "Error al agregar producto a la lista: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



}