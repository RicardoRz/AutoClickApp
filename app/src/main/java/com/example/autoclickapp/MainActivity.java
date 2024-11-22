package com.example.autoclickapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar y solicitar permiso de superposición
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }

        // Botón para iniciar el servicio de burbuja flotante
        Button startBubbleButton = findViewById(R.id.startBubbleButton);
        startBubbleButton.setOnClickListener(v -> showInstructionsDialog());
    }

    private void showInstructionsDialog() {
        // Crear un cuadro de diálogo para las instrucciones
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instrucciones de uso");
        builder.setMessage("1. Presiona el botón 'Start Bubble' para abrir la burbuja flotante.\n" +
                "2. La burbuja flotante aparecerá en la pantalla.\n" +
                "3. Toca la burbuja para acceder al Autoclicker.\n" +
                "4. Usa los botones dentro del Autoclicker para iniciar/detener los clics automáticos.\n\n" +
                "¡Disfruta de la app!");

        builder.setPositiveButton("Entendido", (dialog, which) -> {
            // Inicia el servicio de burbuja flotante después de cerrar el diálogo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(MainActivity.this, FloatingBubbleService.class);
                    startService(intent);
                } else {
                    Toast.makeText(this, "Permiso de superposición necesario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        // Mostrar el cuadro de diálogo
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "Permiso de superposición otorgado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permiso de superposición denegado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
