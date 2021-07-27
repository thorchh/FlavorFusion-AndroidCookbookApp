package com.example.csia2;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                                , SearchActivity.class).putExtra("user", user));
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
        ((TextView) findViewById(R.id.difficultyTextViewProgressBar)).setText("Difficulty: " + recipePassThrough.getDifficulty().toString() + "/5");
        ((ProgressBar) findViewById(R.id.timeProgressBar)).setProgress(recipePassThrough.getTime());
            //set time
            int hours = recipePassThrough.getTime() / 60;
            int minutes = recipePassThrough.getTime() % 60;
            if (recipePassThrough.getTime() <= 60){
                if (minutes == 1){
                    ((TextView) findViewById(R.id.timeTextViewProgressBar)).setText(recipePassThrough.getTime().toString() + "\n Minute");
                }else{
                    ((TextView) findViewById(R.id.timeTextViewProgressBar)).setText(recipePassThrough.getTime().toString() + "\n Minutes");
                }
            }else {
                String tt;
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
                ((TextView) findViewById(R.id.timeTextViewProgressBar)).setText(tt);
            }

        //checkboxList
        ArrayList<ArrayList> arrayList;
        arrayList = recipePassThrough.getingridientsChecklist();
        LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linf = LayoutInflater.from(RecipeActivity.this);
        LinearLayout ingridientLinearLayout = (LinearLayout)findViewById(R.id.ingredientsLinearLayout);
        for (int i = 0; i< arrayList.get(0).size();i++){

            View v = linf.inflate(R.layout.itemlayout, null);

            CheckBox tv = ((CheckBox) v.findViewById(R.id.linearLayoutTextView));
            tv.setText((String)(arrayList.get(0).get(i)));
            tv.setChecked((Boolean)(arrayList.get(1).get(i)));
            if ((Boolean) arrayList.get(1).get(i)) {
                tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                tv.setPaintFlags(tv.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG);
            }
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = ((CheckBox) v.findViewById(R.id.linearLayoutTextView));
                    //true
                    if ((tv.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                        tv.setPaintFlags(tv.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                        FirebaseDatabase.getInstance().getReference().child("RecipeUser").child(recipePassThrough.getTitle()).child("ingridientsChecklist").child("1").child(Integer.toString(finalI)).setValue(false);
                    }
                    //false
                    else {
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        FirebaseDatabase.getInstance().getReference().child("RecipeUser").child(recipePassThrough.getTitle()).child("ingridientsChecklist").child("1").child(Integer.toString(finalI)).setValue(true);
                    }
                }
            });

            ingridientLinearLayout.addView(v);
        }

        //Instructions
        ArrayList<ArrayList> instructionsArrayList;
        //instructionsArrayList = recipePassThrough.getInstructionsArrayList();
        LinearLayout instructionsLinearLayout = (LinearLayout)findViewById(R.id.instructionsLinearLayout);
        for (int i = 0; i< instructionsArrayList.size();i++){

            View v = linf.inflate(R.layout.instructionslayout, null);
            TextView tv1 = (TextView) v.findViewById(R.id.instructionsLinearLayoutStep);
            TextView tv2 = (TextView) v.findViewById(R.id.instructionsLinearLayoutTextView);
            tv1.setText("Step " + i + " :");
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
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find way to send back saved and checklist to the main saved, maybe change the way it works?
                if(saved == true){
                    bookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                    saved = false;
                    bookmarkButton.setChecked(false);
                    FirebaseDatabase.getInstance().getReference().child("RecipeUser").child(recipePassThrough.getTitle()).child("saved").setValue(false);
                }else{
                    bookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                    saved = true;
                    bookmarkButton.setChecked(true);

                    FirebaseDatabase.getInstance().getReference().child("RecipeUser").child(recipePassThrough.getTitle()).child("saved").setValue(true);
                }
            }
        });

        //rating bar
        userRating = recipePassThrough.getUserRating();
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(userRating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userRating = rating;
                FirebaseDatabase.getInstance().getReference().child("RecipeUser").child(recipePassThrough.getTitle()).child("userRating").setValue(userRating);

            }
        });

        //img

        //colourTag
        colourTag = recipePassThrough.getColourTag();
        String[] colours = getResources().getStringArray(R.array.colours);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.dropdown_item, colours);
        Spinner tv = (Spinner) findViewById(R.id.spinner);
        tv.setAdapter(arrayAdapter);
        tv.setSelection(Arrays.asList(colours).indexOf(colourTag));
        tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                FirebaseDatabase.getInstance().getReference().child("RecipeUser").child(recipePassThrough.getTitle()).child("colourTag").setValue(item);
                colourTag = item;
                TypedArray ta = getApplicationContext().getResources().obtainTypedArray(R.array.array_name);
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