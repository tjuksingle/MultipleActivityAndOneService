package com.ksingle.appworkshop.multipleactivityandoneservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ksingle on 2017/9/9.
 */

public class OneActivity extends AppCompatActivity implements View.OnClickListener{

    private Button ask,order,client2,client3;
    private TextView fishLeft,result;

    private OrderService orderService;
    private ServiceConnection serviceConnection;
    private Intent orderIntent;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        client2 = (Button) findViewById(R.id.client2);
        client3 = (Button) findViewById(R.id.client3);
        client2.setOnClickListener(this);
        client3.setOnClickListener(this);

        ask = (Button) findViewById(R.id.ask);
        order = (Button) findViewById(R.id.order);
        fishLeft = (TextView) findViewById(R.id.fishleft);
        result = (TextView) findViewById(R.id.result);

        ask.setOnClickListener(this);
        order.setOnClickListener(this);

        orderIntent = new Intent(OneActivity.this,OrderService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                orderService = ((OrderService.ServiceBinder)iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(orderIntent,serviceConnection, Context.BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.fishleft");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.nofishleft");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.ordersuccess");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.orderfail");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action){
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.fishleft":
                        String onSale = "Fish on sale: "+intent.getIntExtra("howMany",0);
                        fishLeft.setText(onSale);
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.nofishleft":
                        fishLeft.setText("No fish left.");
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.ordersuccess":
                        result.setText("Order success");
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.orderfail":
                        result.setText("Order not success");
                        break;
                }
            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        orderService = null;
        unbindService(serviceConnection);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(ask)){
            Intent intent = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.ask");
            sendBroadcast(intent);
        }else if (v.equals(order)){
            Intent intent = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.order");
            sendBroadcast(intent);
        }else if (v.equals(client2)){
            Intent intent = new Intent(OneActivity.this,TwoActivity.class);
            startActivity(intent);
        }else if (v.equals(client3)){
            Intent intent = new Intent(OneActivity.this,ThreeActivity.class);
            startActivity(intent);
        }
    }
}
