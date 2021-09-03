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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
        ImageButton addInstructionButton = findViewById(R.id.addInstructionButton);
        ImageButton addIngredientButton = findViewById(R.id.addIngredientButton);
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
            //remove ingredient button
            ImageButton removeIngredientButton = v.findViewById(R.id.removeIngredientButton);
            removeIngredientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Remove Ingredient");
                    System.out.println(arrayList);
                    System.out.println(finalI);
                    EditText tempLin =  v.findViewById(R.id.linearLayoutEditTextView);
                    System.out.println((tempLin).getText());
                    // need to change finalI
                    arrayList.remove((tempLin).getText());
                    ingredientsLinearLayoutEditActivity.removeView(v);

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
            //text
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
            //remove instruction button
            ImageButton removeInstructionButton = v.findViewById(R.id.removeInstructionButton);
            removeInstructionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Remove Instruction");
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
        timeProgressBarEditNumPick.setValue(recipePassThrough.getTime());
        difficultyProgressBarEditNumPick.setMaxValue(5);
        difficultyProgressBarEditNumPick.setMinValue(1);
        difficultyProgressBarEditNumPick.setValue(recipePassThrough.getDifficulty());

        timeProgressBarEditNumPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int hours = newVal / 60;
                int minutes = newVal % 60;
                if (newVal <= 60){
                    if (minutes == 1){
                        timeProgressBarTextView.setText(newVal + "\n Minute");
                        timeProgressBarEditActivity.setProgress(newVal);
                    }else{
                        timeProgressBarTextView.setText(newVal + "\n Minutes");
                        timeProgressBarEditActivity.setProgress(newVal);
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
                    timeProgressBarEditActivity.setProgress(newVal);
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

        //add ingredient button
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.add("");
                LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                linf = LayoutInflater.from(EditActivity.this);
                View v = linf.inflate(R.layout.itemlayoutnocheckbox, null);
                EditText tv = ((EditText) v.findViewById(R.id.linearLayoutEditTextView));
                tv.setText("");
                ingredientsLinearLayoutEditActivity.addView(v);
            }
        });

        //add instruction button
        addInstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                linf = LayoutInflater.from(EditActivity.this);
                View v = linf.inflate(R.layout.instructionslayouteditactivity, null);
                EditText tv = ((EditText) v.findViewById(R.id.instructionsLinearLayoutTextViewEditActivity));
                TextView tv2 = (TextView) v.findViewById(R.id.instructionsLinearLayoutStepEditActivity);
                tv.setText("");
                instructionsArrayList.add("");
                int i = instructionsArrayList.toArray().length;
                System.out.println(i);
                tv2.setText("Step " + (i) + " :");
                instructionsLinearLayoutEditActivity.addView(v);
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

                //send to firebase (to user branch)
                String email = user.getEmail();
                int index = email.indexOf('@');
                email = email.substring(0,index);
                FirebaseDatabase.getInstance().getReference().child(("Recipe" + email)).child("desc").setValue(tempDescription);
                FirebaseDatabase.getInstance().getReference().child(("Recipe" + email)).child("difficulty").setValue(tempDifficulty);
                FirebaseDatabase.getInstance().getReference().child(("Recipe" + email)).child("title").setValue(tempTitle);
                FirebaseDatabase.getInstance().getReference().child(("Recipe" + email)).child("time").setValue(tempTime);
                FirebaseDatabase.getInstance().getReference().child(("Recipe" + email)).child("instructionsArrayList").setValue(instructionsArrayList);
                FirebaseDatabase.getInstance().getReference().child(("Recipe" + email)).child("ingridientsChecklist").setValue(ingridientsChecklist);

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