package com.example.root.crptocompare;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class PrefrenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefrence);


        Button button = (Button) findViewById(R.id.buttonAdd);

        //set OnclickListener on button to add New Currency
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.addCurrencyField);
                //if statement to ensure the user enters a valid currency of length 3
                if(editText.getText().toString().length() != 3){
                    Toast.makeText(PrefrenceActivity.this, "Please Enter a Valid Currency", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences sharedPreferences = getSharedPreferences("PREF", 0);
                    /*
                     * check if a sharedprefrence for the currency list exist or not
                    * add the defaults only once if it does not exist
                    */
                    if(!sharedPreferences.contains("currencies")){
                        ArrayList<String> currencies = new ArrayList<>();
                        currencies.add("NGN");
                        currencies.add("USD");
                        currencies.add("AUD");
                        currencies.add("GBP");
                        currencies.add("JPY");
                        currencies.add(editText.getText().toString().toUpperCase());
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < currencies.size(); i++) {
                            stringBuilder.append(currencies.get(i));
                            stringBuilder.append(",");
                        }
                        String output = stringBuilder.toString();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("currencies", output);
                        editor.commit();
                        Toast.makeText(PrefrenceActivity.this, "New Currency Added", Toast.LENGTH_SHORT).show();
                    }else {
                        /*
                        * if Shared prefrence exists, just add the new currency entered by the user to the
                        * existing list of currencies
                        * */
                        String out = sharedPreferences.getString("currencies", "");
                        if(out.contains(editText.getText().toString().toUpperCase())){
                            Toast.makeText(PrefrenceActivity.this, "That Currency already Exists", Toast.LENGTH_SHORT).show();
                        }else if(out.length() > 100){
                            Toast.makeText(PrefrenceActivity.this, "You Cannot Add More Currencies", Toast.LENGTH_SHORT).show();
                        }else{
                            out += editText.getText().toString().toUpperCase() +",";
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("currencies", out);
                            editor.commit();
                            Toast.makeText(PrefrenceActivity.this, "New Currency Added", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });
    }
}
