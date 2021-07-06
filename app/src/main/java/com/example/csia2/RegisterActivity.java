package com.example.csia2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // UI references.
    private EditText mEmail, mPassword;
    private Button register;
    private TextView tvLoginActivity;
    DatabaseReference reff;
    FirebaseUser user;
    DataSnapshot finalCopy;
    int numRecipe;
    String email;
    String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //declare text views and edit texts in oncreate
        mEmail = (EditText) findViewById(R.id.emailRegister);
        mPassword = (EditText) findViewById(R.id.passwordRegister);
        register = (Button) findViewById(R.id.register);
        tvLoginActivity = (TextView) findViewById(R.id.alreadyReg);
        mAuth = FirebaseAuth.getInstance();




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmail.getText().toString();
                pass = mPassword.getText().toString();
                if(!email.equals("") && !pass.equals("")){
                    registerUser(email, pass);
                }else if (pass.length() < 6){
                    toastMessage("Password length must be at least 6 characters long");
                }else{
                    toastMessage("You didn't fill in all the fields.");
                }
            }
        });
        tvLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginRegister.class));
            }
        });

    }

    private void registerUser(String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    reff = FirebaseDatabase.getInstance().getReference();
                    System.out.println(reff.child("RecipeMain"));
/*
                        would need to add some delay, async is the problem
*/
                    reff.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            finalCopy = task.getResult();
                            System.out.println(finalCopy);
                            init();
                        }
                    });
                        //  */
                    
                }else{
                    toastMessage("Registration failed, please try again");
                }
            }
        });
    }

    public void init(){
        //reff.child("Recipe" + email.substring(0,(email.indexOf('@')))).setValue(finalCopy.getValue());
/*                    ArrayList<Boolean> arr = new ArrayList();
                    reff.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            System.out.println("snap" + snapshot);
                            finalCopy = snapshot;
                            System.out.println("final copy" + finalCopy.child("recipemain").getValue());
                            numRecipe = ((ArrayList) finalCopy.child("recipemain").getValue()).size();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    for (int i = 0; i < numRecipe; i++) {arr.add(true);}*/
        reff.child("Recipe" + email.substring(0,(email.indexOf('@')))).setValue(finalCopy.child("RecipeMain").getValue());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.signInWithEmailAndPassword(email,pass);

        toastMessage("Successfully signed in with: " + email);
        Intent i = new Intent(getApplicationContext(), MainHomeActivity.class);
        i.putExtra("user", user).putExtra("activity", "200");
        startActivity(i);
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}