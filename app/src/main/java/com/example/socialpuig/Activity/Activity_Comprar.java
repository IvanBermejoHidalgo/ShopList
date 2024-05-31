package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.socialpuig.Domain.ItemsDomain;
import com.example.socialpuig.Helper.ManagmentCart;
import com.example.socialpuig.R;
import com.example.socialpuig.databinding.ActivityComprarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Activity_Comprar extends AppCompatActivity {

    ActivityComprarBinding binding;
    private ArrayList<String> direccionesEnvioList, metodosPagoList;
    //private ArrayList<Model_Camisetas_Tienda> camisetasTiendaList;
    private ArrayList<ItemsDomain> camisetasTiendaList;
    double precioTotal;
    private FirebaseAuth firebaseAuth;
    private double tax;
    private ManagmentCart managmentCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityComprarBinding.inflate(getLayoutInflater())).getRoot());


        firebaseAuth = FirebaseAuth.getInstance();

        cargarDatosUsuario();
        cargarDireccionesEnvio();
        cargarMetodosPago();

        // Obtener el precio total del intent
        double precioTotalCarrito = getIntent().getDoubleExtra("precio_total", 0.0);

        // Configurar el precio total en el TextView
        binding.precioTotal.setText(String.valueOf(precioTotalCarrito));


        binding.botonCerrarVentana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                startActivity(new Intent(Activity_Comprar.this, TiendaActivity.class));
            }
        });

        binding.botonPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.direccionEnvioConfirmar.getText().toString().trim())){
                    Toast.makeText(Activity_Comprar.this, "Introduce una dirección de envío válida para tramitar el pedido", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.metodoPagoConfirmar.getText().toString().trim())) {
                    Toast.makeText(Activity_Comprar.this, "Introduce un metodo de pago válido para tramitar el pedido", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Activity_Comprar.this, Activity_Tramitando_Pedido.class));
                }
            }
        });

        binding.direccionEnvioExistente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerDireccionEnvio();
            }
        });

        binding.metodoPagoExistente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerMetodosPago();
            }
        });

        binding.crearDireccionEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Comprar.this, Activity_Direcciones_Envio.class));
            }
        });

        binding.crearMetodoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Comprar.this, Activity_Metodos_Pago.class));
            }
        });

        binding.botonDesplegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.direccionEnvio.getVisibility() == View.VISIBLE){
                    binding.direccionEnvio.setVisibility(View.GONE);
                    binding.botonDesplegar.setImageResource(R.drawable.icono_desplegar);
                } else{
                    binding.botonDesplegar.setImageResource(R.drawable.icono_replegar);
                    binding.direccionEnvio.setVisibility(View.VISIBLE);
                }

                if (binding.direccionEnvioExistente.getVisibility() == View.VISIBLE){
                    binding.direccionEnvioExistente.setVisibility(View.GONE);
                } else binding.direccionEnvioExistente.setVisibility(View.VISIBLE);

                if (binding.crearDireccionEnvio.getVisibility() == View.VISIBLE){
                    binding.crearDireccionEnvio.setVisibility(View.GONE);
                } else binding.crearDireccionEnvio.setVisibility(View.VISIBLE);

                if (binding.datosDireccionEnvio.getVisibility() == View.VISIBLE){
                    binding.datosDireccionEnvio.setVisibility(View.GONE);
                } else binding.datosDireccionEnvio.setVisibility(View.VISIBLE);

                if (binding.direccionEnvioFinal.getVisibility() == View.VISIBLE){
                    binding.direccionEnvioFinal.setVisibility(View.GONE);
                } else binding.direccionEnvioFinal.setVisibility(View.VISIBLE);
            }
        });

        binding.botonDesplegar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.metodoPago.getVisibility() == View.VISIBLE){
                    binding.metodoPago.setVisibility(View.GONE);
                    binding.botonDesplegar2.setImageResource(R.drawable.icono_desplegar);
                } else{
                    binding.botonDesplegar2.setImageResource(R.drawable.icono_replegar);
                    binding.metodoPago.setVisibility(View.VISIBLE);
                }

                if (binding.metodoPagoExistente.getVisibility() == View.VISIBLE){
                    binding.metodoPagoExistente.setVisibility(View.GONE);
                } else binding.metodoPagoExistente.setVisibility(View.VISIBLE);

                if (binding.crearMetodoPago.getVisibility() == View.VISIBLE){
                    binding.crearMetodoPago.setVisibility(View.GONE);
                } else binding.crearMetodoPago.setVisibility(View.VISIBLE);

                if (binding.datosMetodoPago.getVisibility() == View.VISIBLE){
                    binding.datosMetodoPago.setVisibility(View.GONE);
                } else binding.datosMetodoPago.setVisibility(View.VISIBLE);

                if (binding.metodoPagoFinal.getVisibility() == View.VISIBLE){
                    binding.metodoPagoFinal.setVisibility(View.GONE);
                } else binding.metodoPagoFinal.setVisibility(View.VISIBLE);
            }
        });

        binding.botonDesplegar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.datosPersonales.getVisibility() == View.VISIBLE){
                    binding.datosPersonales.setVisibility(View.GONE);
                    binding.botonDesplegar3.setImageResource(R.drawable.icono_desplegar);
                } else {
                    binding.botonDesplegar3.setImageResource(R.drawable.icono_replegar);
                    binding.datosPersonales.setVisibility(View.VISIBLE);
                }

                if (binding.nombreUsuario.getVisibility() == View.VISIBLE){
                    binding.nombreUsuario.setVisibility(View.GONE);
                } else binding.nombreUsuario.setVisibility(View.VISIBLE);

                if (binding.correoElectronico.getVisibility() == View.VISIBLE){
                    binding.correoElectronico.setVisibility(View.GONE);
                } else binding.correoElectronico.setVisibility(View.VISIBLE);

                if (binding.confirmarDireccion.getVisibility() == View.VISIBLE){
                    binding.confirmarDireccion.setVisibility(View.GONE);
                } else binding.confirmarDireccion.setVisibility(View.VISIBLE);

                if (binding.direccionEnvioConfirmar.getVisibility() == View.VISIBLE){
                    binding.direccionEnvioConfirmar.setVisibility(View.GONE);
                } else binding.direccionEnvioConfirmar.setVisibility(View.VISIBLE);

                if (binding.confirmarMetodo.getVisibility() == View.VISIBLE){
                    binding.confirmarMetodo.setVisibility(View.GONE);
                } else binding.confirmarMetodo.setVisibility(View.VISIBLE);

                if (binding.metodoPagoConfirmar.getVisibility() == View.VISIBLE){
                    binding.metodoPagoConfirmar.setVisibility(View.GONE);
                } else binding.metodoPagoConfirmar.setVisibility(View.VISIBLE);

                if (binding.botonPagar.getVisibility() == View.VISIBLE){
                    binding.botonPagar.setVisibility(View.GONE);
                } else binding.botonPagar.setVisibility(View.VISIBLE);

            }
        });


    }

    private void cargarDireccionesEnvio() {
        direccionesEnvioList = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }
        String userUID = user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userUID).child("DireccionesEnvio");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                direccionesEnvioList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String calle = dataSnapshot.child("calle").getValue(String.class);
                    String numero = dataSnapshot.child("numero").getValue(String.class);
                    String piso = dataSnapshot.child("piso").getValue(String.class);
                    String puerta = dataSnapshot.child("puerta").getValue(String.class);
                    String localidad = dataSnapshot.child("localidad").getValue(String.class);

                    String direccion = "Calle " + calle + " nº " + numero + " piso " + piso + " puerta " + puerta + " " + localidad;
                    direccionesEnvioList.add(direccion);
                }

                // Habilitar el botón y permitir la selección de dirección solo si hay datos
                if (!direccionesEnvioList.isEmpty()) {
                    binding.direccionEnvioExistente.setEnabled(true); // Habilitar el botón
                } else {
                    Toast.makeText(Activity_Comprar.this, "No hay direcciones de envío disponibles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_Comprar.this, "Error al cargar direcciones", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String direccionSeleccionada;
    private void escogerDireccionEnvio() {

        String[] arrayCategorias = new String[direccionesEnvioList.size()];
        for (int i = 0; i < direccionesEnvioList.size(); i++) {
            arrayCategorias[i] = direccionesEnvioList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escoge una dirección de envío")
                .setItems(arrayCategorias, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        direccionSeleccionada = direccionesEnvioList.get(which);
                        binding.direccionEnvioFinal.setText(direccionSeleccionada);
                        binding.direccionEnvioConfirmar.setText(direccionSeleccionada);
                    }
                }).show();
    }

    private void cargarMetodosPago() {
        metodosPagoList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }
        String userUID = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userUID).child("MetodosPago");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                metodosPagoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String metodo = "Titular:  " + dataSnapshot.child("titular").getValue() + "\nNumero de la tarjeta: " + dataSnapshot.child("numero").getValue()
                            + "\nFecha de caducidad: " + dataSnapshot.child("caducidad").getValue() + "\nCVV " + dataSnapshot.child("cvv").getValue();


                    metodosPagoList.add(metodo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String metodoSeleccionado;
    private void escogerMetodosPago() {

        String[] arrayCategorias = new String[metodosPagoList.size()];
        for (int i = 0; i < metodosPagoList.size(); i++) {
            arrayCategorias[i] = metodosPagoList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escoge un metodo de pago")
                .setItems(arrayCategorias, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        metodoSeleccionado = metodosPagoList.get(which);
                        binding.metodoPagoFinal.setText(metodoSeleccionado);
                        binding.metodoPagoConfirmar.setText(metodoSeleccionado);
                    }
                }).show();
    }

    private void cargarDatosUsuario() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombre_usuario = "" + snapshot.child("name").getValue();
                        String correo_usuario = "" + snapshot.child("email").getValue();


                        binding.nombreUsuario.setText("Nombre: " + nombre_usuario);
                        binding.correoElectronico.setText("Correo eléctronico: " + correo_usuario);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}