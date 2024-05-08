package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MiCuentaActivity extends AppCompatActivity {

    ImageView photoImageView;
    TextView displayNameTextView, emailTextView;
    EditText nameEditText;
    Button saveButton;
    ImageButton changePhotoButton;
    Uri photoUri;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;


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
        // Comprueba si se tienen permisos para acceder a la cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Solicita permisos para acceder a la cámara si no se tienen
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            // Si se tienen permisos, abre la actividad de la cámara
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    photoUri = data.getData();
                    photoImageView.setImageURI(photoUri);
                    // Llama al método para guardar los cambios después de haber seleccionado una imagen de la galería
                    saveChanges(FirebaseAuth.getInstance().getCurrentUser());
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    if (imageBitmap != null) {
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Title", null);
                        photoUri = Uri.parse(path);
                        photoImageView.setImageURI(photoUri);
                        // Llama al método para guardar los cambios después de haber tomado una foto
                        saveChanges(FirebaseAuth.getInstance().getCurrentUser());
                    } else {
                        Toast.makeText(this, "Error al obtener la imagen de la cámara", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error al obtener la imagen de la cámara", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    // Método para guardar la imagen en el almacenamiento externo
    private String saveImageToExternalStorage(Bitmap bitmap) {
        // Define un directorio donde se guardará la imagen
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Si el directorio no existe, créalo
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return null;
            }
        }

        // Crea un nombre de archivo único para la imagen
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        // Crea un archivo en el directorio con el nombre de archivo generado
        File file = new File(directory, fileName);

        try {
            // Crea un flujo de salida para escribir la imagen en el archivo
            FileOutputStream outputStream = new FileOutputStream(file);
            // Comprime la imagen y escribe los bytes en el flujo de salida
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            // Cierra el flujo de salida
            outputStream.close();
            // Devuelve la ruta completa del archivo guardado
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            // Si hay un error, devuelve null
            return null;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario otorgó el permiso, puedes realizar la acción deseada
                openCamera();
            } else {
                Toast.makeText(this, "El permiso de la cámara ha sido denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void saveChanges(FirebaseUser user) {
        String newName = nameEditText.getText().toString();

        // Verifica si el nombre ha cambiado
        if (!newName.isEmpty() && !newName.equals(user.getDisplayName())) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            displayNameTextView.setText(newName);
                            Toast.makeText(MiCuentaActivity.this, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show();

                            // Inicia la nueva actividad después de guardar los cambios
                            Intent intent = new Intent(MiCuentaActivity.this, ConfiguracionActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MiCuentaActivity.this, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (newName.isEmpty()) {
            Toast.makeText(MiCuentaActivity.this, "No se ha proporcionado un nuevo nombre", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(MiCuentaActivity.this, ConfiguracionActivity.class);
            //startActivity(intent);
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
                                            Intent intent = new Intent(MiCuentaActivity.this, ConfiguracionActivity.class);
                                            startActivity(intent);
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