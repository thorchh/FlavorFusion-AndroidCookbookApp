package com.example.csia2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class RecipeActivity extends AppCompatActivity {
    FirebaseUser user;
    CardObj cardObj;
    ListView listView;
    int progr = 0;
    Boolean saved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipe);

        user = Objects.requireNonNull(getIntent().getExtras()).getParcelable("user");
        assert user != null;



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

        Recipe recipePassThrough = Objects.requireNonNull(getIntent().getExtras()).getParcelable("recipePassThrough");
        assert recipePassThrough != null;
        ((TextView) findViewById(R.id.recipeTitle)).setText(recipePassThrough.getTitle());
        ((TextView) findViewById(R.id.recipeDesc)).setText(recipePassThrough.getDesc());
        ((ImageView) findViewById(R.id.recipeIMG)).setImageResource(recipePassThrough.getImg());
        ((ProgressBar) findViewById(R.id.difficultyProgressBar)).setProgress(recipePassThrough.getDifficulty()*20);
        ((TextView) findViewById(R.id.difficultyTextViewProgressBar)).setText("Difficulty: " + recipePassThrough.getDifficulty().toString() + "/5");
        ((ProgressBar) findViewById(R.id.timeProgressBar)).setProgress(100 * recipePassThrough.getTime()/100);
        if (recipePassThrough.getTime() <= 60){
            ((TextView) findViewById(R.id.timeTextViewProgressBar)).setText(recipePassThrough.getTime().toString() + "\n Minutes");
        }else{
            int hours = recipePassThrough.getTime() / 60;
            int minutes = recipePassThrough.getTime() % 60;
            String tt = String.format("%d Hours \n %02d Minutes", hours , minutes);

            ((TextView) findViewById(R.id.timeTextViewProgressBar)).setText(tt);
        }
        saved =  recipePassThrough.getSaved();

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList = recipePassThrough.getIngridients();


        LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linf = LayoutInflater.from(RecipeActivity.this);

        LinearLayout ingridientLinearLayout = (LinearLayout)findViewById(R.id.ingredientsLinearLayout);

        for (int i = 0; i< arrayList.size();i++){

            View v = linf.inflate(R.layout.itemlayout, null);

            TextView tv = ((TextView) v.findViewById(R.id.linearLayoutTextView));
            tv.setText(arrayList.get(i));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox tv = ((CheckBox) v.findViewById(v.getId()));
                    if ((tv.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                        tv.setPaintFlags(tv.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else {
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
            });

            ingridientLinearLayout.addView(v);
        }

        final ToggleButton bookmarkButton = findViewById(R.id.bookmark);

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
                }else{
                    bookmarkButton.setBackgroundResource(R.drawable.bookmark_button);
                    saved = true;
                    bookmarkButton.setChecked(true);
                }
                Toast.makeText(RecipeActivity.this, saved.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}