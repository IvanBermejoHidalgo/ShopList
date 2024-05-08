package com.example.socialpuig.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.socialpuig.R;
import com.example.socialpuig.SignInFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SignOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Realiza el sign out de Google y Firebase
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()).signOut();

        FirebaseAuth.getInstance().signOut();

        // Navega a la pantalla de inicio de sesión (SignInActivity)
        Intent intent = new Intent(this, SignInFragment.class);
        startActivity(intent);

        // Finaliza esta actividad para evitar volver a ella desde la pantalla de inicio de sesión
        finish();
    }
}