package com.example.moneyrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RateActivity extends AppCompatActivity {

    private float dollarrate;
    private float eurorate;
    private float wonrate;
    private EditText new_dol_rate;
    private EditText new_eur_rate;
    private EditText new_won_rate;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        Intent intent = getIntent();
        dollarrate = intent.getFloatExtra("dollarrate",0.0f);
        eurorate = intent.getFloatExtra("eurorate",0.0f);
        wonrate = intent.getFloatExtra("wonrate",0.0f);

        new_dol_rate = (EditText)findViewById(R.id.new_dol_rate);
        new_eur_rate = (EditText)findViewById(R.id.new_eur_rate);
        new_won_rate = (EditText)findViewById(R.id.new_won_rate);
        save = (Button)findViewById(R.id.save);

        new_dol_rate.setText(String.valueOf(dollarrate));
        new_eur_rate.setText(String.valueOf(eurorate));
        new_won_rate.setText(String.valueOf(wonrate));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dollarrate = Float.parseFloat(new_dol_rate.getText().toString());
                eurorate = Float.parseFloat(new_eur_rate.getText().toString());
                wonrate = Float.parseFloat(new_won_rate.getText().toString());

                Intent back = new Intent(RateActivity.this,MainActivity.class);
                back.putExtra("new_dol_rate",dollarrate);
                back.putExtra("new_eur_rate",eurorate);
                back.putExtra("new_won_rate",wonrate);
                back.putExtra("tag",1);

                SharedPreferences sharedPreferences = getSharedPreferences("myrate", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("new_dol_rate",dollarrate);
                editor.putFloat("new_eur_rate",eurorate);
                editor.putFloat("new_won_rate",wonrate);
                editor.apply();

                startActivity(back);
            }
        });

    }
}
