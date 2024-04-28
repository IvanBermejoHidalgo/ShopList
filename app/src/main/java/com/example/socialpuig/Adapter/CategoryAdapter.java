package com.example.socialpuig.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialpuig.Activity.TiendaActivity;
import com.example.socialpuig.Domain.CategoryDomain;
import com.example.socialpuig.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    private ArrayList<CategoryDomain> items;
    private Context context;
    private TiendaActivity tiendaActivity; // Agregar esta variable
    private OnItemClickListener listener; // Agregar esta línea
    public interface OnItemClickListener {
        void onItemClick(String brand);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CategoryAdapter(ArrayList<CategoryDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, int position) {
        holder.binding.title.setText(items.get(position).getTitle());

        Glide.with(context)
                .load(items.get(position).getPicUrl())
                .into(holder.binding.pic);


        // Manejar el clic en la imagen de la categoría
        // Manejar el clic en la imagen de la categoría
        holder.binding.pic.setOnClickListener(view -> {
            if (listener != null) {
                String brand = items.get(position).getBrand();
                listener.onItemClick(brand);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ViewholderCategoryBinding binding;

        public Viewholder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String brand = items.get(position).getBrand();
                    listener.onItemClick(brand);
                }
            }
        }

    }
}
