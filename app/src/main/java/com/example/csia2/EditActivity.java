package com.example.csia2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    FirebaseUser user;
    Boolean saved;
    float userRating;
    String imgURI;
    String colourTag;
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imageView;
    Uri mImgURI;
    ArrayList<String> arrayList;
    ArrayList<String> instructionsArrayList;

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
        imageView = findViewById(R.id.recipeIMGEditActivity);
        LinearLayout instructionsLinearLayoutEditActivity = (LinearLayout)findViewById(R.id.instructionsLinearLayoutEditActivity);

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
        Picasso.get().load(imgURI).into((ImageView) findViewById(R.id.recipeIMGEditActivity));
        difficultyProgressBarEditActivity.setProgress(recipePassThrough.getDifficulty()*20);
        difficultyProgressBarTextView.setHint(String.format("Difficulty: %d/5", recipePassThrough.getDifficulty()));
        timeProgressBarEditActivity.setProgress(recipePassThrough.getTime());

        //Ingredients ArrayList
        arrayList = recipePassThrough.getingridientsChecklist().get(0);
        LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linf = LayoutInflater.from(EditActivity.this);
        for (int i = 0; i< arrayList.size();i++){

            View v = linf.inflate(R.layout.itemlayoutnocheckbox, null);

            EditText tv = ((EditText) v.findViewById(R.id.linearLayoutEditTextView));
            tv.setText((String)(arrayList.get(i)));
            final int finalI = i;
            tv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    arrayList.set(finalI, s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            ingredientsLinearLayoutEditActivity.addView(v);
        }

        //Instructions ArrayList
        instructionsArrayList = recipePassThrough.getInstructionsArrayList();
        LinearLayout instructions = (LinearLayout)findViewById(R.id.instructionsLinearLayoutEditActivity);
        for (int i = 0; i< instructionsArrayList.size();i++){

            View v = linf.inflate(R.layout.instructionslayouteditactivity, null);
            EditText tv = ((EditText) v.findViewById(R.id.instructionsLinearLayoutTextViewEditActivity));
            TextView tv2 = (TextView) v.findViewById(R.id.instructionsLinearLayoutStepEditActivity);
            tv.setText((String)(instructionsArrayList.get(i)));
            tv2.setText("Step " + (i+1) + " :");
            final int finalI = i;
            tv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    instructionsArrayList.set(finalI, s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            instructionsLinearLayoutEditActivity.addView(v);
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

        //img pick and upload
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data
                String tempTitle = recipeTitleEditText.getText().toString();
                Integer tempTime = (timeProgressBarEditActivity.getProgress());
                Integer tempDifficulty = difficultyProgressBarEditActivity.getProgress()/20;
                String tempDescription = (String) recipeDescEditText.getText().toString();
                ArrayList<ArrayList> ingridientsChecklist = new ArrayList<>();
                ArrayList<Boolean> checklist = new ArrayList<>();
                ArrayList<String> ingridients = new ArrayList<>();
                for(int i = 0; i< arrayList.size(); i++){ ingridients.add(arrayList.get(i)); checklist.add(false); }
                ingridientsChecklist.add(ingridients); ingridientsChecklist.add(checklist);
                System.out.println(instructionsArrayList);
                // need to add functionality to add and remove ingridients

                // need to add img functionality: push to firebase database and storage

                //send to firebase (to user branch)

                //go back to RecipeActivity
                startActivity(new Intent(getApplicationContext(), RecipeActivity.class).putExtra("user", user).putExtra("recipePassThrough", recipePassThrough));
            }
        });
    }

    //img chooser
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImgURI = data.getData();
            Picasso.get().load(mImgURI).into(imageView);
        }
    }
}