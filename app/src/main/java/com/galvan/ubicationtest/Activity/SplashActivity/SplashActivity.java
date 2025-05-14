package com.galvan.ubicationtest.Activity.SplashActivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ade.accessControl.manager.PermissionsManager;
import com.galvan.ubicationtest.R;

public class SplashActivity extends AppCompatActivity {

    // Administrador de permisos (clase personalizada)
    private PermissionsManager permissionsManager;

    // Texto en pantalla para mostrar mensajes al usuario
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asocia el layout activity_splash a esta Activity
        setContentView(R.layout.activity_splash);

        // Inicializa el manejador de permisos
        permissionsManager = new PermissionsManager(this);

        // Obtiene la referencia al TextView que muestra el mensaje
        message = findViewById(R.id.messageSplash);

        // Inicia la verificación de permisos (muestra el diálogo si hace falta)
        permissionsManager.checkAndRequestPermissions();

        // Observa el estado de alerta (cuando el usuario niega permisos)
        permissionsManager.getAlert().observe(this, it -> {
            if (it) {
                // Si los permisos fueron denegados, comienza cuenta regresiva para cerrar la app
                int[] seconds = {8}; // Cuenta regresiva de 8 segundos

                // Crea un hilo secundario para hacer el conteo
                Thread thread = new Thread(() -> {
                    while (true) {
                        seconds[0]--; // Decrementa segundos

                        // Actualiza el UI desde el hilo principal
                        runOnUiThread(() -> {
                            if (message != null) {
                                // Muestra mensaje con segundos restantes
                                message.setText(getString(R.string.denied_permiss) + " " + seconds[0]);
                            }
                        });

                        // Espera 1 segundo antes de repetir
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Cuando llega a 1 segundo, cierra la Activity
                        if (seconds[0] <= 1) {
                            finish(); // Termina la actividad (cierra la app)
                            break;
                        }
                    }
                });

                // Inicia el hilo de cuenta regresiva
                thread.start();
            }
        });
    }

    // Este método maneja la respuesta del usuario al pedir permisos (sí o no)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Pasa los resultados al PermissionsManager para que los procese
        permissionsManager.handlePermissionsResult(requestCode, permissions, grantResults);
    }
}
