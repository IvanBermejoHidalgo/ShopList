package com.example.socialpuig.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.database.collection.BuildConfig;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.socialpuig.AppViewModel;
import com.example.socialpuig.Post;
import com.example.socialpuig.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class NewPostActivity extends AppCompatActivity {

    Button publishButton;
    EditText postConentEditText;
    Uri mediaUri;
    String mediaTipo;

    public AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        publishButton = findViewById(R.id.publishButton);
        postConentEditText = findViewById(R.id.postContentEditText);

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar();
            }
        });

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        findViewById(R.id.camara_fotos).setOnClickListener(v -> tomarFoto());
        findViewById(R.id.imagen_galeria).setOnClickListener(v -> seleccionarImagen());

        appViewModel.mediaSeleccionado.observe(this, media -> {
            this.mediaUri = media.uri;
            this.mediaTipo = media.tipo;
            Glide.with(this).load(media.uri).into((ImageView) findViewById(R.id.previsualizacion));
        });
    }

    private void publicar() {
        String postContent = postConentEditText.getText().toString();
        if (TextUtils.isEmpty(postContent)) {
            postConentEditText.setError("Required");
            return;
        }
        publishButton.setEnabled(false);
        if (mediaTipo == null) {
            guardarEnFirestore(postContent, null);
        } else {
            pujaIguardarEnFirestore(postContent);
        }
    }

    private void guardarEnFirestore(String postContent, String mediaUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Post post = new Post(user.getUid(), user.getDisplayName(), (user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "R.drawable.user"), postContent, System.currentTimeMillis(), mediaUrl, mediaTipo);

        FirebaseFirestore.getInstance().collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        setResult(Activity.RESULT_OK);
                        // Finalizar la actividad actual (NewPostActivity)
                        finish();
                        // Iniciar una nueva instancia de HomeActivity
                        Intent intent = new Intent(NewPostActivity.this, HomeActivity.class);
                        startActivity(intent);
                        // Limpiar la selección de medios en el ViewModel
                        appViewModel.setMediaSeleccionado(null, null);
                    }
                });
    }

    private void pujaIguardarEnFirestore(final String postText) {
        FirebaseStorage.getInstance().getReference(mediaTipo + "/" +
                        UUID.randomUUID())
                .putFile(mediaUri)
                .continueWithTask(task ->
                        task.getResult().getStorage().getDownloadUrl())
                .addOnSuccessListener(url -> guardarEnFirestore(postText,
                        url.toString()));
    }

    private final ActivityResultLauncher<String> galeria =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        appViewModel.setMediaSeleccionado(uri, mediaTipo);
                    });

    private final ActivityResultLauncher<Uri> camaraFotos =
            registerForActivityResult(new ActivityResultContracts.TakePicture(),
                    isSuccess -> {
                        appViewModel.setMediaSeleccionado(mediaUri, "image");
                    });

    private void seleccionarImagen() {
        mediaTipo = "image";
        galeria.launch("image/*");
    }

    private void tomarFoto() {
        try {
            mediaUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    File.createTempFile("img", ".jpg",
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            );
            camaraFotos.launch(mediaUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
