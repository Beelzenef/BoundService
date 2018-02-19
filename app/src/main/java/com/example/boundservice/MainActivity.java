package com.example.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Este Service nos ofrece un contador de tiempo
    private BoundService boundService;

    // Este campo indica si estamos vinculados al Service
    boolean isBound;

    // Necesitaremos también un ServiceConnection para gestionar la conexión con el Servicio
    private ServiceConnection serviceConnection;

    private Button btn_DameTiempo;
    private Button btn_StopTiempo;
    private TextView txtV_Tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtV_Tiempo = (TextView) findViewById(R.id.txtV_Tiempo);

        btn_DameTiempo = (Button) findViewById(R.id.btn_DameTiempo);
        btn_DameTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBound) {
                    txtV_Tiempo.setVisibility(View.VISIBLE);
                    txtV_Tiempo.setText(boundService.getTime());
                    btn_StopTiempo.setEnabled(true);
                    btn_DameTiempo.setEnabled(false);
                }
            }
        });

        btn_StopTiempo = (Button) findViewById(R.id.btn_StopTiempo);
        btn_StopTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBound) {
                    isBound = false;

                    stopServiceBound();
                    txtV_Tiempo.setText("Sin tiempo!");
                    btn_DameTiempo.setEnabled(true);
                    btn_StopTiempo.setEnabled(false);
                }
            }
        });

        isBound = false;
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                boundService = ((BoundService.MyBinder) iBinder).getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
    }

    // La diferencia entre un Service normal y un Service Bounded, es que necesita el proceso
    // de binding, en diferentes momentos de la aplicación, en onStart() o onStop(), cuando
    // clicamos en un botón u otro

    @Override
    protected void onStart() {
        super.onStart();

        startServiceBound();
    }

    // Es obligatorio en onStop desvincular el Service de la Activity

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            stopServiceBound();
        }
    }

    // Método que inicia el Service

    // Si se inicia mediante startService(), el Service que se inicia en serviceConnection es null
    // Se inicializa directamente con bindService()

    private void startServiceBound() {
        Intent intent = new Intent(this, BoundService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopServiceBound() {
        Intent intent = new Intent(this, BoundService.class);
        //stopService(intent);
        unbindService(serviceConnection);
        stopService(intent);
    }

    // La Activity recoge un servicio concreto que ofrece en este caso el Service BoundService
    // No es un servicio genérico, es concreto
    // La Activity necesitará por tanto ese service
}
