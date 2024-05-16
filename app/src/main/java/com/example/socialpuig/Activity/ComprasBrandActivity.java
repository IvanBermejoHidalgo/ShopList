package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.socialpuig.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComprasBrandActivity extends AppCompatActivity {
    int nikePurchases = 0;
    int zaraPurchases = 0;
    int adidasPurchases = 0;
    int pumaPurchases = 0;
    int gucciPurchases = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras_brand);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        // Obtener la referencia de la base de datos para las compras
        DatabaseReference comprasRef = FirebaseDatabase.getInstance().getReference().child("Compras");

        // Escuchar los cambios en la base de datos de compras
        comprasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nikePurchases = 0;
                zaraPurchases = 0;
                adidasPurchases = 0;
                pumaPurchases = 0;
                gucciPurchases = 0;

                for (DataSnapshot compraSnapshot : dataSnapshot.getChildren()) {

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

                }

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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error al leer los datos de compras", databaseError.toException());
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Lógica para volver a la pantalla anterior
        super.onBackPressed();
    }
}