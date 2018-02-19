package com.example.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Chronometer;

import java.io.FileDescriptor;

/**
 * Created by usuario on 16/02/18.
 */

public class BoundService extends Service {

    private Chronometer chronometer;
    IBinder iBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        chronometer = new Chronometer(this);
        chronometer.setBase(SystemClock.elapsedRealtime());
        iBinder = new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        chronometer.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chronometer.stop();
    }

    public String getTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000 / 60000);
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        int milis = (int) (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000) / 1000;

        return hours + ":" + minutes + ":" + seconds + ":" + milis;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }


    class MyBinder extends Binder {

        BoundService getService() {
            return BoundService.this;
        }

    }


}
