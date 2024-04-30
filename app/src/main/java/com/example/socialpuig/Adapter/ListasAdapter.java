package com.example.socialpuig.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.socialpuig.Domain.ListaDomain;
import com.example.socialpuig.R;
import java.util.ArrayList;

public class ListasAdapter extends RecyclerView.Adapter<ListasAdapter.ListasViewHolder> {
    private ArrayList<ListaDomain> listaList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ListasViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombreLista;

        public ListasViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvNombreLista = itemView.findViewById(R.id.tvNombreLista);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ListasAdapter(ArrayList<ListaDomain> listaList) {
        this.listaList = listaList;
    }

    @NonNull
    @Override
    public ListasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item, parent, false);
        ListasViewHolder lvh = new ListasViewHolder(v, mListener);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListasViewHolder holder, int position) {
        ListaDomain currentItem = listaList.get(position);
        holder.tvNombreLista.setText(currentItem.getNombre());
    }

    @Override
    public int getItemCount() {
        return listaList.size();
    }
}
