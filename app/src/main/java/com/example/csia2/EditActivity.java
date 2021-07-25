package com.example.csia2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    FirebaseUser user;
    Boolean saved;
    float userRating;
    String imgURI;
    String colourTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //get user
        user = Objects.requireNonNull(getIntent().getExtras()).getParcelable("user");
        assert user != null;

        //findviewbyid
        TextView recipeTitleEditText = findViewById(R.id.recipeTitleEditText);
        TextView recipeDescEditText = findViewById(R.id.recipeDescEditText);
        ProgressBar difficultyProgressBarEditActivity = findViewById(R.id.difficultyProgressBarEditActivity);
        NumberPicker difficultyProgressBarEditNumPick = findViewById(R.id.difficultyProgressBarEditNumPick);
        ProgressBar timeProgressBarEditActivity = findViewById(R.id.timeProgressBarEditActivity);
        NumberPicker timeProgressBarEditNumPick = findViewById(R.id.timeProgressBarEditNumPick);
        LinearLayout ingredientsLinearLayoutEditActivity = (LinearLayout)findViewById(R.id.ingredientsLinearLayoutEditActivity);
        TextView difficultyProgressBarTextView = findViewById(R.id.difficultyProgressBarTextView);
        TextView timeProgressBarTextView = findViewById(R.id.timeProgressBarTextView);
        Button saveButton = findViewById(R.id.saveEditActivityButton);

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
        recipeTitleEditText.setText(recipePassThrough.getTitle());
        recipeDescEditText.setText(recipePassThrough.getDesc());
        imgURI = recipePassThrough.getImg();
        difficultyProgressBarEditActivity.setProgress(recipePassThrough.getDifficulty()*20);
        difficultyProgressBarTextView.setHint(String.format("Difficulty: %d/5", recipePassThrough.getDifficulty()));
        timeProgressBarEditActivity.setProgress(100 * recipePassThrough.getTime()/100);

        //Ingridient arraylist
        ArrayList<ArrayList> arrayList;
        arrayList = recipePassThrough.getingridientsChecklist();
        LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linf = LayoutInflater.from(EditActivity.this);
        LinearLayout ingridientLinearLayout = (LinearLayout)findViewById(R.id.ingredientsLinearLayoutEditActivity);
        for (int i = 0; i< arrayList.get(0).size();i++){

            View v = linf.inflate(R.layout.itemlayoutnocheckbox, null);

            EditText tv = ((EditText) v.findViewById(R.id.linearLayoutEditTextView));
            tv.setText((String)(arrayList.get(0).get(i)));
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


        //set time
        int hours = recipePassThrough.getTime() / 60;
        int minutes = recipePassThrough.getTime() % 60;
        if (recipePassThrough.getTime() <= 60){
            if (minutes == 1){
                timeProgressBarTextView.setHint(recipePassThrough.getTime().toString() + "\n Minute");
            }else{
                timeProgressBarTextView.setHint(recipePassThrough.getTime().toString() + "\n Minutes");
            }
        }else{
            String tt;
            if (hours == 1){
                if (minutes == 1){
                    tt = String.format("%d Hour \n %02d Minute", hours , minutes);
                }else{
                    tt = String.format("%d Hour \n %02d Minutes", hours , minutes);
                }
            }else{
                if (minutes == 1){
                    tt = String.format("%d Hours \n %02d Minute", hours , minutes);
                }else{
                    tt = String.format("%d Hours \n %02d Minutes", hours , minutes);
                }
            }

            timeProgressBarTextView.setHint(tt);
        }


        //on edit listeners
        recipeTitleEditText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("bob!");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recipeDescEditText.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //numberpickers
        timeProgressBarEditNumPick.setMaxValue(7200);
        timeProgressBarEditNumPick.setMinValue(1);
        timeProgressBarEditNumPick.setValue(30);
        difficultyProgressBarEditNumPick.setMaxValue(5);
        difficultyProgressBarEditNumPick.setMinValue(1);
        difficultyProgressBarEditNumPick.setValue(3);

        timeProgressBarEditNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int hours = newVal / 60;
                int minutes = newVal % 60;
                if (newVal <= 60){
                    if (minutes == 1){
                        timeProgressBarTextView.setText(newVal + "\n Minute");
                        timeProgressBarEditActivity.setProgress(100 * newVal/100);
                    }else{
                        timeProgressBarTextView.setText(newVal + "\n Minutes");
                        timeProgressBarEditActivity.setProgress(100 * newVal/100);
                    }
                }else{
                    String tt;
                    if (hours == 1){
                        if (minutes == 1){
                            tt = String.format("%d Hour \n %02d Minute", hours , minutes);
                        }else{
                            tt = String.format("%d Hour \n %02d Minutes", hours , minutes);
                        }
                    }else{
                        if (minutes == 1){
                            tt = String.format("%d Hours \n %02d Minute", hours , minutes);
                        }else{
                            tt = String.format("%d Hours \n %02d Minutes", hours , minutes);
                        }
                    }
                    timeProgressBarTextView.setText(tt);
                    timeProgressBarEditActivity.setProgress(100 * newVal/100);
                }
            }
        });
        difficultyProgressBarEditNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                difficultyProgressBarTextView.setText(String.format("Difficulty: %d/5", newVal));
                difficultyProgressBarEditActivity.setProgress(newVal*20);
            }
        });

        //save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send to firebase

                //go back to RecipeActivity
                startActivity(new Intent(getApplicationContext(), RecipeActivity.class).putExtra("user", user).putExtra("recipePassThrough", recipePassThrough));
            }
        });
    }
}