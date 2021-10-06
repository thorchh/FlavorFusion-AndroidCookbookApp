package com.example.csia2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainHomeActivity extends AppCompatActivity implements Adapter.OnNoteListener{
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Recipe> recipeObjList;
    ArrayList<CardObj> cardObjList;
    HashMap<CardObj, Recipe> recipeHash = new HashMap<>();
    FirebaseUser user;
    DatabaseReference reff;
    ArrayList<ArrayList> ingridientsChecklist;
    private ArrayList<String> ingridients;
    private ArrayList<Boolean> checklist;
    private ArrayList<String> instructionsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainhome);
        recipeObjList = new ArrayList<>();
        ingridients = new ArrayList<>();
        checklist = new ArrayList<>();
        ingridientsChecklist = new ArrayList<>();
        instructionsArrayList = new ArrayList<>();

        //get email without domain: @xxx.xxx using user from login/register
        user = Objects.requireNonNull(getIntent().getExtras()).getParcelable("user");
        assert user != null;
        String email = user.getEmail();
        int index = email.indexOf('@');
        email = email.substring(0,index);
        reff = FirebaseDatabase.getInstance().getReference().child(("Recipe" + email));
        System.out.println(reff);
        System.out.println(reff.child(""));
        reff.addValueEventListener(new ValueEventListener(){

            //get from firebase database 'saved' branch
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //create Recipes from firebase into recipeObjList
                //loop through
                for (DataSnapshot element : children){
                    //check if saved
                    if ((Boolean) element.child("saved").getValue()){
                        //set values
                        String title = (String) element.child("title").getValue();
                        String desc = (String) element.child("desc").getValue();
                        String imgURI = (String) element.child("img").getValue();
                        Long difficulty = (Long) element.child("difficulty").getValue();
                        Long time = (Long)element.child("time").getValue();
                        Boolean saved = (Boolean) element.child("saved").getValue();
                        String colourTag = (String) element.child("colourTag").getValue();
                        ArrayList<ArrayList> ingridientsChecklist = (ArrayList<ArrayList>) element.child("ingridientsChecklist").getValue();
                        Object userRating = element.child("userRating").getValue();
                        ArrayList<String> instructionsArrayList = (ArrayList<String>) element.child("instructionsArrayList").getValue();
                        Long recipeID = (Long) element.child("RecipeID").getValue();

                        if (userRating instanceof Long) {
                            Long lUserRating = (Long) userRating;
                            // create new recipe and append into recipeobjlist
                            recipeObjList.add(new Recipe(title, desc, imgURI, difficulty, time, saved, colourTag, ingridientsChecklist, lUserRating.doubleValue(), instructionsArrayList, recipeID));
                        } else {
                            // create new recipe and append into recipeobjlist
                            recipeObjList.add(new Recipe(title, desc, imgURI, difficulty, time, saved, colourTag, ingridientsChecklist, (Double) userRating, instructionsArrayList, recipeID));
                        }
                    }
                }
                //run init()
                init();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                init();
            }
        });
    }

    // to initialise everything
    public void init(){
        cardObjList = new ArrayList<>();
        //cardobj + cardobjlist + hashmap (recipehash)
        for (int i = 0; i< recipeObjList.size();i++){
            //create and add cardobj to cardobjlist with recipe from recipe obj list
            cardObjList.add(new CardObj(recipeObjList.get(i).getTitle(), recipeObjList.get(i).getDesc(), recipeObjList.get(i).getImg(),recipeObjList.get(i).getColourTag(),recipeObjList.get(i).getingridientsChecklist()));
            //link recipe and cardobj
            recipeHash.put(cardObjList.get(i),recipeObjList.get(i));
        }
        //get recyclerview and adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, cardObjList, this);
        recyclerView.setAdapter(adapter);

        //get bottomnav
        BottomNavigationView bottomNavigationView = findViewById(R.id.navBot);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            //if bottomnav pressed
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //home
                    case R.id.nav_home:
                        return true;
                    //search
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext()
                                , ExploreActivity.class).putExtra("user", user));
                        overridePendingTransition(0,0);
                        return true;
                    //profile
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

    //search menu, when search is pressed
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
                //call adapter filter method
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    //when cardobj is pressed
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