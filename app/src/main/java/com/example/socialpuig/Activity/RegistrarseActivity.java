package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.socialpuig.HomeFragment;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityRegistrarseBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseActivity extends AppCompatActivity {

    private ActivityRegistrarseBinding binding;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityRegistrarseBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });




    }

    private String nombre = "", email = "", telefono= "", password = "";

    private void validateData() {
        //Obtener el texto del edittext, y convertirlo en un string eliminando espacios innecesarios
        nombre = binding.nombre.getText().toString().trim();
        email = binding.correoElectronico.getText().toString().trim();
        telefono = binding.numeroTelefono.getText().toString().trim();
        password = binding.contraseA.getText().toString().trim();
        String cPassword = binding.repetirContraseA.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(this,"Introduce tu nombre",Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this,"Dirección de correo electronico inválida",Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(telefono).matches()) {
            Toast.makeText(this,"Número de telefono incorrecto",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Escribe una contraseña",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cPassword)) {
            Toast.makeText(this,"Repite la contraseña",Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cPassword)) {
            Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        } else {
            createAccount();
        }

    }


    private void createAccount() {
        //dialog.showDialog();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrarseActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void updateUserInfo() {
        long timestamp = System.currentTimeMillis();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Crear el map y añadir las claves y valores
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", nombre);
        hashMap.put("telefono", telefono);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "user");
        hashMap.put("timestamp", timestamp);
        // Crear la referencia de usuarios en la base de datos(si no esta creada aún) y añadir el valor del usuario, si ya esta creada
        // tan solo se añaden los apartados con las claves y valores del usuario que se crea.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        try {
                            Thread.sleep(3000);
                            //dialog.hideDialog();
                            startActivity(new Intent(RegistrarseActivity.this, HomeActivity.class));

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrarseActivity.this,"Cuenta no Creada",Toast.LENGTH_SHORT).show();

                    }
                });

    }



}