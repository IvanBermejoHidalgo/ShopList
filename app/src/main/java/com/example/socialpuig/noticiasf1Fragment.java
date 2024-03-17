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

import com.example.socialpuig.databinding.FragmentNoticiasf1Binding;
import com.example.socialpuig.databinding.ViewholderNoticiasf1Binding;

import java.util.List;


public class noticiasf1Fragment extends Fragment {

    private FragmentNoticiasf1Binding binding;
    private Noticiasf1ViewModel noticiasf1ViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentNoticiasf1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noticiasf1ViewModel = new ViewModelProvider(requireActivity()).get(Noticiasf1ViewModel.class);
        navController = Navigation.findNavController(view);

        /*binding.irANuevoCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_home2Fragment_to_nuevoCancionesFragment);
            }
        });*/

        noticiasf1Adapter noticiasf1Adapter = new noticiasf1Fragment.noticiasf1Adapter();
        binding.recyclerView.setAdapter(noticiasf1Adapter);

        noticiasf1ViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Noticiasf1>>() {
            @Override
            public void onChanged(List<Noticiasf1> noticiasf1s) {
                noticiasf1Adapter.establecerLista(noticiasf1s);
            }
        });


    }

    class Noticiasf1ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderNoticiasf1Binding binding;

        public Noticiasf1ViewHolder(ViewholderNoticiasf1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class noticiasf1Adapter extends RecyclerView.Adapter<Noticiasf1ViewHolder> {

        List<Noticiasf1> noticiasf1s;

        @NonNull
        @Override
        public noticiasf1Fragment.Noticiasf1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new noticiasf1Fragment.Noticiasf1ViewHolder(ViewholderNoticiasf1Binding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull noticiasf1Fragment.Noticiasf1ViewHolder holder, int position) {

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
            Noticiasf1 noticiasf1 = this.noticiasf1s.get(position);

            holder.binding.noticiaf1.setText(noticiasf1.nombrenoticia);
            holder.binding.descnotif1.setText(noticiasf1.descnoticia);

            // Cargar la imagen correspondiente a la canción
            holder.binding.imgnotif1.setImageResource(noticiasf1.imageUrlnoticia);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticiasf1ViewModel.seleccionar(noticiasf1);
                    navController.navigate(R.id.action_noticiasf1Fragment2_to_mostrarnoticiasf1Fragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return noticiasf1s != null ? noticiasf1s.size() : 0;
        }

        public void establecerLista(List<Noticiasf1> noticiasf1s){
            this.noticiasf1s = noticiasf1s;
            notifyDataSetChanged();
        }

        public Noticiasf1 obtenerNoticiasf1(int posicion){
            return noticiasf1s.get(posicion);
        }
    }
}