package com.example.socialpuig.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialpuig.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    int nikePurchases = 0;
    int zaraPurchases = 0;
    int adidasPurchases = 0;
    int pumaPurchases = 0;
    int gucciPurchases = 0;
    int hombrePurchases = 0;
    int mujerPurchases = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Obtener la referencia de la base de datos para las compras
        DatabaseReference comprasRef = FirebaseDatabase.getInstance().getReference().child("Compras");

        // Escuchar los cambios en la base de datos de compras
        comprasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalCompras = 0;
                nikePurchases = 0;
                zaraPurchases = 0;
                adidasPurchases = 0;
                pumaPurchases = 0;
                gucciPurchases = 0;
                hombrePurchases = 0;
                mujerPurchases = 0;

                for (DataSnapshot compraSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot totalCompraSnapshot = compraSnapshot.child("TotalCompra");
                    if (totalCompraSnapshot.exists()) {
                        int totalCompra = totalCompraSnapshot.getValue(Integer.class);
                        totalCompras += totalCompra;
                    }

                    for (DataSnapshot productSnapshot : compraSnapshot.getChildren()) {
                        String brand = productSnapshot.child("brand").getValue(String.class);
                        if ("nike".equalsIgnoreCase(brand)) {
                            nikePurchases++;
                        } else if ("zara".equalsIgnoreCase(brand)) {
                            zaraPurchases++;
                        } else if ("adidas".equalsIgnoreCase(brand)) {
                            adidasPurchases++;
                        } else if ("puma".equalsIgnoreCase(brand)) {
                            pumaPurchases++;
                        } else if ("gucci".equalsIgnoreCase(brand)) {
                            gucciPurchases++;
                        }
                    }

                    for (DataSnapshot productSnapshot : compraSnapshot.getChildren()) {
                        String brand = productSnapshot.child("gender").getValue(String.class);
                        if ("hombre".equalsIgnoreCase(brand)) {
                            hombrePurchases++;
                        } else if ("mujer".equalsIgnoreCase(brand)) {
                            mujerPurchases++;
                        }
                    }
                }

                // Actualizar la UI con el total de compras
                TextView totalComprasTextView = findViewById(R.id.totalComprasTextView);
                totalComprasTextView.setText("Total de dinero generado: " + totalCompras + "€");

                // Actualizar la UI con el número de compras de productos "nike"
                /*TextView nikeComprasTextView = findViewById(R.id.nikeComprasTextView);
                nikeComprasTextView.setText("Compras de productos Nike: " + nikePurchases);

                // Actualizar la UI con el número de compras de productos "zara"
                TextView zaraComprasTextView = findViewById(R.id.zaraComprasTextView);
                zaraComprasTextView.setText("Compras de productos Zara: " + zaraPurchases);

                // Actualizar la UI con el número de compras de productos "adidas"
                TextView adidasComprasTextView = findViewById(R.id.adidasComprasTextView);
                adidasComprasTextView.setText("Compras de productos Adidas: " + adidasPurchases);

                // Actualizar la UI con el número de compras de productos "puma"
                TextView pumaComprasTextView = findViewById(R.id.pumaComprasTextView);
                pumaComprasTextView.setText("Compras de productos Puma: " + pumaPurchases);

                // Actualizar la UI con el número de compras de productos "gucci"
                TextView gucciComprasTextView = findViewById(R.id.gucciComprasTextView);
                gucciComprasTextView.setText("Compras de productos Gucci: " + gucciPurchases);*/

                // Actualizar la UI con el número de compras de productos "puma"
                TextView hombreComprasTextView = findViewById(R.id.hombreComprasTextView);
                hombreComprasTextView.setText("Compras de productos Hombre: " + hombrePurchases);

                // Actualizar la UI con el número de compras de productos "puma"
                TextView mujerComprasTextView = findViewById(R.id.mujerComprasTextView);
                mujerComprasTextView.setText("Compras de productos Mujer: " + mujerPurchases);


                // GRÁFICO DE BARRAS DE COMPRAS POR MARCAS

                AnyChartView anyChartView = findViewById(R.id.any_chart_view);
                anyChartView.setProgressBar(findViewById(R.id.progress_bar));

                Cartesian cartesian = AnyChart.column();

                List<DataEntry> data = new ArrayList<>();
                data.add(new ValueDataEntry("Nike", nikePurchases));
                data.add(new ValueDataEntry("Adidas", adidasPurchases));
                data.add(new ValueDataEntry("Puma", pumaPurchases));
                data.add(new ValueDataEntry("Zara", zaraPurchases));
                data.add(new ValueDataEntry("Gucci", gucciPurchases));
                //data.add(new ValueDataEntry("Nail polish", 143760));
                //data.add(new ValueDataEntry("Eyebrow pencil", 170670));
                //data.add(new ValueDataEntry("Eyeliner", 213210));
                //data.add(new ValueDataEntry("Eyeshadows", 249980));

                Column column = cartesian.column(data);

                column.tooltip()
                        .titleFormat("{%X}")
                        .position(Position.CENTER_BOTTOM)
                        .anchor(Anchor.CENTER_BOTTOM)
                        .offsetX(0d)
                        .offsetY(5d)
                        .format("{%Value}{groupsSeparator: }");

                cartesian.animation(true);
                cartesian.title("VENTAS POR MARCAS");

                cartesian.yScale().minimum(0d);

                cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

                cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                cartesian.interactivity().hoverMode(HoverMode.BY_X);

                cartesian.xAxis(0).title("Product");
                cartesian.yAxis(0).title("Revenue");

                anyChartView.setChart(cartesian);






                // GRÁFICO DE ESFERA (PIE CHART) PARA COMPRAS POR GÉNERO

                /*AnyChartView pieChartView2 = findViewById(R.id.pie_chart_view);
                pieChartView2.setProgressBar(findViewById(R.id.progress_bar));

                Pie pie = AnyChart.pie();

                pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
                    @Override
                    public void onClick(Event event) {
                        Toast.makeText(AdminActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
                    }
                });

                List<DataEntry> pieData = new ArrayList<>();
                pieData.add(new ValueDataEntry("Hombre", hombrePurchases));
                pieData.add(new ValueDataEntry("Mujer", mujerPurchases));

                pie.data(pieData);

                pie.title("VENTAS POR GÉNERO");

                pie.labels().position("outside");

                pie.legend().title().enabled(true);
                pie.legend().title()
                        .text("Género")
                        .padding(0d, 0d, 10d, 0d);

                pie.legend()
                        .position("center-bottom")
                        .itemsLayout(LegendLayout.HORIZONTAL)
                        .align(Align.CENTER);

                pieChartView2.setChart(pie);*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error al leer los datos de compras", databaseError.toException());
            }
        });

        // Obtener la referencia de la base de datos para los usuarios
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Escuchar los cambios en la base de datos de usuarios
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numeroUsuarios = dataSnapshot.getChildrenCount();

                // Actualizar la UI con el número de usuarios
                TextView numeroUsuariosTextView = findViewById(R.id.numeroUsuariosTextView);
                numeroUsuariosTextView.setText("Número de usuarios registrados: " + numeroUsuarios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error al leer los datos de usuarios", databaseError.toException());
            }
        });

        // Obtener la referencia de la base de datos para el número de pedidos
        comprasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numPedidos = dataSnapshot.getChildrenCount();

                // Actualizar la UI con el número de pedidos
                TextView numeroPedidosTextView = findViewById(R.id.numeroPedidosTextView);
                numeroPedidosTextView.setText("Número de pedidos: " + numPedidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error al leer los datos de compras", databaseError.toException());
            }
        });




    }
}
