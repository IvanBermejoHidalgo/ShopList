package com.example.socialpuig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ProfileFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    ImageView photoImageView;
    TextView displayNameTextView, emailTextView;
    ImageButton changePhotoButton;
    EditText nameEditText;
    Button saveButton;
    Uri photoUri;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoImageView = view.findViewById(R.id.photoImageView);
        displayNameTextView = view.findViewById(R.id.displayNameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        changePhotoButton = view.findViewById(R.id.changePhotoButton);
        nameEditText = view.findViewById(R.id.nameEditText);
        saveButton = view.findViewById(R.id.saveButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            displayNameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());
            Glide.with(requireView()).load(user.getPhotoUrl()).into(photoImageView);

            // Establece un listener para el botón de cambiar foto
            changePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePickerDialog();
                }
            });

            // Establece un listener para el botón de guardar
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveChanges(user);
                }
            });
        }
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
    // prueba
    private void saveChanges(FirebaseUser user) {
        String newName = String.valueOf(nameEditText.getText());
        Uri imagen;

        // Verifica si el nombre ha cambiado
        if (!newName.equals(user.getDisplayName())) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            displayNameTextView.setText(newName);
                            MainActivity mainActivity = (MainActivity) getActivity(); // Obtén una referencia a MainActivity
                            if (mainActivity != null) {
                                mainActivity.updateNavigationHeaderName(newName); // Llama al método de actualización en MainActivity
                            }
                            Toast.makeText(getContext(), "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show();

                            // Actualizar el nombre en Firebase Auth
                            FirebaseUser updatedUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (updatedUser != null) {
                                displayNameTextView.setText(updatedUser.getDisplayName());
                            }
                        } else {
                            Toast.makeText(getContext(), "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Verifica si la foto ha cambiado
        if (photoUri != null && !photoUri.equals(user.getPhotoUrl())) {
            // Sube la nueva foto a Firebase Storage
            StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("profile_photos/" + user.getUid());
            photoRef.putFile(photoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Obtén la URL de descarga de la foto subida
                            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Actualiza la URL de la foto en Firebase Authentication
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(uri)
                                            .build();

                                    user.updateProfile(profileUpdates)

                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("ProfileFragment", "User photo URL updated.");
                                                    }
                                                }
                                            });

                                    MainActivity mainActivity = (MainActivity) getActivity(); // Obtén una referencia a MainActivity
                                    if (mainActivity != null) {
                                        mainActivity.updateNavigationHeaderPhoto(photoUri); // Llama al método de actualización en MainActivity
                                    }
                                }
                            });
                        }
                    });
        }
    }
}