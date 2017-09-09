package com.ksingle.appworkshop.multipleactivityandoneservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Ksingle on 2017/9/9.
 */

public class OrderService extends Service {

    private BroadcastReceiver broadcastReceiver;

    private int fish = 5;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.ask");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.order");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.howmanyfishneed");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.sellfish");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                switch(action){
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.ask":
                        ask();
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.order":
                        order();
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.howmanyfishneed":
                        Intent fishNeed = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.fishneed");
                        fishNeed.putExtra("howMany",20-fish);
                        sendBroadcast(fishNeed);
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.sellfish":
                        int howMany = intent.getIntExtra("howMany",0);
                        Log.i("Info", "onReceive: "+howMany);
                        buySomeFish(howMany);
                }
            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
    }

    private void ask(){
        if(fish > 0){
            Intent fishLeft = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.fishleft");
            fishLeft.putExtra("howMany",fish);
            sendBroadcast(fishLeft);
        }else {
            Intent noFishLeft = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.nofishleft");
            sendBroadcast(noFishLeft);
        }
    }

    private void order(){
        if(fish > 0){
            fish = fish-1;
            Intent fishLeft = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.ordersuccess");
            sendBroadcast(fishLeft);
        }else {
            Intent noFishLeft = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.orderfail");
            sendBroadcast(noFishLeft);
        }
    }

    private void buySomeFish(int howMany){
        int fishNeed = 20-fish; //the max number of the fish is 20.
        if (fishNeed  <= 0){
            Intent noNeed =new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.noneed");
            sendBroadcast(noNeed);
        }else if (fishNeed > howMany){
            fish = fish+ howMany;
            Intent buyAll = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.buyall");
            sendBroadcast(buyAll);
        }else if (fishNeed <howMany){
            fish  = fishNeed+fish;//or fish = 20;
            Intent buySome = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.buysome");
            buySome.putExtra("howMany",fishNeed);
            sendBroadcast(buySome);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    class ServiceBinder extends Binder{
        OrderService getService(){
            return OrderService.this;
        }
    }
}
