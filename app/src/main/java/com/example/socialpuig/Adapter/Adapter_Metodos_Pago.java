package com.example.socialpuig.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialpuig.R;
import com.example.socialpuig.Model.Model_Metodos_Pago;
import com.example.socialpuig.databinding.EstiloMetodosPagoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Adapter_Metodos_Pago extends RecyclerView.Adapter<Adapter_Metodos_Pago.HolderMetodosPago> {

    private Context context;
    ArrayList<Model_Metodos_Pago> metodosPagosList;
    EstiloMetodosPagoBinding binding;

    public Adapter_Metodos_Pago(Context context, ArrayList<Model_Metodos_Pago> metodosPagosList) {
        this.context = context;
        this.metodosPagosList = metodosPagosList;
    }

    @NonNull
    @Override
    public HolderMetodosPago onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloMetodosPagoBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Adapter_Metodos_Pago.HolderMetodosPago(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMetodosPago holder, int position) {
        Model_Metodos_Pago modelMetodosPago = metodosPagosList.get(position);

        String titular = "" + modelMetodosPago.getTitular();
        String numero = "" + modelMetodosPago.getNumero();
        String caducidad = "" + modelMetodosPago.getCaducidad();
        String cvv = "" + modelMetodosPago.getCvv();

        holder.numeroMetodoPago.setText("" + (position + 1));
        holder.titular.setText(titular);
        holder.numero.setText(numero);
        holder.caducidad.setText(caducidad);
        holder.cvv.setText(cvv);

        holder.boton_desplegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textoTitular.getVisibility() == View.VISIBLE){
                    holder.boton_desplegar.setImageResource(R.drawable.icono_desplegar);
                    holder.textoTitular.setVisibility(View.GONE);
                } else {
                    holder.boton_desplegar.setImageResource(R.drawable.icono_replegar);
                    holder.textoTitular.setVisibility(View.VISIBLE);
                }

                if (holder.titular.getVisibility() == View.VISIBLE){
                    holder.titular.setVisibility(View.GONE);
                } else holder.titular.setVisibility(View.VISIBLE);

                if (holder.numero.getVisibility() == View.VISIBLE){
                    holder.numero.setVisibility(View.GONE);
                } else holder.numero.setVisibility(View.VISIBLE);

                if (holder.caducidad.getVisibility() == View.VISIBLE){
                    holder.caducidad.setVisibility(View.GONE);
                } else holder.caducidad.setVisibility(View.VISIBLE);

                if (holder.cvv.getVisibility() == View.VISIBLE){
                    holder.cvv.setVisibility(View.GONE);
                } else holder.cvv.setVisibility(View.VISIBLE);




            }
        });



        holder.boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar Dirección")
                        .setMessage("¿Estas seguro de que quieres eliminar esta dirección?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarMetodoPago(modelMetodosPago,holder);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

    }

    private void eliminarMetodoPago(Model_Metodos_Pago modelMetodosPago, HolderMetodosPago holder) {

        String id = modelMetodosPago.getId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MetodosPago");
        databaseReference.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Dirección eliminada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Fallo al eliminar la dirección", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return metodosPagosList.size();
    }

    class HolderMetodosPago extends RecyclerView.ViewHolder{

        TextView titular, numero, caducidad, cvv, numeroMetodoPago, textoTitular;
        ImageView boton_eliminar, boton_desplegar;

        public HolderMetodosPago(@NonNull View itemView) {
            super(itemView);

            textoTitular = binding.textoTitular;
            titular = binding.titular;
            numero = binding.numeroTarjeta;
            caducidad = binding.fechaCaducidad;
            cvv = binding.CCV;
            numeroMetodoPago = binding.numeroMetodoPago;
            boton_eliminar = binding.botonEliminar;
            boton_desplegar = binding.botonDesplegar;

        }
    }
}

