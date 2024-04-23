package com.example.socialpuig;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialpuig.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    NavController navController;
    private Button boton_volver;
    private TextInputEditText nombre, correo_electronico, numero_telefono, contraseña, repetir_contraseña;
    TextView displayNameTextView;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);

    }/*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);

        emailSignInButton2 = view.findViewById(R.id.emailSignInButton2);

        emailSignInButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.home2Fragment);
            }
        });

        view.findViewById(R.id.gotoCreateAccountTextView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.signInFragment);
            }
        });

    }

}*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        mAuth = FirebaseAuth.getInstance();
        nombre = view.findViewById(R.id.nombre);
        correo_electronico = view.findViewById(R.id.correo_electronico);
        numero_telefono = view.findViewById(R.id.numero_telefono);
        contraseña = view.findViewById(R.id.contraseña);
        repetir_contraseña = view.findViewById(R.id.repetir_contraseña);
        displayNameTextView = view.findViewById(R.id.displayNameTextView);

        boton_volver = view.findViewById(R.id.boton_volver);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


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
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
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
                                Snackbar.make(requireView(), "Este correo electrónico ya está en uso.", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                            }
                            boton_volver.setEnabled(true);
                        }
                    }
                });
    }


    private void guardarNombreUsuario(FirebaseUser user, String username) {
        String newName = String.valueOf(nombre.getText());
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // El nombre de usuario se actualizó correctamente
                            //displayNameTextView.setText(newName);
                            MainActivity mainActivity = (MainActivity) getActivity(); // Obtén una referencia a MainActivity
                            if (mainActivity != null) {
                                mainActivity.updateNavigationHeaderName(newName); // Llama al método de actualización en MainActivity
                            }
                        } else {
                            // Error al actualizar el nombre de usuario
                        }
                    }
                });
    }

    private void establecerFotoPerfilPredeterminada(FirebaseUser user) {
        Uri defaultPhotoUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.perfildefault);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(defaultPhotoUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // La foto de perfil predeterminada se estableció correctamente
                            MainActivity mainActivity = (MainActivity) getActivity(); // Obtén una referencia a MainActivity
                            if (mainActivity != null) {
                                mainActivity.updateNavigationHeaderPhoto(defaultPhotoUri); // Llama al método de actualización en MainActivity
                            }
                        } else {
                            // Error al establecer la foto de perfil predeterminada
                        }
                    }
                });
    }

    private void actualizarUI(FirebaseUser currentUser) {
        if(currentUser != null){
            String username = currentUser.getDisplayName();
            if (!TextUtils.isEmpty(username)) {
                Toast.makeText(requireContext(), "Bienvenido, " + username, Toast.LENGTH_SHORT).show();
            }
            navController.navigate(R.id.homeFragment);
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
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(Activity_Crear_Cuenta.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(requireContext(), "Cuenta no Creada", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void updateUserInfo() {
        long timestamp = System.currentTimeMillis();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Crear el map y añadir las claves y valores
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", correo_electronico);
        hashMap.put("name", nombre);
        hashMap.put("telefono", numero_telefono);
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
                            //startActivity(new Intent(RegisterFragment.this, HomeFragment.class));
                            navController.navigate(R.id.homeFragment);

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(RegisterFragment.this,"Cuenta no Creada",Toast.LENGTH_SHORT).show();
                        Toast.makeText(requireContext(), "Cuenta no Creada", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}