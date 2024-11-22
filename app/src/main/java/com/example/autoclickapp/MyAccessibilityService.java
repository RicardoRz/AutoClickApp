package com.example.autoclickapp;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // No se necesita lógica específica aquí por ahora
    }

    @Override
    public void onInterrupt() {
        // Método obligatorio, pero no se necesita lógica
    }
}
