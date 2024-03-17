package com.example.socialpuig;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.socialpuig.databinding.FragmentMostrarnoticiasf1Binding;
import com.example.socialpuig.databinding.FragmentMostrarnoticiasmotogpBinding;
import com.example.socialpuig.databinding.FragmentMostrarpilotosf1Binding;

public class mostrarpilotosf1Fragment extends Fragment {

    private FragmentMostrarpilotosf1Binding binding;
    NavController navController;
    private Button volver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentMostrarpilotosf1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Pilotosf1ViewModel pilotosf1ViewModel = new ViewModelProvider(requireActivity()).get(Pilotosf1ViewModel.class);

        pilotosf1ViewModel.seleccionado().observe(getViewLifecycleOwner(), new Observer<Pilotosf1>() {
            @Override
            public void onChanged(Pilotosf1 pilotosf1) {
                binding.nombrepilotof1.setText(pilotosf1.nombrepilf1);
                binding.descripcionpilotof1.setText(pilotosf1.descpiluno);

                // Cargar la imagen con Glide
                Glide.with(requireContext())
                        .load(pilotosf1.imagepilf1)
                        .into(binding.imagenpilotof1);

                binding.resumenunopilotof1.setText(pilotosf1.resumenpiluno);
                binding.resumendospilotof1.setText(pilotosf1.resumenpildos);

                // Establecer un clic en la imagen para mostrarla ampliada
                binding.imagenpilotof1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(pilotosf1.imagepilf1);
                    }
                });

                // Cargar la segunda imagen con Glide
                Glide.with(requireContext())
                        .load(pilotosf1.imagepilf12)
                        .into(binding.imagenpilotof12);

                // Establecer un clic en la segunda imagen para mostrarla ampliada
                binding.imagenpilotof12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(pilotosf1.imagepilf12);
                    }
                });
            }
        });

        navController = Navigation.findNavController(view);

        volver = view.findViewById(R.id.volver);

        view.findViewById(R.id.volver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.home2Fragment);
            }
        });
    }

    private void showImageDialog(int imageResource) {
        // Crear un diálogo personalizado
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        // Obtener la referencia de la imagen en el diálogo
        ImageView expandedImage = dialog.findViewById(R.id.expandedImage);

        // Cargar la imagen ampliada en el ImageView usando Glide
        Glide.with(requireContext())
                .load(imageResource)
                .into(expandedImage);

        // Hacer clic en el diálogo para cerrarlo
        expandedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        dialog.show();
    }
}
