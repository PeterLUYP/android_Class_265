package com.example.peter.simpleui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ArrayList<Order> orders;
    String drinkName;
    CheckBox checkBox;
    ListView listView;
    String note = "";
    Spinner spinner;

    SharedPreferences sp;//功能:讀
    SharedPreferences.Editor editor;//功能:寫

    Realm realm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        radioGroup = (RadioGroup)findViewById(R.id.firstRadioGroup);
        checkBox = (CheckBox)findViewById(R.id.hideCheckBox);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);
        orders = new ArrayList<>();

        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        //"setting" → 這個功能的名字 , Context.MODE_PRIVATE → 模式: 可讀寫[覆蓋]
        //容量不大
        editor = sp.edit();

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);

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

        int checkedId = sp.getInt("radioGroup", R.id.blackTeaRadioButton);
        radioGroup.check(checkedId);

        RadioButton radioButton = (RadioButton)findViewById(checkedId);
        drinkName = radioButton.getText().toString();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editor.putInt("radioGroup", checkedId);
                editor.apply();

                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                drinkName = radioButton.getText().toString();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //getItem完型態是Object→需 (Order)轉型態為order
                Order order = (Order) parent.getAdapter().getItem(position);
                //parent.getAdapter() → 拿出OrderAdapter
                Snackbar.make(view, order.getNote(), Snackbar.LENGTH_SHORT).show();
            }
        });

        setupListView();
        setupSpinner();



    }

    void setupListView(){
        RealmResults results = realm.allObjects(Order.class);


        OrderAdapter adapter = new OrderAdapter(this, results.subList(0, results.size()));
        listView.setAdapter(adapter);
    }

    void setupSpinner(){
        String[] data = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);

        spinner.setAdapter(adapter);
    }

    public void click(View view){
        note = editText.getText().toString();
        String text = note;
        textView.setText(text);
        Order order = new Order();
        order.setDrinkName(drinkName);
        order.setNote(note);
        order.setStoreInfo((String) spinner.getSelectedItem());



        // Persist your data easily
        realm.beginTransaction();
        realm.copyToRealm(order);
        realm.commitTransaction();

        editText.setText("");

        setupListView();

    }

}
