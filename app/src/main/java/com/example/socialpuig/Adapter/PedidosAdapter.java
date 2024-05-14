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

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {
    private List<ItemsDomain> pedidosList;

    public PedidosAdapter(List<ItemsDomain> pedidosList) {
        this.pedidosList = pedidosList;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        ItemsDomain pedido = pedidosList.get(position);
        holder.cartIdTextView.setText(pedido.getCartId());
    }

    @Override
    public int getItemCount() {
        return pedidosList.size();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView cartIdTextView;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            cartIdTextView = itemView.findViewById(R.id.cartIdTextView);
        }
    }
}