package com.example.csia2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Adapter.OnNoteListener{
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<CardObj> cardObjList;
    DatabaseReference reff;
    Recipe recipe;


    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(MainActivity.this, "onCreate", Toast.LENGTH_LONG ).show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        cardObjList = new ArrayList<>();
        cardObjList.add(new CardObj("signature brown meatballs", "signature brown cheeseeeee", R.drawable.squat1));
        cardObjList.add(new CardObj("meatballs", "cheeseeeee", R.drawable.squat2));
        cardObjList.add(new CardObj("signature brown meatballs", "signature brown cheeseeeee", R.drawable.squat1));
        cardObjList.add(new CardObj("meatballs", "cheeseeeee", R.drawable.squat2));
        cardObjList.add(new CardObj("signature brown meatballs", "signature brown cheeseeeee", R.drawable.squat1));
        cardObjList.add(new CardObj("meatballs", "cheeseeeee", R.drawable.squat2));
        cardObjList.add(new CardObj("signature brown meatballs", "signature brown cheeseeeee", R.drawable.squat1));
        cardObjList.add(new CardObj("meatballs", "cheeseeeee", R.drawable.squat2));
        cardObjList.add(new CardObj("signature brown meatballs", "signature brown cheeseeeee", R.drawable.squat1));
        cardObjList.add(new CardObj("meatballs", "cheeseeeee", R.drawable.squat2));



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, cardObjList, this);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navBot);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity2.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity3.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //push to firebase
        recipe = new Recipe();
        reff = FirebaseDatabase.getInstance().getReference().child("Recipe");
        recipe.setTitle("Meatballs");
        recipe.setDesc("Cheese");
        reff.child("Meatballs").setValue(recipe);


        //get from firebase
        reff = FirebaseDatabase.getInstance().getReference().child("Recipe");
        reff.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                String  title = dataSnapshot.child("Meatballs").child("title").getValue().toString();
                String  desc = dataSnapshot.child("Meatballs").child("desc").getValue().toString();
                System.out.println(title + desc);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Activity2(View v){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.nav_search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;

    }


    @Override
    public void onNoteClick(int position) {
        cardObjList.get(position);
        Intent intent = new Intent(this, MainActivity4.class);
        intent.putExtra("cardObj", cardObjList.get(position));
        startActivity(intent);
    }
}