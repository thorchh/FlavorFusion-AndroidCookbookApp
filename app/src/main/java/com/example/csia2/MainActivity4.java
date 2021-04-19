package com.example.csia2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity4 extends AppCompatActivity {
    CardObj cardObj;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main4);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navBot);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity2.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity3.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        CardObj cardObjf = Objects.requireNonNull(getIntent().getExtras()).getParcelable("cardObj");
        assert cardObjf != null;
        ((TextView) findViewById(R.id.recipeTitle)).setText(cardObjf.getTitle());
        ((TextView) findViewById(R.id.recipeDesc)).setText(cardObjf.getDesc());
        ((ImageView) findViewById(R.id.recipeIMG)).setImageResource(cardObjf.getImg());
        listView =(ListView)findViewById(R.id.listview);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("bobs");
        arrayList.add("jeff");
        arrayList.add("gustav");
        arrayList.add("bobs");
        arrayList.add("bobs");


        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);





    }
}