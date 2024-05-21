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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
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

// Implementa el listener para el botón de eliminar cuenta
        TextView deleteAccountTextView = findViewById(R.id.deleteAccountTextView);
        deleteAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAccountDialog(user);
            }
        });
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

        boolean isNameChanged = !newName.isEmpty() && !newName.equals(user.getDisplayName());
        boolean isPhotoChanged = photoUri != null;

        if (isNameChanged || isPhotoChanged) {
            UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();

            if (isNameChanged) {
                profileUpdatesBuilder.setDisplayName(newName);
            }

            if (isPhotoChanged) {
                StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("profile_photos/" + user.getUid());
                photoRef.putFile(photoUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                profileUpdatesBuilder.setPhotoUri(uri);
                                updateProfile(user, profileUpdatesBuilder.build(), isNameChanged, newName, uri.toString());
                            });
                        });
            } else {
                updateProfile(user, profileUpdatesBuilder.build(), isNameChanged, newName, null);
            }
        } else {
            Toast.makeText(MiCuentaActivity.this, "No se realizaron cambios", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile(FirebaseUser user, UserProfileChangeRequest profileUpdates, boolean isNameChanged, String newName, String photoUrl) {
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (isNameChanged) {
                            displayNameTextView.setText(newName);
                            updatePosts(user.getUid(), newName, photoUrl);
                        } else if (photoUrl != null) {
                            updatePosts(user.getUid(), null, photoUrl);
                        }
                        Intent intent = new Intent(MiCuentaActivity.this, ConfiguracionActivity.class);
                        startActivity(intent);
                        Toast.makeText(MiCuentaActivity.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MiCuentaActivity.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePosts(String userId, String newName, String newPhotoUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query postsQuery = db.collection("posts").whereEqualTo("uid", userId);

        postsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = db.batch();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (newName != null) {
                        batch.update(document.getReference(), "author", newName);
                    }
                    if (newPhotoUrl != null) {
                        batch.update(document.getReference(), "authorPhotoUrl", newPhotoUrl);
                    }
                }
                batch.commit().addOnCompleteListener(batchTask -> {
                    if (batchTask.isSuccessful()) {
                        Toast.makeText(MiCuentaActivity.this, "Posts actualizados correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MiCuentaActivity.this, "Error al actualizar los posts", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void showDeleteAccountDialog(FirebaseUser user) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteAccount(user))
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void deletePosts(String userId, OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query postsQuery = db.collection("posts").whereEqualTo("uid", userId);

        postsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = db.batch();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    batch.delete(document.getReference());
                }
                batch.commit().addOnCompleteListener(onCompleteListener);
            } else {
                //onCompleteListener.onComplete(task);
            }
        });
    }

    private void deleteAccount(FirebaseUser user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        deleteUserRelatedCollections(databaseReference, user.getUid(), task -> {
            if (task.isSuccessful()) {
                // Continúa con la eliminación del usuario en la autenticación de Firebase
                user.delete().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MiCuentaActivity.this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MiCuentaActivity.this, IniciarSesionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MiCuentaActivity.this, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(MiCuentaActivity.this, "Error al eliminar el usuario de la base de datos", Toast.LENGTH_SHORT).show();
            }
        });



        deletePosts(user.getUid(), task -> {
            if (task.isSuccessful()) {
                // Continúa con la eliminación del usuario en la autenticación de Firebase
                user.delete().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MiCuentaActivity.this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MiCuentaActivity.this, IniciarSesionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MiCuentaActivity.this, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(MiCuentaActivity.this, "Error al eliminar los posts del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deleteUserRelatedCollections(DatabaseReference databaseReference, String userId, OnCompleteListener<Void> onCompleteListener) {
        // Elimina el usuario de la base de datos de Firebase Realtime
        databaseReference.child("Users").child(userId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si se elimina el usuario correctamente, notifica al listener
                        onCompleteListener.onComplete(task);
                    } else {
                        // Si hay un error al eliminar el usuario, notifica al listener con el Task correspondiente
                        onCompleteListener.onComplete(task);
                    }
                });
    }


    private void deleteCollection(Query collection, OnCompleteListener<Void> onCompleteListener) {
        collection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = FirebaseFirestore.getInstance().batch();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    batch.delete(document.getReference());
                }
                batch.commit().addOnCompleteListener(onCompleteListener);
            } else {
                //onCompleteListener.onComplete(task);
            }
        });
    }


    @Override
    public void onBackPressed() {
        // Lógica para volver a la pantalla anterior
        super.onBackPressed();
    }
}