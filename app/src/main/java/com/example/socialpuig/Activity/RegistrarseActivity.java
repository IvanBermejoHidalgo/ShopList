package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialpuig.HomeFragment;
import com.example.socialpuig.MainActivity;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityRegistrarseBinding;
import com.example.socialpuig.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityRegistrarseBinding binding;
    NavController navController;
    private Button boton_volver;
    private TextInputEditText nombre, correo_electronico, numero_telefono, contraseña, repetir_contraseña;
    TextView displayNameTextView;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityRegistrarseBinding.inflate(getLayoutInflater())).getRoot());

        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.nombre);
        correo_electronico = findViewById(R.id.correo_electronico);
        numero_telefono = findViewById(R.id.numero_telefono);
        contraseña = findViewById(R.id.contraseña);
        repetir_contraseña = findViewById(R.id.repetir_contraseña);
        displayNameTextView = findViewById(R.id.displayNameTextView);

        boton_volver = findViewById(R.id.boton_volver);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        findViewById(R.id.gotoCreateAccountTextView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrarseActivity.this, IniciarSesionActivity.class));
            }
        });

        boton_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //crearCuenta(user);
                createAccount();
            }
        });

    }


    private void crearCuenta(FirebaseUser user) {
        if (!validarFormulario()) {
            return;
        }
        boton_volver.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(correo_electronico.getText().toString(), contraseña.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            // Guardar el nombre de usuario en la base de datos
                            guardarNombreUsuario(currentUser, nombre.getText().toString());
                            establecerFotoPerfilPredeterminada(currentUser);
                            // Actualizar la interfaz de usuario
                            actualizarUI(currentUser);
                        } else {
                            // Si el error se debe a que el correo electrónico ya está en uso
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Snackbar.make(findViewById(android.R.id.content), "Este correo electrónico ya está en uso.", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                            }
                            boton_volver.setEnabled(true);
                        }
                    }
                });
    }


    private void guardarNombreUsuario(FirebaseUser user, String name) {
        String newName = String.valueOf(nombre.getText());
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // El nombre de usuario se actualizó correctamente
                            //displayNameTextView.setText(newName);
                            /*MainActivity mainActivity = (MainActivity) getApplicationContext(); // Obtén una referencia a MainActivity
                            if (mainActivity != null) {
                                mainActivity.updateNavigationHeaderName(newName); // Llama al método de actualización en MainActivity
                            }*/
                            //updateNavigationHeaderName(newName); // Llama al método de actualización en MainActivity

                        } else {
                            // Error al actualizar el nombre de usuario
                        }
                    }
                });
    }

    private void establecerFotoPerfilPredeterminada(FirebaseUser user) {
        Uri defaultPhotoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.perfildefault);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(defaultPhotoUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            /*MainActivity mainActivity = (MainActivity) getApplicationContext();
                            if (mainActivity != null) {
                                mainActivity.updateNavigationHeaderPhoto(defaultPhotoUri);
                            }*/
                            //updateNavigationHeaderPhoto(defaultPhotoUri);

                        }
                    }
                });
    }

    private void actualizarUI(FirebaseUser name) {
        if(name != null){
            String username = name.getDisplayName();
            if (!TextUtils.isEmpty(username)) {
                Toast.makeText(RegistrarseActivity.this, "Bienvenido, " + username, Toast.LENGTH_SHORT).show();
            }
            //navController.navigate(R.id.homeFragment);
            startActivity(new Intent(RegistrarseActivity.this, HomeActivity.class));
        }
    }


    private boolean validarFormulario() {
        boolean valid = true;
        if (TextUtils.isEmpty(nombre.getText().toString())) {
            nombre.setError("Requerido.");
            valid = false;
        } else {
            nombre.setError(null);
        }

        if (TextUtils.isEmpty(correo_electronico.getText().toString())) {
            correo_electronico.setError("Requerido.");
            valid = false;
        } else {
            correo_electronico.setError(null);
        }

        if (TextUtils.isEmpty(numero_telefono.getText().toString())) {
            numero_telefono.setError("Requerido.");
            valid = false;
        } else {
            numero_telefono.setError(null);
        }

        if (TextUtils.isEmpty(contraseña.getText().toString())) {
            contraseña.setError("Requerido.");
            valid = false;
        } else {
            contraseña.setError(null);
        }

        if (TextUtils.isEmpty(repetir_contraseña.getText().toString())) {
            repetir_contraseña.setError("Requerido.");
            valid = false;
        } else {
            repetir_contraseña.setError(null);
        }

        if (!contraseña.getText().toString().equals(repetir_contraseña.getText().toString())) {
            repetir_contraseña.setError("Las contraseñas no coinciden.");
            valid = false;
        } else {
            repetir_contraseña.setError(null);
        }

        return valid;
    }



    private void createAccount() {
        //dialog.showDialog();
        if (!validarFormulario()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(correo_electronico.getText().toString(), contraseña.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        // Guardar el nombre de usuario en la base de datos
                        guardarNombreUsuario(currentUser, nombre.getText().toString());
                        establecerFotoPerfilPredeterminada(currentUser);
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(Activity_Crear_Cuenta.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegistrarseActivity.this, "Cuenta no Creada", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void updateUserInfo() {
        long timestamp = System.currentTimeMillis();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtener los valores de los EditText correctamente
        String email = correo_electronico.getText().toString();
        String name = nombre.getText().toString();
        String telefono = numero_telefono.getText().toString();

        //Crear el map y añadir las claves y valores
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("telefono", telefono);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "user");
        hashMap.put("timestamp", timestamp);

        // Crear la referencia de usuarios en la base de datos y añadir los datos del usuario
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Éxito al guardar los datos del usuario
                        // Redirigir a la pantalla de inicio o realizar otras acciones necesarias
                        //navController.navigate(R.id.homeFragment);
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        // Guardar el nombre de usuario en la base de datos
                        //guardarNombreUsuario(currentUser, nombre.getText().toString());
                        establecerFotoPerfilPredeterminada(currentUser);
                        // Actualizar la interfaz de usuario
                        actualizarUI(currentUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al guardar los datos del usuario
                        Toast.makeText(RegistrarseActivity.this, "Cuenta no Creada", Toast.LENGTH_SHORT).show();
                    }
                });
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