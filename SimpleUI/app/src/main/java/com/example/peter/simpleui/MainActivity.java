package com.example.peter.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;
    /*
    final : 最終型態，不能動 → 大寫
    自己設定的識別碼
     */

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ArrayList<Order> orders;
    String drinkName;
    String storeInfo;
    CheckBox checkBox;
    ListView listView;
    String note = "";
    Spinner spinner;
    ProgressBar progressBar;

    String menuResults = "";

    SharedPreferences sp;//功能:讀
    SharedPreferences.Editor editor;//功能:寫

    Realm realm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//搶螢幕
        Log.d("debug", "Main Activity onCreate");

        ParseObject testObject = new ParseObject("HomeworkParse");
        testObject.put("sid", "盧冠翔");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        radioGroup = (RadioGroup)findViewById(R.id.firstRadioGroup);
        checkBox = (CheckBox)findViewById(R.id.hideCheckBox);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        orders = new ArrayList<>();

        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        //"setting" → 這個功能的名字 , Context.MODE_PRIVATE → 模式: 可讀寫[覆蓋]
        //容量不大
        editor = sp.edit();

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();

        Realm.setDefaultConfiguration(realmConfig);

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

        editText.setText(sp.getString("editText", ""));
        //"editText" → 要找的東西, "" → 找不到東西時給的東西

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = editText.getText().toString();
                editor.putString("editText", text);
                //text → editor 導入的東西, "editText" → 命名
                editor.apply();

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    click(v);
                    return true;
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    click(v);
                    return true;
                }
                return false;
            }
        });

//        int checkedId = sp.getInt("radioGroup", R.id.blackTeaRadioButton);
//        radioGroup.check(checkedId);
//
//        RadioButton radioButton = (RadioButton)findViewById(checkedId);
//        drinkName = radioButton.getText().toString();
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                editor.putInt("radioGroup", checkedId);
//                editor.apply();
//
//                RadioButton radioButton = (RadioButton) findViewById(checkedId);
//                drinkName = radioButton.getText().toString();
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getItem完型態是Object→需 (Order)轉型態為order
                Order order = (Order) parent.getAdapter().getItem(position);
                //parent.getAdapter() → 拿出OrderAdapter
                Snackbar.make(view, order.getNote(), Snackbar.LENGTH_SHORT).show();
            }
        });

        spinner.setSelection(sp.getInt("spinner", 0));
        storeInfo = (String)spinner.getSelectedItem();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("spinner", spinner.getSelectedItemPosition());
                editor.apply();
                storeInfo = (String)spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        setupListView();
        setupSpinner();



    }

    void setupListView(){
        final RealmResults results = realm.allObjects(Order.class);

        progressBar.setVisibility(View.VISIBLE);

        OrderAdapter adapter = new OrderAdapter(MainActivity.this, results.subList(0, results.size()));
        listView.setAdapter(adapter);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Order");//去網路上找到Order
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);

                    return;
                }

                List<Order> orders = new ArrayList<Order>();

                Realm realm = Realm.getDefaultInstance();

                for(int i = 0; i < objects.size(); i++){
                    Order order = new Order();
                    order.setNote(objects.get(i).getString("note"));
                    order.setStoreInfo(objects.get(i).getString("storeInfo"));
                    order.setMenuResults(objects.get(i).getString("menuResults"));
                    orders.add(order);

                    if(results.size() <= 1){
                        realm.beginTransaction();
                        realm.copyToRealm(order);
                        realm.commitTransaction();
                    }
                }

                realm.close();

                progressBar.setVisibility(View.GONE);

                OrderAdapter adapter = new OrderAdapter(MainActivity.this, orders);
                listView.setAdapter(adapter);

            }
        });
    }

    void setupSpinner(){
        String[] data = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);

        spinner.setAdapter(adapter);
        spinner.setSelection(sp.getInt("spinner", 0));
    }

    public void click(View view){
        note = editText.getText().toString();
        String text = note;
        textView.setText(text);
        Order order = new Order();
        order.setMenuResults(menuResults);
        order.setNote(note);
        order.setStoreInfo(storeInfo);



        SaveCallbackWithRealm saveCallbackWithRealm = new SaveCallbackWithRealm(order, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Save fail", Toast.LENGTH_LONG).show();
                }

                editText.setText("");
                menuResults = "";

                setupListView();
            }
        });

        order.saveToRemote(saveCallbackWithRealm);

    }

    public void goToMenu(View view){
        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);//從這個Class到DrinkMenuActivity.class
        /*
        [版本1]
        startActivity(intent);//啟動這個intent
         */
        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY);
        //啟動這個Intent並可以得知是誰的回傳
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_MENU_ACTIVITY){
            if(resultCode == RESULT_OK){
                menuResults = data.getStringExtra("result");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_take_photo){
            Toast.makeText(this, "Take Photo", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "Main Activity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "Main Activity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "Main Activity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "Main Activity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        Log.d("debug", "Main Activity onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "Main Activity onRestart");
    }
}
