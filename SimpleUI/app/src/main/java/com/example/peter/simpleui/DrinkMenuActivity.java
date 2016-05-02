package com.example.peter.simpleui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinkMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        Log.d("debug", "DrinkMenu Activity onCreate");
    }

    public void add(View view){
        Button button = (Button)view;
        String text = button.getText().toString();
        int count = Integer.parseInt(text);//Integer.parseInt()把文字轉成數字
        count++;
        button.setText(String.valueOf(count));
    }

    public void cancel(View view){
        finish();//結束目前的Activity並回到上一頁 → 目前的Activity執行onDestroy()並回到上一個Activity
    }

    public void done(View view){
        Intent intent = new Intent();
        intent.putExtra("result", getData().toString());

        setResult(RESULT_OK, intent);
        //將自己所帶的內容留在殘留的intent當中??以利其他Activity接取??
        finish();
    }

    public JSONArray getData(){
        LinearLayout rootLinearLayout = (LinearLayout)findViewById(R.id.root);//找到最高層LinearLayout

        JSONArray jsonArray = new JSONArray();


        for(int i = 1; i <= 3; i++){
            LinearLayout linearLayout = (LinearLayout)rootLinearLayout.getChildAt(i);//找到最高層下第2~4層LinearLayout

            TextView textView = (TextView)linearLayout.getChildAt(0);
            Button mButton = (Button)linearLayout.getChildAt(1);
            Button lButton = (Button)linearLayout.getChildAt(2);

            String drinkName = textView.getText().toString();

            int m = Integer.parseInt(mButton.getText().toString());
            int l = Integer.parseInt(lButton.getText().toString());

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", drinkName);
                jsonObject.put("m", m);
                jsonObject.put("l", l);
                /*
                用法同SharedPreference
                Ex: "name" : "black tea"
                "mNumber" : 5
                */

                jsonArray.put(jsonObject);
                /*
                一堆Object組成的Array
                Ex:
                "order" : [{"name" : "black tea"}, {"mNumber" : 5}]
                */
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return jsonArray;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "DrinkMenu Activity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "DrinkMenu Activity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "DrinkMenu Activity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "DrinkMenu Activity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "DrinkMenu Activity onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "DrinkMenu Activity onRestart");
    }
}
