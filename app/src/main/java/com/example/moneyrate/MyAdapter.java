package com.example.moneyrate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter{

    private static final String TAG = "MyAdapter";

    public MyAdapter(Context context,int resource,ArrayList<HashMap<String,String>> list){

        super(context,resource,list);

    }

    public View getView(int postion, View convertView, ViewGroup parent){
        View itemView = convertView;
        if (itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Map<String,String> map = (Map<String, String>) getItem(postion);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);

        title.setText(map.get("ItemTitle"));
        detail.setText(map.get("ItemDetail"));

        return itemView;
    }

}
