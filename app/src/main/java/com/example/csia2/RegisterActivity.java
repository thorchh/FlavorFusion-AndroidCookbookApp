package com.example.csia2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // UI references.
    private EditText mEmail, mPassword;
    private Button register;
    private TextView tvLoginActivity;
    FirebaseUser user;

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
                String email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
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
                    toastMessage("Successfully signed in with: " + email);
                    mAuth.signInWithEmailAndPassword(email,pass);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Intent i = new Intent(getApplicationContext(), MainHomeActivity.class);
                    i.putExtra("user", user).putExtra("activity", "200");
                    startActivity(i);
                }else{
                    toastMessage("Registration failed, please try again");
                }
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}