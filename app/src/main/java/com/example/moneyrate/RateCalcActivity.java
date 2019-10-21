package com.example.moneyrate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class RateCalcActivity extends AppCompatActivity {

    private float rate = 0f;
    private String title;
    private String TAG = "rateCalc";
    private TextView title2;
    private EditText inp2;
    private Button bt2;
    private TextView show2;
    private float rmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);

        title = getIntent().getStringExtra("title");
        rate = getIntent().getFloatExtra("rate",0f);

        Log.i(TAG, "onCreate: title = " + title);
        Log.i(TAG, "onCreate: rate = " + rate);

        title2 = (TextView) findViewById(R.id.title2);
        inp2 = (EditText) findViewById(R.id.inp2);
        bt2 = (Button) findViewById(R.id.bt2);
        show2 = (TextView) findViewById(R.id.show2);

        title2.setText(title);

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inp2.getText().toString() == null){
                    Toast.makeText(RateCalcActivity.this,"请输入要计算的金额",Toast.LENGTH_SHORT).show();
                }
                else {
                    rmb = Float.parseFloat(inp2.getText().toString());
                    show2.setText(String.valueOf(rmb*rate));
                }
            }
        });
    }
}
