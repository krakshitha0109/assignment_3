package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splashactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set splash layout
        setContentView(R.layout.activity_splash);

        // Delay for 2 seconds then open MainActivity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(splashactivity.this, MainActivity.class));
            finish();
        }, 2000);
    }
}