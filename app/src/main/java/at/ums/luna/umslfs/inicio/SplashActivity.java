package at.ums.luna.umslfs.inicio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.ums.luna.umslfs.R;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Runnable ejecutable = new Runnable() {
            @Override
            public void run() {
                actividadPrincipal();
                finish();
            }
        };

        Handler miHandler = new Handler();
        miHandler.postDelayed(ejecutable, 1500);

    }
    public void actividadPrincipal(){
        Intent intento = new Intent(this, MainActivity.class);
        startActivity(intento);
    }

}
