package com.example.autoclickapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class FloatingBubbleService extends Service {

    private WindowManager windowManager;
    private View floatingBubbleView;

    private int initialX, initialY;
    private float initialTouchX, initialTouchY;

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializar WindowManager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Inflar el diseño de la burbuja flotante
        floatingBubbleView = LayoutInflater.from(this).inflate(R.layout.floating_bubble, null, false);

        // Configurar los parámetros de la ventana
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        // Posición inicial de la burbuja
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        // Añadir la vista al WindowManager
        windowManager.addView(floatingBubbleView, params);

        // Configurar el movimiento de la burbuja
        ImageView bubbleIcon = floatingBubbleView.findViewById(R.id.bubbleIcon);
        bubbleIcon.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(floatingBubbleView, params);
                    return true;

                case MotionEvent.ACTION_UP:
                    // Abrir la actividad AutoClickerActivity al soltar la burbuja
                    Intent intent = new Intent(FloatingBubbleService.this, AutoClickerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
            }
            return false;
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingBubbleView != null) {
            windowManager.removeView(floatingBubbleView);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
