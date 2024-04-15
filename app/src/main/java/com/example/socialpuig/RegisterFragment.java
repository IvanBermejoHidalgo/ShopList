package com.example.socialpuig;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterFragment extends Fragment {

    NavController navController;
    private EditText usernametxt,emailEditTextreg, passwordEditText, passwordEditText2;
    private Button emailSignInButton2;
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
        usernametxt = view.findViewById(R.id.usernametxt);
        emailEditTextreg = view.findViewById(R.id.emailEditTextreg);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        passwordEditText2 = view.findViewById(R.id.passwordEditText2);
        displayNameTextView = view.findViewById(R.id.displayNameTextView);

        emailSignInButton2 = view.findViewById(R.id.emailSignInButton2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        emailSignInButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta(user);
            }
        });

    }
    private void crearCuenta(FirebaseUser user) {
        if (!validarFormulario()) {
            return;
        }
        emailSignInButton2.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(emailEditTextreg.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            // Guardar el nombre de usuario en la base de datos
                            guardarNombreUsuario(currentUser, usernametxt.getText().toString());
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
                            emailSignInButton2.setEnabled(true);
                        }
                    }
                });
    }


    private void guardarNombreUsuario(FirebaseUser user, String username) {
        String newName = String.valueOf(usernametxt.getText());
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
        if (TextUtils.isEmpty(usernametxt.getText().toString())) {
            usernametxt.setError("Requerido.");
            valid = false;
        } else {
            usernametxt.setError(null);
        }

        if (TextUtils.isEmpty(emailEditTextreg.getText().toString())) {
            emailEditTextreg.setError("Requerido.");
            valid = false;
        } else {
            emailEditTextreg.setError(null);
        }

        if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            passwordEditText.setError("Requerido.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        if (TextUtils.isEmpty(passwordEditText2.getText().toString())) {
            passwordEditText2.setError("Requerido.");
            valid = false;
        } else {
            passwordEditText2.setError(null);
        }

        if (!passwordEditText.getText().toString().equals(passwordEditText2.getText().toString())) {
            passwordEditText2.setError("Las contraseñas no coinciden.");
            valid = false;
        } else {
            passwordEditText2.setError(null);
        }

        return valid;
    }
}