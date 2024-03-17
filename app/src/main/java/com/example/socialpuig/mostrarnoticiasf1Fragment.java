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

public class mostrarnoticiasf1Fragment extends Fragment {

    private FragmentMostrarnoticiasf1Binding binding;
    NavController navController;
    private Button volver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentMostrarnoticiasf1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Noticiasf1ViewModel noticiasf1ViewModel = new ViewModelProvider(requireActivity()).get(Noticiasf1ViewModel.class);

        noticiasf1ViewModel.seleccionado().observe(getViewLifecycleOwner(), new Observer<Noticiasf1>() {
            @Override
            public void onChanged(Noticiasf1 noticiasf1) {
                binding.nombrenoticiaf1.setText(noticiasf1.nombrenoticia);
                binding.descripcionnoticiaf1.setText(noticiasf1.descnoticia);

                // Cargar la imagen con Glide
                Glide.with(requireContext())
                        .load(noticiasf1.imageUrlnoticia)
                        .into(binding.imagennoticiaf1);

                binding.resumenunonoticiaf1.setText(noticiasf1.resumenunonoticia);
                binding.resumendosnoticiaf1.setText(noticiasf1.resumendosnoticia);

                // Establecer un clic en la imagen para mostrarla ampliada
                binding.imagennoticiaf1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(noticiasf1.imageUrlnoticia);
                    }
                });

                // Cargar la segunda imagen con Glide
                Glide.with(requireContext())
                        .load(noticiasf1.imageUrlnoticia2)
                        .into(binding.imagennoticiaf12);

                // Establecer un clic en la segunda imagen para mostrarla ampliada
                /*binding.imagennoticiaf12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(noticiasf1.imageUrlnoticia2);
                    }
                });*/

                binding.imagennoticiaf12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(noticiasf1.imageUrlnoticia2);
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
