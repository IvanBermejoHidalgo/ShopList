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

import com.example.socialpuig.databinding.FragmentPilotosf1Binding;
import com.example.socialpuig.databinding.ViewholderNoticiasmotogpBinding;
import com.example.socialpuig.databinding.ViewholderPilotosf1Binding;

import java.util.List;


public class pilotosf1Fragment extends Fragment {

    private FragmentPilotosf1Binding binding;
    private Pilotosf1ViewModel pilotosf1ViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentPilotosf1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pilotosf1ViewModel = new ViewModelProvider(requireActivity()).get(Pilotosf1ViewModel.class);
        navController = Navigation.findNavController(view);

        /*binding.irANuevoCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_home2Fragment_to_nuevoCancionesFragment);
            }
        });*/

        pilotosf1Adapter pilotosf1Adapter = new pilotosf1Adapter();
        binding.recyclerView.setAdapter(pilotosf1Adapter);

        pilotosf1ViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Pilotosf1>>() {
            @Override
            public void onChanged(List<Pilotosf1> pilotosf1) {
                pilotosf1Adapter.establecerLista(pilotosf1);
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
                Pilotosf1 pilotosf1 = pilotosf1Adapter.obtenerpilotosf1(posicion);
                pilotosf1ViewModel.eliminar(pilotosf1);

            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    class pilotosf1ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderPilotosf1Binding binding;

        public pilotosf1ViewHolder(ViewholderPilotosf1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class pilotosf1Adapter extends RecyclerView.Adapter<pilotosf1ViewHolder> {

        List<Pilotosf1> pilotosf1;

        @NonNull
        @Override
        public pilotosf1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new pilotosf1ViewHolder(ViewholderPilotosf1Binding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull pilotosf1ViewHolder holder, int position) {

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
            Pilotosf1 pilotosf1 = this.pilotosf1.get(position);

            holder.binding.nombrepil.setText(pilotosf1.nombrepilf1);
            holder.binding.nombrepiloto.setText(pilotosf1.descpiluno);

            // Cargar la imagen correspondiente a la canción
            holder.binding.imagenpil.setImageResource(pilotosf1.imagepilf1);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pilotosf1ViewModel.seleccionar(pilotosf1);
                    navController.navigate(R.id.action_pilotosf1Fragment_to_mostrarpilotosf1Fragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pilotosf1 != null ? pilotosf1.size() : 0;
        }

        public void establecerLista(List<Pilotosf1> pilotosf1){
            this.pilotosf1 = pilotosf1;
            notifyDataSetChanged();
        }

        public Pilotosf1 obtenerpilotosf1(int posicion){
            return pilotosf1.get(posicion);
        }
    }
}