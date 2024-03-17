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

import com.example.socialpuig.databinding.FragmentEquiposf1Binding;
import com.example.socialpuig.databinding.ViewholderEquiposf1Binding;
import com.example.socialpuig.databinding.ViewholderPilotosf1Binding;

import java.util.List;

public class equiposf1Fragment extends Fragment {

    private FragmentEquiposf1Binding binding;
    private Equiposf1ViewModel equiposf1ViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentEquiposf1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        equiposf1ViewModel = new ViewModelProvider(requireActivity()).get(Equiposf1ViewModel.class);
        navController = Navigation.findNavController(view);

        /*binding.irANuevoCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_home2Fragment_to_nuevoCancionesFragment);
            }
        });*/

        equiposf1Adapter equiposf1Adapter = new equiposf1Fragment.equiposf1Adapter();
        binding.recyclerView.setAdapter(equiposf1Adapter);

        equiposf1ViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Equiposf1>>() {
            @Override
            public void onChanged(List<Equiposf1> equiposf1s) {
                equiposf1Adapter.establecerLista(equiposf1s);
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
                Equiposf1 equiposf1 = equiposf1Adapter.obtenerequiposf1(posicion);
                equiposf1ViewModel.eliminar(equiposf1);

            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    class equiposf1ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderEquiposf1Binding binding;

        public equiposf1ViewHolder(ViewholderEquiposf1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class equiposf1Adapter extends RecyclerView.Adapter<equiposf1Fragment.equiposf1ViewHolder> {

        List<Equiposf1> equiposf1s;

        @NonNull
        @Override
        public equiposf1Fragment.equiposf1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new equiposf1Fragment.equiposf1ViewHolder(ViewholderEquiposf1Binding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull equiposf1Fragment.equiposf1ViewHolder holder, int position) {

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
            Equiposf1 equiposf1 = this.equiposf1s.get(position);

            holder.binding.nombreequif1.setText(equiposf1.nombreequif1);
            holder.binding.nombrequif1.setText(equiposf1.descequiuno);

            // Cargar la imagen correspondiente a la canción
            holder.binding.imagenequif1.setImageResource(equiposf1.imageequif1);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    equiposf1ViewModel.seleccionar(equiposf1);
                    navController.navigate(R.id.action_equiposf1Fragment_to_mostrarequiposf1Fragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return equiposf1s != null ? equiposf1s.size() : 0;
        }

        public void establecerLista(List<Equiposf1> equiposf1s){
            this.equiposf1s = equiposf1s;
            notifyDataSetChanged();
        }

        public Equiposf1 obtenerequiposf1(int posicion){
            return equiposf1s.get(posicion);
        }
    }
}