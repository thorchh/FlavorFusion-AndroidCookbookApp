package com.example.csia2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
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

public class ExploreActivity extends AppCompatActivity implements Adapter.OnNoteListener{
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Recipe> recipeObjList;
    ArrayList<CardObj> cardObjList;
    HashMap<CardObj, Recipe> recipeHash = new HashMap<>();
    Recipe recipe;
    FirebaseUser user;
    DatabaseReference reff;
    ArrayList<ArrayList> ingridientsChecklist;
    float userRating;
    private ArrayList<String> ingridients;
    private ArrayList<Boolean> checklist;
    private ArrayList<String> instructionsArrayList;
    String img;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference imageRef = storage.getReference();
    ArrayList<String> imageUrls = new ArrayList();
    ArrayList<String> imageName = new ArrayList<>();
    ArrayList<ArrayList> imageUrlsName = new ArrayList<>();
    long recipeID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_explore);
        user = Objects.requireNonNull(getIntent().getExtras()).getParcelable("user");
        assert user != null;
        recipeObjList = new ArrayList<>();
        ingridients = new ArrayList<>();
        checklist = new ArrayList<>();
        ingridientsChecklist = new ArrayList<>();
        instructionsArrayList = new ArrayList<>();

/*        ingridients.add("cheese"); ingridients.add("not cheese"); ingridients.add("bananas"); ingridients.add("not bananas");
        checklist.add(true); checklist.add(true); checklist.add(true); checklist.add(false); checklist.add(true);
        ingridientsChecklist.add(ingridients); ingridientsChecklist.add(checklist);
        recipeObjList.add(new Recipe("signature brown meatballs", "I am a salmon lover. This is a great recipe for a slightly exotic flavor of Indian inspiration with a maple twist. The flavor is exceptional, delicious, and unique. Orange zest may be added for an extra flavor twist.", R.drawable.squat1, 5, 50, true, "Green", ingridientsChecklist, 2.5f));
        recipeObjList.add(new Recipe("signature brown meat", "just cheese", R.drawable.squat1, 2, 100, false, "green", ingridientsChecklist, 2.5f));
        //push to firebase
        //need to find a way to push pictures to firebase
        for (int i = 0; i< recipeObjList.size();i++) {
            recipe = recipeObjList.get(i);
            reff = FirebaseDatabase.getInstance().getReference().child("Recipe");
            reff.child(recipe.getTitle()).setValue(recipe);
        }*/

        //change this to different branches later
        //get email without @xxx.xxx
        String email = user.getEmail();
        int index = email.indexOf('@');
        email = email.substring(0,index);
        reff = FirebaseDatabase.getInstance().getReference().child(("Recipe" + email));
        reff.addValueEventListener(new ValueEventListener(){

            //get from firebase database 'saved' branch
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //create Recipes from firebase into recipeObjList
                //loop through
                for (DataSnapshot element : children){
                    //set values
                    String title = (String) element.child("title").getValue();String desc = (String) element.child("desc").getValue();String imgURI = (String) element.child("img").getValue();Long difficulty = (Long) element.child("difficulty").getValue();Long time = (Long)element.child("time").getValue();Boolean saved = (Boolean) element.child("saved").getValue();String colourTag = (String) element.child("colourTag").getValue();ArrayList<ArrayList> ingridientsChecklist = (ArrayList<ArrayList>) element.child("ingridientsChecklist").getValue();Object userRating = element.child("userRating").getValue(); ArrayList<String> instructionsArrayList = (ArrayList<String>) element.child("instructionsArrayList").getValue(); Long recipeID = (Long) element.child("RecipeID").getValue();
                    System.out.println(recipeID);
                    if (userRating instanceof Long) {
                        Long lUserRating = (Long) userRating;
                        // create new recipe and append into recipeobjlist
                        recipeObjList.add(new Recipe(title, desc, imgURI, difficulty, time, saved, colourTag, ingridientsChecklist, lUserRating.doubleValue(), instructionsArrayList, recipeID));
                    } else {
                        // create new recipe and append into recipeobjlist
                        recipeObjList.add(new Recipe(title, desc, imgURI, difficulty, time, saved, colourTag, ingridientsChecklist, (Double) userRating, instructionsArrayList, recipeID));
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
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            //if bottomnav pressed
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //home
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext()
                                , MainHomeActivity.class).putExtra("user", user));
                        overridePendingTransition(0,0);
                        return true;
                    //search
                    case R.id.nav_search:
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

/*        //loop through recipeobjlist
        for (Recipe recipe:recipeObjList) {
            int count = 0;
            //database reference
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            //get email without @xxx.xxx
            String email = user.getEmail();
            int index = email.indexOf('@');
            email = email.substring(0,index);
            DatabaseReference getImage = databaseReference.child(("Recipe" + email)).child(recipe.getTitle()).child("img");
            System.out.println("get img: " + getImage);
            final int finalCount = count;
            getImage.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // getting a DataSnapshot for the location at the specified relative path and getting in the link variable
                    String link = dataSnapshot.getValue(String.class);
                    System.out.println("link: " + link);
                    //setting link
                    recipeObjList.get(finalCount).setImg(link);
                    System.out.println(recipeObjList.get(finalCount).getImg());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            count++;
        }*/

        //img
        System.out.println("hello");
        //listFiles();
    }

    //search menu
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

    //when cardobj is pressed
    @Override
    public void onNoteClick(int position) {
        System.out.println(recipeID);
        //get cardobj from cardobjlist and get recipe through hashmap
        Recipe passThrough = recipeHash.get(cardObjList.get(position));
        //new intent
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipePassThrough", passThrough).putExtra("user", user);
        System.out.println(recipeID);
        startActivity(intent);
    }

    //imgs get img reference list from firebase
    //need to find a way to get this list added to the list of objects
    /*public void listFiles(){
        try {
            imageRef.child("images/").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>(){
                @Override
                public void onSuccess(ListResult listResult) {
                    List<StorageReference> images;
                    images = (List) listResult.getItems();

                    for (StorageReference image : images){
                        Task<Uri> urlTask = image.getDownloadUrl();
                        urlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //get email without @xxx.xxx
                                String email = user.getEmail();
                                int index = email.indexOf('@');
                                email = email.substring(0,index);

                                System.out.println("name: " + image.getName());
                                System.out.println("uri: " + uri);
                                System.out.println("uri to string: " + uri.toString());
                                imageUrlsName.clear();
                                imageUrls.add(uri.toString());
                                imageName.add(image.getName().substring(0,image.getName().length() - 4));
                                imageUrlsName.add(imageName);imageUrlsName.add(imageUrls);
                                System.out.println("imageUrlsName: " + imageUrlsName);
                                FirebaseDatabase.getInstance().getReference().child(("Recipe" + email)).child(image.getName().substring(0,image.getName().length() - 4)).child("img").setValue(uri.toString());
                                // ^^ this needs to happen before adding images to the recipe objlist
                                // maybe add this to init before everything and then add init after this onSuccess?
                            }
                        });
                    }

                }
            });
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"oops, had a problem loading images", Toast.LENGTH_LONG).show();
        }
    }
*/
}