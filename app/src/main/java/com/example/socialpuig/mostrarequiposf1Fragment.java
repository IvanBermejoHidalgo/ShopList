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
import com.example.socialpuig.databinding.FragmentMostrarequiposf1Binding;
import com.example.socialpuig.databinding.FragmentMostrarpilotosf1Binding;


public class mostrarequiposf1Fragment extends Fragment {

    private FragmentMostrarequiposf1Binding binding;
    NavController navController;
    private Button volver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentMostrarequiposf1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Equiposf1ViewModel equiposf1ViewModel = new ViewModelProvider(requireActivity()).get(Equiposf1ViewModel.class);

        equiposf1ViewModel.seleccionado().observe(getViewLifecycleOwner(), new Observer<Equiposf1>() {
            @Override
            public void onChanged(Equiposf1 equiposf1) {
                binding.nombreequipof1.setText(equiposf1.nombreequif1);
                binding.descripcionequipof1.setText(equiposf1.descequiuno);

                // Cargar la imagen con Glide
                Glide.with(requireContext())
                        .load(equiposf1.imageequif1)
                        .into(binding.imagenequipof1);

                binding.resumenunoequipof1.setText(equiposf1.resumenequiuno);
                binding.resumendosequipof1.setText(equiposf1.resumenequidos);

                // Establecer un clic en la imagen para mostrarla ampliada
                binding.imagenequipof1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(equiposf1.imageequif1);
                    }
                });

                // Cargar la segunda imagen con Glide
                Glide.with(requireContext())
                        .load(equiposf1.imageequif12)
                        .into(binding.imagenequipof12);

                // Establecer un clic en la segunda imagen para mostrarla ampliada
                binding.imagenequipof12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(equiposf1.imageequif12);
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
