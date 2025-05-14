package com.galvan.ubicationtest.Activity.SplashActivity;



import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ade.accessControl.manager.PermissionsManager;
import com.galvan.ubicationtest.R;

public class SplashActivity extends AppCompatActivity {
    private PermissionsManager permissionsManager;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        permissionsManager = new PermissionsManager(this);
        message = findViewById(R.id.messageSplash);
        permissionsManager.checkAndRequestPermissions();
        permissionsManager.getAlert().observe(this, it -> {
            if (it) {
                int[] seconds = {8};
                Thread thread = new Thread(() -> {
                    while (true) {
                        seconds[0]--;
                        runOnUiThread(() -> {
                            if (message != null) {
                                message.setText(getString(R.string.denied_permiss) + " " + seconds[0]);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (seconds[0] <= 1) {
                            finish();
                            break;
                        }
                    }
                });
                thread.start();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.handlePermissionsResult(requestCode, permissions, grantResults);
    }
}