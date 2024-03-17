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

import com.example.socialpuig.databinding.FragmentNoticiasmotogpBinding;
import com.example.socialpuig.databinding.ViewholderCancionesBinding;
import com.example.socialpuig.databinding.ViewholderNoticiasmotogpBinding;

import java.util.List;


public class noticiasmotogpFragment extends Fragment {

    private FragmentNoticiasmotogpBinding binding;
    private NoticiasmotogpViewModel noticiasmotogpViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentNoticiasmotogpBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noticiasmotogpViewModel = new ViewModelProvider(requireActivity()).get(NoticiasmotogpViewModel.class);
        navController = Navigation.findNavController(view);

        /*binding.irANuevoCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_home2Fragment_to_nuevoCancionesFragment);
            }
        });*/

        noticiasmotogpAdapter noticiasmotogpAdapter = new noticiasmotogpFragment.noticiasmotogpAdapter();
        binding.recyclerView.setAdapter(noticiasmotogpAdapter);

        noticiasmotogpViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Noticiasmotogp>>() {
            @Override
            public void onChanged(List<Noticiasmotogp> noticiasmotogps) {
                noticiasmotogpAdapter.establecerLista(noticiasmotogps);
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
                Noticiasmotogp noticiasmotogp = noticiasmotogpAdapter.obtenerNoticiasmotogp(posicion);
                noticiasmotogpViewModel.eliminar(noticiasmotogp);

            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    class NoticiasmotogpViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderNoticiasmotogpBinding binding;

        public NoticiasmotogpViewHolder(ViewholderNoticiasmotogpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class noticiasmotogpAdapter extends RecyclerView.Adapter<NoticiasmotogpViewHolder> {

        List<Noticiasmotogp> noticiasmotogps;

        @NonNull
        @Override
        public NoticiasmotogpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new noticiasmotogpFragment.NoticiasmotogpViewHolder(ViewholderNoticiasmotogpBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull noticiasmotogpFragment.NoticiasmotogpViewHolder holder, int position) {

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
            Noticiasmotogp noticiasmotogp = this.noticiasmotogps.get(position);

            holder.binding.noticiamotogp.setText(noticiasmotogp.nombrenoticia);
            holder.binding.descnotimotogp.setText(noticiasmotogp.descnoticia);

            // Cargar la imagen correspondiente a la canción
            holder.binding.imgnotimotogp.setImageResource(noticiasmotogp.imageUrlnoticia);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticiasmotogpViewModel.seleccionar(noticiasmotogp);
                    navController.navigate(R.id.action_noticiasmotogpFragment_to_mostrarnoticiasmotogpFragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return noticiasmotogps != null ? noticiasmotogps.size() : 0;
        }

        public void establecerLista(List<Noticiasmotogp> noticiasmotogps){
            this.noticiasmotogps = noticiasmotogps;
            notifyDataSetChanged();
        }

        public Noticiasmotogp obtenerNoticiasmotogp(int posicion){
            return noticiasmotogps.get(posicion);
        }
    }
}