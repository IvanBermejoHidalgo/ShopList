package com.example.socialpuig;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialpuig.databinding.FragmentMostrarCancionesBinding;

public class mostrarCancionesFragment extends Fragment {

    private FragmentMostrarCancionesBinding binding;
    NavController navController;
    private Button volver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentMostrarCancionesBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CancionesViewModel cancionesViewModel = new ViewModelProvider(requireActivity()).get(CancionesViewModel.class);

        cancionesViewModel.seleccionado().observe(getViewLifecycleOwner(), new Observer<Canciones>() {
            @Override
            public void onChanged(Canciones canciones) {
                binding.nombre2.setText(canciones.nombrecancion);
                binding.descripcion2.setText(canciones.nombreartista);

                // Cargar la imagen con Glide
                Glide.with(requireContext())
                        .load(canciones.imageUrl)
                        .into(binding.imagenCancion2);

                binding.resumenuno.setText(canciones.resumenuno);
                binding.resumendos.setText(canciones.resumendos);

                // Establecer un clic en la imagen para mostrarla ampliada
                /*binding.imagenCancion2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(canciones.imageUrl);
                    }
                });*/
                binding.imagenCancion2.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showImageDialog(canciones.imageUrl);
                        return true; // Retorna true para indicar que el evento ha sido consumido
                    }
                });

                // Cargar la segunda imagen con Glide
                Glide.with(requireContext())
                        .load(canciones.imageUrl2)
                        .into(binding.imagenCancion3);

                // Establecer un clic en la segunda imagen para mostrarla ampliada
                binding.imagenCancion3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog(canciones.imageUrl2);
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
    private CancionesViewModel cancionesViewModel;
    private Home2Fragment.CancionesAdapter adapter;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            //cancionesViewModel.deleteCancion(adapter.getCancionAt(position));
        }
    };
}
