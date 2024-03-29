package com.example.csia2;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RecipeActivity extends AppCompatActivity {
    FirebaseUser user;
    CardObj cardObj;
    ListView listView;
    int progr = 0;
    Boolean saved;
    float userRating;
    String imgURI;
    String colourTag;
    String title;
    String desc;
    Integer difficulty;
    Integer time;
    Long RecipeID;
    DatabaseReference reff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recipe);

        //get user
        user = Objects.requireNonNull(getIntent().getExtras()).getParcelable("user");
        assert user != null;


        //bottomNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.navBot);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext()
                                , MainHomeActivity.class).putExtra("user", user));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext()
                                , ExploreActivity.class).putExtra("user", user));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext()
                                , ProfileActivity.class).putExtra("user", user));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        //setting xml
        final Recipe recipePassThrough = Objects.requireNonNull(getIntent().getExtras()).getParcelable("recipePassThrough");
        assert recipePassThrough != null;
        ((TextView) findViewById(R.id.recipeTitle)).setText(recipePassThrough.getTitle());
        ((TextView) findViewById(R.id.recipeDesc)).setText(recipePassThrough.getDesc());
            //img
            imgURI = recipePassThrough.getImg();
            Picasso.get().load(imgURI).into((ImageView) findViewById(R.id.recipeIMG));
        ((ProgressBar) findViewById(R.id.difficultyProgressBar)).setProgress(recipePassThrough.getDifficulty()*20);
        ((ProgressBar) findViewById(R.id.timeProgressBar)).setProgress(recipePassThrough.getTime());
        ((TextView) findViewById(R.id.difficultyTextViewProgressBar)).setText("Difficulty: " + recipePassThrough.getDifficulty().toString() + "/5");
            //set time text
            //separate hours and minutes
            int hours = recipePassThrough.getTime() / 60;
            int minutes = recipePassThrough.getTime() % 60;
            //minutes or minute
            if (recipePassThrough.getTime() <= 60){
                if (minutes == 1){
                    ((TextView) findViewById(R.id.timeTextViewProgressBar)).setText(recipePassThrough.getTime().toString() + "\n Minute");
                }else{
                    ((TextView) findViewById(R.id.timeTextViewProgressBar)).setText(recipePassThrough.getTime().toString() + "\n Minutes");
                }
            }else {
                String tt;
                //hours or hour
                if (hours == 1) {
                    if (minutes == 1) {
                        tt = String.format("%d Hour \n %02d Minute", hours, minutes);
                    } else {
                        tt = String.format("%d Hour \n %02d Minutes", hours, minutes);
                    }
                } else {
                    if (minutes == 1) {
                        tt = String.format("%d Hours \n %02d Minute", hours, minutes);
                    } else {
                        tt = String.format("%d Hours \n %02d Minutes", hours, minutes);
                    }
                }
                //set text
                ((TextView) findViewById(R.id.timeTextViewProgressBar)).setText(tt);
            }

        //checkboxList
        ArrayList<ArrayList> arrayList;
        arrayList = recipePassThrough.getingridientsChecklist();
        //inflater
        LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linf = LayoutInflater.from(RecipeActivity.this);
        LinearLayout ingridientLinearLayout = (LinearLayout)findViewById(R.id.ingredientsLinearLayout);
        //loop through ingridientsChecklist
        for (int i = 0; i< arrayList.get(0).size();i++){
            //create view
            View v = linf.inflate(R.layout.itemlayout, null);
            //get checkbox
            CheckBox tv = ((CheckBox) v.findViewById(R.id.linearLayoutTextView));
            //set text from arrayList
            tv.setText((String)(arrayList.get(0).get(i)));
            //set checked if true
            tv.setChecked((Boolean)(arrayList.get(1).get(i)));
            //if true
            if ((Boolean) arrayList.get(1).get(i)) {
                //initially cross out
                tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                //initially uncross
                tv.setPaintFlags(tv.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG);
            }
            //get email
            final int finalI = i;
            String email = user.getEmail();
            int index = email.indexOf('@');
            email = email.substring(0,index);
            String finalEmail = email;
            //set checkbox OnClickListener
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get checkbox
                    TextView tv = ((CheckBox) v.findViewById(R.id.linearLayoutTextView));
                    //if true
                    if ((tv.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                        //cross out
                        tv.setPaintFlags(tv.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                        //set in firebase
                        FirebaseDatabase.getInstance().getReference().child("Recipe" + finalEmail).child(recipePassThrough.getRecipeID().toString()).child("ingridientsChecklist").child("1").child(Integer.toString(finalI)).setValue(false);
                    }
                    //if false
                    else {
                        //uncross
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        //set in firebase
                        FirebaseDatabase.getInstance().getReference().child("Recipe" + finalEmail).child(recipePassThrough.getRecipeID().toString()).child("ingridientsChecklist").child("1").child(Integer.toString(finalI)).setValue(true);
                    }
                }
            });
            //add view to linear layout
            ingridientLinearLayout.addView(v);
        }

        //Instructions
        ArrayList<String> instructionsArrayList;
        instructionsArrayList = recipePassThrough.getInstructionsArrayList();
        LinearLayout instructionsLinearLayout = (LinearLayout)findViewById(R.id.instructionsLinearLayout);
        for (int i = 0; i< instructionsArrayList.size();i++){

            View v = linf.inflate(R.layout.instructionslayout, null);
            TextView tv1 = (TextView) v.findViewById(R.id.instructionsLinearLayoutStep);
            TextView tv2 = (TextView) v.findViewById(R.id.instructionsLinearLayoutTextView);
            tv1.setText("Step " + (i+1) + " :");
            tv2.setText((String)(instructionsArrayList.get(i)));
            instructionsLinearLayout.addView(v);
        }

        //bookmark
        final ToggleButton bookmarkButton = findViewById(R.id.bookmark);
        saved =  recipePassThrough.getSaved();
        bookmarkButton.setText(null);
        bookmarkButton.setTextOn(null);
        bookmarkButton.setTextOff(null);
        if(saved == true){
            bookmarkButton.setChecked(true);
            bookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
        }else{
            bookmarkButton.setChecked(false);
            bookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
        }
        String email = user.getEmail();
        int index = email.indexOf('@');
        email = email.substring(0,index);
        String finalEmail = email;
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find way to send back saved and checklist to the main saved, maybe change the way it works?
                if(saved == true){
                    bookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                    saved = false;
                    bookmarkButton.setChecked(false);
                    FirebaseDatabase.getInstance().getReference().child("Recipe" + finalEmail).child(recipePassThrough.getRecipeID().toString()).child("saved").setValue(false);
                }else{
                    bookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                    saved = true;
                    bookmarkButton.setChecked(true);

                    FirebaseDatabase.getInstance().getReference().child("Recipe" + finalEmail).child(recipePassThrough.getRecipeID().toString()).child("saved").setValue(true);
                }
            }
        });

        //rating bar
        userRating = recipePassThrough.getUserRating();
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(userRating);

        //setOnRatingBarChangeListener
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userRating = rating;
                FirebaseDatabase.getInstance().getReference().child("Recipe" + finalEmail).child(recipePassThrough.getRecipeID().toString()).child("userRating").setValue(userRating);

            }
        });

        //colourTag
        colourTag = recipePassThrough.getColourTag();
        //get colors array from resources
        String[] colours = getResources().getStringArray(R.array.colours);
        //create array adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.dropdown_item, colours);
        //get spinner
        Spinner tv = (Spinner) findViewById(R.id.spinner);
        //set array adapter and selection
        tv.setAdapter(arrayAdapter);
        tv.setSelection(Arrays.asList(colours).indexOf(colourTag));
        //set onItemSelectedListener
        tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get item from position
                String item = (String) adapterView.getItemAtPosition(i);
                //set firebase colourTag value
                FirebaseDatabase.getInstance().getReference().child("Recipe" + finalEmail).child(recipePassThrough.getRecipeID().toString()).child("colourTag").setValue(item);
                colourTag = item;
                //get TypedArray array_name from resources
                TypedArray ta = getApplicationContext().getResources().obtainTypedArray(R.array.array_name);
                //set colour by retrieving colour's Position from colours Array and passing it as the index ta's .getColor() Method
                tv.setBackgroundColor(ta.getColor(Arrays.asList(colours).indexOf(colourTag),0));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //edit recipe button
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditActivity.class).putExtra("user", user).putExtra("recipePassThrough", recipePassThrough));
            }
        });
    }
}