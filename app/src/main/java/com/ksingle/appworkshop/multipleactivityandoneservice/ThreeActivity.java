package com.ksingle.appworkshop.multipleactivityandoneservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ksingle on 2017/9/9.
 */

public class ThreeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button ask,sell,client1,client2;
    private TextView fishNeed,result;
    private EditText num;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        client1 = (Button) findViewById(R.id.client1);
        client2 = (Button) findViewById(R.id.client2);
        client1.setOnClickListener(this);
        client2.setOnClickListener(this);

        ask = (Button) findViewById(R.id.ask);
        sell = (Button) findViewById(R.id.sell);
        fishNeed = (TextView) findViewById(R.id.fishneed);
        result = (TextView) findViewById(R.id.result);

        ask.setOnClickListener(this);
        sell.setOnClickListener(this);

        num = (EditText) findViewById(R.id.num);
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.fishneed");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.noneed");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.buyall");
        intentFilter.addAction("com.ksingle.appworkshop.multipleactivityandoneservice.buysome");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action){
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.fishneed":
                        String onSale = "Fish need: "+intent.getIntExtra("howMany",0);
                        fishNeed.setText(onSale);
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.noneed":
                        result.setText("No fish need.");
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.buyall":
                        result.setText("You sell them all!");
                        break;
                    case "com.ksingle.appworkshop.multipleactivityandoneservice.buysome":
                        //int howMany = intent.getIntExtra("howMany",0);
                        String howMany = "You can sell "+intent.getIntExtra("howMany",0) +" fish.";
                        result.setText(howMany);
                        break;
                }
            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(ask)){
            Intent intent = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.howmanyfishneed");
            sendBroadcast(intent);
        }else if (v.equals(sell)){
            int howMany = Integer.parseInt(num.getText().toString());
            Log.i("Info", "onClick: "+howMany);
            if (howMany > 0){
                Intent intent = new Intent("com.ksingle.appworkshop.multipleactivityandoneservice.sellfish");
                intent.putExtra("howMany",howMany);
                sendBroadcast(intent);
            }else if (howMany <0){
                Toast.makeText(this,"Please setup a positive number in the EditText.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Please setup a number in the EditText.",Toast.LENGTH_SHORT).show();
            }
        }else if (v.equals(client1)){
            finish();
        }else if (v.equals(client2)){
            Intent intent = new Intent(this,TwoActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
