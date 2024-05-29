package com.example.socialpuig.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialpuig.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class IniciarSesionActivity extends AppCompatActivity {

    private TextInputEditText correo_electronico, contraseña;
    private Button emailSignInButton;
    private LinearLayout signInForm;
    private ProgressBar signInProgressBar;
    private FirebaseAuth mAuth;
    private SignInButton googleSignInButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        mAuth = FirebaseAuth.getInstance();

        correo_electronico = findViewById(R.id.correo_electronico);
        contraseña = findViewById(R.id.contraseña);
        emailSignInButton = findViewById(R.id.emailSignInButton);
        signInForm = findViewById(R.id.signInForm);
        signInProgressBar = findViewById(R.id.signInProgressBar);
        //googleSignInButton = findViewById(R.id.googleSignInButton);

        findViewById(R.id.gotoCreateAccountTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IniciarSesionActivity.this, RegistrarseActivity.class));
            }
        });

        findViewById(R.id.gotoCreateAccountTextView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IniciarSesionActivity.this, CambiarContraActivity.class));
            }
        });

        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accederConEmail();
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            try {
                                firebaseAuthWithGoogle(GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class));
                            } catch (ApiException e) {
                                Log.e("ABCD", "signInResult:failed code=" + e.getStatusCode());
                            }
                        }
                    }
                });

/*        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accederConGoogle();
            }
        });*/
    }

    private void accederConEmail() {
        if (!validarFormulario()) {
            return;
        }
        signInForm.setVisibility(View.GONE);
        signInProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(correo_electronico.getText().toString(), contraseña.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            actualizarUI(mAuth.getCurrentUser());
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                        }
                        signInForm.setVisibility(View.VISIBLE);
                        signInProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void actualizarUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            if (currentUser.getEmail().equals("admin@gmail.com")) {
                startActivity(new Intent(IniciarSesionActivity.this, AdminActivity.class));
            } else {
                startActivity(new Intent(IniciarSesionActivity.this, HomeActivity.class));
            }
        }
    }

    private void accederConGoogle() {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());
        activityResultLauncher.launch(googleSignInClient.getSignInIntent());
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        if (acct == null) return;
        signInProgressBar.setVisibility(View.VISIBLE);
        signInForm.setVisibility(View.GONE);
        mAuth.signInWithCredential(GoogleAuthProvider.getCredential(acct.getIdToken(), null))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("ABCD", "signInWithCredential:success");
                            actualizarUI(mAuth.getCurrentUser());
                        } else {
                            Log.e("ABCD", "signInWithCredential:failure", task.getException());
                            signInProgressBar.setVisibility(View.GONE);
                            signInForm.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private boolean validarFormulario() {
        boolean valid = true;
        if (TextUtils.isEmpty(correo_electronico.getText().toString())) {
            correo_electronico.setError("Requerido.");
            valid = false;
        } else {
            correo_electronico.setError(null);
        }

        if (TextUtils.isEmpty(contraseña.getText().toString())) {
            contraseña.setError("Requerido.");
            valid = false;
        } else {
            contraseña.setError(null);
        }

        return valid;
    }
}