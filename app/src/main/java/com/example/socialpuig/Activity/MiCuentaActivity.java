package com.example.socialpuig.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialpuig.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MiCuentaActivity extends AppCompatActivity {

    ImageView photoImageView;
    TextView displayNameTextView, emailTextView;
    EditText nameEditText;
    Button saveButton;
    ImageButton changePhotoButton;
    Uri photoUri;

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        photoImageView = findViewById(R.id.photoImageView);
        displayNameTextView = findViewById(R.id.displayNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        nameEditText = findViewById(R.id.nameEditText);
        saveButton = findViewById(R.id.saveButton);
        changePhotoButton = findViewById(R.id.changePhotoButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            displayNameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);

            // Implementa el listener del ImageButton para cambiar la foto
            changePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePickerDialog();
                }
            });

            ImageView backBtn = findViewById(R.id.backBtn);
            backBtn.setOnClickListener(v -> onBackPressed());

            // Implementa el listener del botón para guardar los cambios
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveChanges(user);
                }
            });
        }
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar foto de perfil");
        builder.setItems(new String[]{"Elegir de galería", "Tomar foto"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openGallery();
                } else if (which == 1) {
                    openCamera();
                }
            }
        });
        builder.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    photoUri = data.getData();
                    photoImageView.setImageURI(photoUri);
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                photoUri = (Uri) data.getExtras().get("data");
                photoImageView.setImageURI(photoUri);
            }
        }
    }

    private void saveChanges(FirebaseUser user) {
        String newName = nameEditText.getText().toString();

        // Verifica si el nombre ha cambiado
        if (!newName.equals(user.getDisplayName())) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            displayNameTextView.setText(newName);
                            Toast.makeText(MiCuentaActivity.this, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MiCuentaActivity.this, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Verifica si la foto ha cambiado
        if (photoUri != null) {
            StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("profile_photos/" + user.getUid());
            photoRef.putFile(photoUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Glide.with(MiCuentaActivity.this).load(uri).into(photoImageView);
                                            Toast.makeText(MiCuentaActivity.this, "Foto de perfil actualizada correctamente", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MiCuentaActivity.this, "Error al actualizar la foto de perfil", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });
                    });
        }
    }

    @Override
    public void onBackPressed() {
        // Lógica para volver a la pantalla anterior
        super.onBackPressed();
    }
}
