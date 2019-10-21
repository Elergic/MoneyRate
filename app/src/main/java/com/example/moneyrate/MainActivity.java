package com.example.moneyrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "MainActivity";
    private EditText rate;
    private Button dollar;
    private Button euro;
    private Button won;
    private Button config;
    private float money;
    private float dollarrate;
    private float eurorate;
    private float wonrate;
    private Intent intent;
    private int tag;
    private float new_dol_rate;
    private float new_eur_rate;
    private float new_won_rate;
    private Handler handler;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Toast.makeText(MainActivity.this,"click Add!!!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.OpenList:
                intent = new Intent(MainActivity.this,RateListActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rate = (EditText)findViewById(R.id.rate);
        dollar = (Button)findViewById(R.id.dollar);
        euro = (Button)findViewById(R.id.euro);
        won = (Button)findViewById(R.id.won);
        config = (Button)findViewById(R.id.config);

        money = 0;
        dollarrate = (float) 0.1403;
        eurorate = (float)0.1278;
        wonrate = (float)167.9202;

        Intent back = getIntent();
        tag = back.getIntExtra("tag",0);
        new_dol_rate = back.getFloatExtra("new_dol_rate",0.0f);
        new_eur_rate = back.getFloatExtra("new_eur_rate",0.0f);
        new_won_rate = back.getFloatExtra("new_won_rate",0.0f);

        if(tag == 1){
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Context.MODE_PRIVATE);
            dollarrate = sharedPreferences.getFloat("new_dol_rate",0.0f);
            eurorate = sharedPreferences.getFloat("new_eur_rate",0.0f);
            wonrate = sharedPreferences.getFloat("new_won_rate",0.0f);
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg = " + str);
                }
                super.handleMessage(msg);
            }
        };

        Thread t = new Thread(this);
        t.start();

        dollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = Float.parseFloat(rate.getText().toString());
                if(tag == 0){
                    money = (float) (money*dollarrate);
                }
                else if(tag == 1){
                    money = (float) (money*new_dol_rate);
                }
                rate.setText(String.valueOf(money));
            }
        });

        euro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = Float.parseFloat(rate.getText().toString());
                if(tag == 0){
                    money = (float) (money*eurorate);
                }
                else if(tag == 1){
                    money = (float) (money*new_eur_rate);
                }
                rate.setText(String.valueOf(money));
            }
        });

        won.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = Float.parseFloat(rate.getText().toString());
                if(tag == 0){
                    money = (float) (money*wonrate);
                }
                else if(tag == 1){
                    money = (float) (money*new_won_rate);
                }
                rate.setText(String.valueOf(money));
            }
        });

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,RateActivity.class);
                if(tag == 0){
                    intent.putExtra("dollarrate",dollarrate);
                    intent.putExtra("eurorate",eurorate);
                    intent.putExtra("wonrate",wonrate);
                }
                else if(tag == 1){
                    intent.putExtra("dollarrate",new_dol_rate);
                    intent.putExtra("eurorate",new_eur_rate);
                    intent.putExtra("wonrate",new_won_rate);
                }
                startActivity(intent);
            }
        });



    }

    @Override
    public void run() {

        Message msg = handler.obtainMessage(7);
        msg.obj = "Hello fron run()";
        handler.sendMessage(msg);

        try{
            String url = "http://www.usd-cny.com/bankofchina.htm";
            Document doc = Jsoup.connect(url).get();
            Log.i(TAG, "run: " + doc.title());
            Elements tables = doc.getElementsByTag("table");

            Element table6 = tables.get(0);
            Elements tds = table6.getElementsByTag("td");
            for (int i = 0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td1.text();
                String val = td2.text();
                Log.i(TAG, "run: " + str1 + "==>" + val);

                float v = 100f / Float.parseFloat(val);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

}
