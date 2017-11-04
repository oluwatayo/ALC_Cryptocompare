package com.example.root.crptocompare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.StringReader;
import java.text.DecimalFormat;

public class Converter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        TextView textView = (TextView) findViewById(R.id.convertText);
        String name = "Conversion From ";
        name+= getIntent().getExtras().getString("name") + " To BTC";
        textView.setText(name);
        Button button = (Button) findViewById(R.id.buttonConvert);
        final EditText editText = (EditText) findViewById(R.id.userInput);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float a = Float.parseFloat(getIntent().getExtras().getString("value"));
                String b = editText.getText().toString();
                if (TextUtils.isEmpty(b)){
                    Toast.makeText(Converter.this, "Enter A Value", Toast.LENGTH_SHORT).show();
                }else {
                    TextView textView = (TextView) findViewById(R.id.result);
                    a = Float.parseFloat(b) / a;
                    DecimalFormat decimalFormat = new DecimalFormat("0.0000000");
                    String result = decimalFormat.format(a);
                    textView.setText(result);
                }

            }
        });
    }
}
