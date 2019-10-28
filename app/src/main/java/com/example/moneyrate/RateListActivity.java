package com.example.moneyrate;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable, AdapterView.OnItemClickListener {

    private String TAG = "RateListAtivity";
    private Handler handler;
    private ArrayList<HashMap<String,String>> listItems;
    private Adapter listItemAdapter;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRatedateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        SharedPreferences sp = getSharedPreferences("myrate",Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY,"");
        Log.i(TAG, "lastRatedateStr = " + logDate);


        List<String> list1 = new ArrayList<String>();
        for (int i = 1;i<100;i++){
            list1.add("item" + i);
        }
        String[] list_data = {"wait......"};
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_data);
        setListAdapter(adapter);

        Thread thread = new Thread(this);
        thread.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 7){
                    ArrayList<HashMap<String,String>> list2 = (ArrayList<HashMap<String, String>>) msg.obj;
                    MyAdapter myAdapter = new MyAdapter(RateListActivity.this,R.layout.list_item,list2);
                    setListAdapter(myAdapter);
                }
                super.handleMessage(msg);
            }
        };

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void run() {
        ArrayList<HashMap<String,String>> retList = new ArrayList<HashMap<String, String>>();

        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i(TAG, "curDateStr:" + curDateStr + "logDate:" + logDate);

        if (curDateStr.equals(logDate)){
            Log.i(TAG, "run: 日期相等，从数据库中获取数据");
            RateManager manager = new RateManager(this);
            for (RateItem item : manager.listAll()){
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("ItemTitle",item.getCurName());
                map.put("ItemDetail",item.getCurRate());
                retList.add(map);
            }
        }
        else {
            Log.i(TAG, "run: 日期相等，从网络中获取数据");
            try{
                Thread.sleep(3000);

                String url = "http://www.usd-cny.com/bankofchina.htm";
                Document doc = Jsoup.connect(url).get();
                Elements tables = doc.getElementsByTag("table");

                Element table6 = tables.get(0);
                Elements tds = table6.getElementsByTag("td");

                List<RateItem> rateList = new ArrayList<RateItem>();

                for (int i = 0;i<tds.size();i+=6){
                    HashMap<String,String> map = new HashMap<String,String>();
                    Element td1 = tds.get(i);
                    Element td2 = tds.get(i+5);

                    String str1 = td1.text();
                    String val = td2.text();

                    map.put("ItemTitle",str1);
                    map.put("ItemDetail",val);
                    retList.add(map);

                    rateList.add(new RateItem(str1,val));
                }

                RateManager manager = new RateManager(this);
                manager.addAll(rateList);

                SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(DATE_SP_KEY,curDateStr);
                edit.apply();

            }
            catch (IOException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Object itemAtPosition = getListView().getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String,String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");

        Log.i(TAG, "onItemClick: titleStr = " + titleStr);
        Log.i(TAG, "onItemClick: detailStr = " + detailStr);

        TextView title = (TextView) view.findViewById(R.id.itemTitle);
        TextView detail = (TextView) view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());

        Log.i(TAG, "onItemClick: title2 = " + title2);
        Log.i(TAG, "onItemClick: detail2 = " + detail2);

        Intent rateCalc = new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);
    }
}
