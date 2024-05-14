package com.example.socialpuig.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.R;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> {
    private List<ItemsDomain> productosList;

    public ProductosAdapter(List<ItemsDomain> productosList) {
        this.productosList = productosList;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ItemsDomain producto = productosList.get(position);
        holder.titleTextView.setText(producto.getTitle());
        // Agrega más configuraciones si necesitas mostrar más información sobre el producto
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            // Si necesitas mostrar más vistas, inicialízalas aquí
        }
    }
}
