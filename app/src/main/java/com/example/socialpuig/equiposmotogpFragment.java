package com.example.socialpuig;

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

import com.example.socialpuig.databinding.FragmentEquiposmotogpBinding;
import com.example.socialpuig.databinding.ViewholderEquiposmotogpBinding;

import java.util.List;

public class equiposmotogpFragment extends Fragment {

    private FragmentEquiposmotogpBinding binding;
    private EquiposmotogoViewModel equiposmotogpViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentEquiposmotogpBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        equiposmotogpViewModel = new ViewModelProvider(requireActivity()).get(EquiposmotogoViewModel.class);
        navController = Navigation.findNavController(view);

        /*binding.irANuevoCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_home2Fragment_to_nuevoCancionesFragment);
            }
        });*/

        equiposmotogpFragment.equiposmotogpAdapter equiposmotogpAdapter = new equiposmotogpFragment.equiposmotogpAdapter();
        binding.recyclerView.setAdapter(equiposmotogpAdapter);

        equiposmotogpViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Equiposmotogp>>() {
            @Override
            public void onChanged(List<Equiposmotogp> equiposmotogps) {
                equiposmotogpAdapter.establecerLista(equiposmotogps);
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
                Equiposmotogp equiposmotogp = equiposmotogpAdapter.obtenerequiposmotogp(posicion);
                equiposmotogpViewModel.eliminar(equiposmotogp);

            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    class equiposmotogpViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderEquiposmotogpBinding binding;

        public equiposmotogpViewHolder(ViewholderEquiposmotogpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class equiposmotogpAdapter extends RecyclerView.Adapter<equiposmotogpFragment.equiposmotogpViewHolder> {

        List<Equiposmotogp> equiposmotogps;

        @NonNull
        @Override
        public equiposmotogpFragment.equiposmotogpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new equiposmotogpFragment.equiposmotogpViewHolder(ViewholderEquiposmotogpBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull equiposmotogpFragment.equiposmotogpViewHolder holder, int position) {

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
            Equiposmotogp equiposmotogp = this.equiposmotogps.get(position);

            holder.binding.nombreequimotogp.setText(equiposmotogp.nombreequimotogp);
            holder.binding.nombrequimotogp2.setText(equiposmotogp.descequigpuno);

            // Cargar la imagen correspondiente a la canción
            holder.binding.imagenequimotogp.setImageResource(equiposmotogp.imageequimotogp);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    equiposmotogpViewModel.seleccionar(equiposmotogp);
                    navController.navigate(R.id.action_equiposmotogpFragment_to_mostrarequiposmtgpFragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return equiposmotogps != null ? equiposmotogps.size() : 0;
        }

        public void establecerLista(List<Equiposmotogp> equiposmotogps){
            this.equiposmotogps = equiposmotogps;
            notifyDataSetChanged();
        }

        public Equiposmotogp obtenerequiposmotogp(int posicion){
            return equiposmotogps.get(posicion);
        }
    }
}