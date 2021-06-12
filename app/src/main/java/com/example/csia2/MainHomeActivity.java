package com.example.csia2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.util.Log;
import android.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class MainHomeActivity extends AppCompatActivity implements Adapter.OnNoteListener{
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Recipe> recipeObjList;
    ArrayList<CardObj> cardObjList;
    HashMap<CardObj, Recipe> recipeHash = new HashMap<>();
    Recipe recipe;
    FirebaseUser user;
    DatabaseReference reff;
    ArrayList<String> ingridients;
    float userRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mainhome);

        user = Objects.requireNonNull(getIntent().getExtras()).getParcelable("user");
        assert user != null;

        recipeObjList = new ArrayList<>();
        ingridients = new ArrayList<String>();
/*
        ingridients.add("cheese"); ingridients.add("not cheese"); ingridients.add("bananas"); ingridients.add("not bananas");
        recipeObjList.add(new Recipe("signature brown meatballs", "I am a salmon lover. This is a great recipe for a slightly exotic flavor of Indian inspiration with a maple twist. The flavor is exceptional, delicious, and unique. Orange zest may be added for an extra flavor twist.", R.drawable.squat1, 5, 50, true, "Green", ingridients, 2.5f));
        recipeObjList.add(new Recipe("signature brown meat", "just cheese", R.drawable.squat1, 2, 100, false, "green", ingridients, 2.5f));

        //push to firebase
        //need to find a way to push pictures to firebase
        for (int i = 0; i< recipeObjList.size();i++) {
            recipe = recipeObjList.get(i);
            reff = FirebaseDatabase.getInstance().getReference().child("Recipe");
            reff.child(recipe.getTitle()).setValue(recipe);
        }

*/



        reff = FirebaseDatabase.getInstance().getReference().child("Recipe");
        reff.addValueEventListener(new ValueEventListener(){

            //get from saved branch

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                //create Recipes from firebase
                for (DataSnapshot element : children){
                    System.out.println(element.getValue());
                    System.out.println(element.child("img").getValue());
                    recipeObjList.add(new Recipe((String) element.child("title").getValue(),(String) element.child("description").getValue(), (Integer) Math.toIntExact(element.child("img").getValue()), (Integer) element.child("difficulty").getValue(), (Integer)element.child("time").getValue(), (Boolean) element.child("saved").getValue(), (String) element.child("colourTag").getValue(), (ArrayList<String>) element.child("ingridients").getValue(), (float) element.child("userRating").getValue()));
                    System.out.println(element.child("ingridients").getValue().getClass().getName());
                }
                //System.out.println(recipeObjList);

                    // Enhanced loop for (E element : list) {

                //System.out.println(children);
                //System.out.println(dataSnapshot);
                String  title = dataSnapshot.child("signature brown meat").child("title").getValue().toString();
                String  desc = dataSnapshot.child("signature brown meat").child("desc").getValue().toString();
                Integer difficulty = Integer.parseInt(dataSnapshot.child("signature brown meat").child("difficulty").getValue().toString());
               // System.out.println(title + desc + difficulty);
                //System.out.println(dataSnapshot.child("signature brown meat").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cardObjList = new ArrayList<>();
        //cardobj + cardobjlist + hashmap (recipehash)
        for (int i = 0; i< recipeObjList.size();i++){
            //create and add cardobj to cardobjlist with recipe from recipe obj list
            cardObjList.add(new CardObj(recipeObjList.get(i).getTitle(), recipeObjList.get(i).getDesc(), recipeObjList.get(i).getImg()));

            //link recipe and cardobj
            recipeHash.put(cardObjList.get(i),recipeObjList.get(i));
        }



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
                                , SearchActivity.class).putExtra("user", user));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext()
                                , ProfileActivity.class).putExtra("user", user));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }


    public void Activity2(View v){
        Intent intent = new Intent(this, SearchActivity.class);
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
        //get cardobj from cardobjlist and get recipe through hashmap
        Recipe passThrough = recipeHash.get(cardObjList.get(position));
        //new intent
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipePassThrough", passThrough).putExtra("user", user);
        startActivity(intent);
    }
}