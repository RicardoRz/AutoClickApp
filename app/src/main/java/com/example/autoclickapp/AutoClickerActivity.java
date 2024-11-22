package com.example.autoclickapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AutoClickerActivity extends AppCompatActivity {

    private boolean isAutoClicking = false; // Estado del autoclicker
    private int clickCount = 0; // Contador de clics
    private int autoClickInterval = 100; // Intervalo inicial del autoclicker (en milisegundos)

    private Handler handler;
    private Runnable autoClickRunnable;

    private TextView clickCounter; // Muestra la cantidad de clics
    private TextView speedLabel; // Muestra la velocidad seleccionada
    private Button toggleAutoClickButton; // Botón para iniciar/detener el autoclicker
    private Button manualClickButton; // Botón para clic manual

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoclicker);

        // Referencias de UI
        clickCounter = findViewById(R.id.clickCounter);
        speedLabel = findViewById(R.id.speedLabel);
        SeekBar speedSeekBar = findViewById(R.id.speedSeekBar);
        toggleAutoClickButton = findViewById(R.id.toggleAutoClickButton);
        manualClickButton = findViewById(R.id.manualClickButton);

        handler = new Handler();

        // Configurar el Runnable del autoclicker
        autoClickRunnable = new Runnable() {
            @Override
            public void run() {
                clickCount++;
                clickCounter.setText("Toques: " + clickCount);
                handler.postDelayed(this, autoClickInterval);
            }
        };

        // Configurar la barra deslizante para ajustar la velocidad
        speedSeekBar.setMax(500); // Máximo: 500 ms
        speedSeekBar.setProgress(autoClickInterval);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                autoClickInterval = progress + 50; // Evitar valores menores a 50 ms
                speedLabel.setText("Velocidad: " + autoClickInterval + "ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Botón para clic manual
        manualClickButton.setOnClickListener(v -> {
            clickCount++;
            clickCounter.setText("Toques: " + clickCount);
        });

        // Botón para iniciar/detener el autoclicker
        toggleAutoClickButton.setOnClickListener(v -> {
            if (isAutoClicking) {
                stopAutoClicker();
            } else {
                startAutoClicker();
            }
        });
    }

    private void startAutoClicker() {
        isAutoClicking = true;
        toggleAutoClickButton.setText("Detener Multitap");
        handler.post(autoClickRunnable);
    }

    private void stopAutoClicker() {
        isAutoClicking = false;
        toggleAutoClickButton.setText("Iniciar Multitap");
        handler.removeCallbacks(autoClickRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoClickRunnable); // Detener autoclicker al destruir la actividad
    }
}
