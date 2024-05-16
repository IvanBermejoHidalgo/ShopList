package com.example.socialpuig.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.socialpuig.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Escuchar los cambios en la base de datos de usuarios
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numeroUsuarios = dataSnapshot.getChildrenCount();

                // Actualizar la UI con el número de usuarios
                //TextView numeroUsuariosTextView = findViewById(R.id.UsuariosTextView);
                //numeroUsuariosTextView.setText("Número de usuarios registrados: " + numeroUsuarios);



                // GRÁFICO DE ESFERA (PIE CHART) PARA COMPRAS POR GÉNERO

                AnyChartView pieChartView = findViewById(R.id.any_chart_view);
                pieChartView.setProgressBar(findViewById(R.id.progress_bar));

                Pie pie = AnyChart.pie();

                pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
                    @Override
                    public void onClick(Event event) {
                        Toast.makeText(UsuariosActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
                    }
                });

                List<DataEntry> pieData = new ArrayList<>();
                pieData.add(new ValueDataEntry("Usuarios", numeroUsuarios));
                //pieData.add(new ValueDataEntry("Mujer", mujerPurchases));

                pie.data(pieData);

                pie.title("USUARIOS GENERADOS");

                pie.labels().position("outside");

                pie.legend().title().enabled(true);
                pie.legend().title()
                        .text("Género")
                        .padding(0d, 0d, 10d, 0d);

                pie.legend()
                        .position("center-bottom")
                        .itemsLayout(LegendLayout.HORIZONTAL)
                        .align(Align.CENTER);

                pieChartView.setChart(pie);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminActivity", "Error al leer los datos de usuarios", databaseError.toException());
            }
        });


    }

    @Override
    public void onBackPressed() {
        // Lógica para volver a la pantalla anterior
        super.onBackPressed();
    }
}