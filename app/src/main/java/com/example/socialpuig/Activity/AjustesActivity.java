package com.example.socialpuig.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.socialpuig.R;

public class AjustesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());



    }

    @Override
    public void onBackPressed() {
        // Lógica para volver a la pantalla anterior
        super.onBackPressed();
    }
}