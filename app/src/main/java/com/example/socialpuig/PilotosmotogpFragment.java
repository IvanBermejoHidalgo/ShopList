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

import com.example.socialpuig.databinding.FragmentPilotosmotogpBinding;
import com.example.socialpuig.databinding.ViewholderPilotosmotogpBinding;

import java.util.List;


public class PilotosmotogpFragment extends Fragment {

    private FragmentPilotosmotogpBinding binding;
    private PilotosmotogpViewModel pilotosmotogpViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentPilotosmotogpBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pilotosmotogpViewModel = new ViewModelProvider(requireActivity()).get(PilotosmotogpViewModel.class);
        navController = Navigation.findNavController(view);

        /*binding.irANuevoCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_home2Fragment_to_nuevoCancionesFragment);
            }
        });*/

        pilotosmotogpAdapter pilotosmotogpAdapter = new pilotosmotogpAdapter();
        binding.recyclerView.setAdapter(pilotosmotogpAdapter);

        pilotosmotogpViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<PilotosMotogp>>() {
            @Override
            public void onChanged(List<PilotosMotogp> pilotosMotogps) {
                pilotosmotogpAdapter.establecerLista(pilotosMotogps);
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
                PilotosMotogp pilotosMotogp = pilotosmotogpAdapter.obtenerpilotosmotogp(posicion);
                pilotosmotogpViewModel.eliminar(pilotosMotogp);

            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    class pilotosmotogpViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderPilotosmotogpBinding binding;

        public pilotosmotogpViewHolder(ViewholderPilotosmotogpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class pilotosmotogpAdapter extends RecyclerView.Adapter<pilotosmotogpViewHolder> {

        List<PilotosMotogp> pilotosMotogps;

        @NonNull
        @Override
        public PilotosmotogpFragment.pilotosmotogpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PilotosmotogpFragment.pilotosmotogpViewHolder(ViewholderPilotosmotogpBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PilotosmotogpFragment.pilotosmotogpViewHolder holder, int position) {

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
            PilotosMotogp pilotosMotogp = this.pilotosMotogps.get(position);

            holder.binding.nombrepilgp.setText(pilotosMotogp.nombrepilgp);
            holder.binding.nombrepilotogp.setText(pilotosMotogp.descpilunogp);

            // Cargar la imagen correspondiente a la canción
            holder.binding.imagenpilgp.setImageResource(pilotosMotogp.imagepilgp);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pilotosmotogpViewModel.seleccionar(pilotosMotogp);
                    navController.navigate(R.id.action_pilotosmotogpFragment_to_mostrarpilotosmotogpFragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pilotosMotogps != null ? pilotosMotogps.size() : 0;
        }

        public void establecerLista(List<PilotosMotogp> pilotosMotogps){
            this.pilotosMotogps = pilotosMotogps;
            notifyDataSetChanged();
        }

        public PilotosMotogp obtenerpilotosmotogp(int posicion){
            return pilotosMotogps.get(posicion);
        }
    }
}