package com.example.socialpuig.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.socialpuig.Adapter.CartAdapter;
import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.Helper.ManagmentCart;
import com.example.socialpuig.R;
import com.example.socialpuig.SignInFragment;
import com.example.socialpuig.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        managmentCart = new ManagmentCart(this);

        binding.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // Si el usuario está autenticado, guarda el carrito en la base de datos de Firebase
                    saveCartToFirebase(user.getUid());
                } else {
                    // Si el usuario no está autenticado, redirige al usuario a la pantalla de inicio de sesión
                    startActivity(new Intent(CartActivity.this, SignInFragment.class));
                    finish();
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                startActivity(new Intent(CartActivity.this, TiendaActivity.class));
            }
        });

        calculatorCart();
        initCartList();
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText(itemTotal + "€");
        binding.taxTxt.setText(tax + "€");
        binding.deliveryTxt.setText(delivery + "€");
        binding.totalTxt.setText(total + "€");
    }

    private void saveCartToFirebase(String userId) {
        ArrayList<ItemsDomain> cartItems = managmentCart.getListCart();

        // Generar una ID única para el carrito
        String cartId = mDatabase.child("Users").child(userId).child("Cart").push().getKey();
        double totalCompra = managmentCart.getTotalWithTaxAndDelivery();
        for (ItemsDomain item : cartItems) {
            item.setCartId(cartId);
        }

        // Guardar los datos del carrito en la base de datos de Firebase
        DatabaseReference userCartRef = mDatabase.child("Users").child(userId).child("Cart").child(cartId);
        userCartRef.setValue(cartItems)
                .addOnSuccessListener(aVoid -> {
                    // Guardar el total de la compra dentro de la entrada con la clave cartId en Firebase
                    DatabaseReference totalCompraRef = mDatabase.child("Users").child(userId).child("Cart").child(cartId).child("TotalCompra");
                    totalCompraRef.setValue(totalCompra);

                    // Guardar los datos del carrito en la sección de Compras
                    DatabaseReference comprasRef = mDatabase.child("Compras").child(cartId);
                    comprasRef.setValue(cartItems)
                            .addOnSuccessListener(aVoid1 -> {
                                // Guardar el total de la compra dentro de la entrada con la clave cartId en la sección de Compras
                                DatabaseReference totalCompraComprasRef = mDatabase.child("Compras").child(cartId).child("TotalCompra");
                                totalCompraComprasRef.setValue(totalCompra);

                                //Toast.makeText(CartActivity.this, "Carrito guardado exitosamente en Firebase y en Compras", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(new Intent(CartActivity.this, Activity_Comprar.class));
                                intent.putExtra("precio_total", totalCompra);
                                intent.putExtra("numero_productos", managmentCart.getListCart().size());
                                startActivity(intent);

                                finish();
                            })
                            .addOnFailureListener(e -> {
                                //Toast.makeText(CartActivity.this, "Error al guardar el carrito en Compras: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(CartActivity.this, "Error al guardar el carrito en Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




}