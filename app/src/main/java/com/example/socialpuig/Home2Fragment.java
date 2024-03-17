package com.example.socialpuig;

import android.os.Bundle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.socialpuig.databinding.FragmentHome2Binding;
import com.example.socialpuig.databinding.ViewholderCancionesBinding;

import java.util.List;

public class Home2Fragment extends Fragment {

    private FragmentHome2Binding binding;
    private CancionesViewModel cancionesViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentHome2Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancionesViewModel = new ViewModelProvider(requireActivity()).get(CancionesViewModel.class);
        navController = Navigation.findNavController(view);

        /*binding.irANuevoCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_home2Fragment_to_nuevoCancionesFragment);
            }
        });*/

        CancionesAdapter cancionesAdapter = new CancionesAdapter();
        binding.recyclerView.setAdapter(cancionesAdapter);

        cancionesViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Canciones>>() {
            @Override
            public void onChanged(List<Canciones> canciones) {
                cancionesAdapter.establecerLista(canciones);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT  | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicion = viewHolder.getAdapterPosition();
                Canciones canciones = cancionesAdapter.obtenerCanciones(posicion);
                cancionesViewModel.eliminar(canciones);

            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    class CancionesViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderCancionesBinding binding;

        public CancionesViewHolder(ViewholderCancionesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class CancionesAdapter extends RecyclerView.Adapter<CancionesViewHolder> {

        List<Canciones> canciones;

        @NonNull
        @Override
        public CancionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CancionesViewHolder(ViewholderCancionesBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CancionesViewHolder holder, int position) {

            /*Canciones canciones = this.canciones.get(position);

            holder.binding.nombrecancion.setText(canciones.nombrecancion);
            holder.binding.nombreartista.setText(canciones.nombreartista);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancionesViewModel.seleccionar(canciones);
                    navController.navigate(R.id.action_home2Fragment_to_mostrarCancionesFragment);
                }
            });
            Canciones canciones = this.canciones.get(position);

            holder.binding.nombrecancion.setText(canciones.nombrecancion);
            holder.binding.nombreartista.setText(canciones.nombreartista);

            // Carga la imagen correspondiente a la canción
            int resourceId = holder.itemView.getContext().getResources()
                    .getIdentifier(canciones.nombrecancion.toLowerCase(), "drawable",
                            holder.itemView.getContext().getPackageName());
            if (resourceId != 0) {
                holder.binding.imagenCancion.setImageResource(resourceId);
            } else {
                // Si la imagen no está disponible, muestra una imagen predeterminada
                //holder.binding.imagenCancion.setImageResource(R.drawable.imagen_default);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancionesViewModel.seleccionar(canciones);
                    navController.navigate(R.id.action_home2Fragment_to_mostrarCancionesFragment);
                }
            });*/
            Canciones cancion = this.canciones.get(position);

            holder.binding.nombrecancion.setText(cancion.nombrecancion);
            holder.binding.nombreartista.setText(cancion.nombreartista);

            // Cargar la imagen correspondiente a la canción
            holder.binding.imagenCancion.setImageResource(cancion.imageUrl);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancionesViewModel.seleccionar(cancion);
                    navController.navigate(R.id.action_home2Fragment_to_mostrarCancionesFragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return canciones != null ? canciones.size() : 0;
        }

        public void establecerLista(List<Canciones> canciones){
            this.canciones = canciones;
            notifyDataSetChanged();
        }

        public Canciones obtenerCanciones(int posicion){
            return canciones.get(posicion);
        }
    }
}