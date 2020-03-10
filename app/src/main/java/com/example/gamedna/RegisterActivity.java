/*To make this code possible I used the following sources
https://firebase.google.com/docs/auth
https://medium.com/@sfazleyrabbi/firebase-login-and-registration-authentication-99ea25388cbf
 */


package com.example.gamedna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Initialisation

    EditText mEmailEdit, mPasswordEdit;
    Button mRegisterButton;
    TextView mAlreadyAccountT;

    //Loading bar for Registration

    ProgressDialog progressDialog;

    //Firebase Authentication

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Change title of the Actionbar

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        //Back button to go back

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Linking Java to XML Components

        mEmailEdit = findViewById(R.id.emailEdit);
        mPasswordEdit = findViewById(R.id.passwordEdit);
        mRegisterButton = findViewById(R.id.register_button);
        mAlreadyAccountT = findViewById(R.id.already_accountT);

        //Firebase Initialisation

        mAuth = FirebaseAuth.getInstance();

        //Loading bar setup

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");

        //When Register Button is clicked

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Input user's email address and password

                String email = mEmailEdit.getText().toString().trim();
                String password = mPasswordEdit.getText().toString().trim();

                //Validation

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { //If error found

                    mEmailEdit.setError("Invalid Email Address");
                    mEmailEdit.setFocusable(true);

                }
                else if (password.length()<6){

                    mPasswordEdit.setError("Password needs to be at least 6 characters");
                    mPasswordEdit.setFocusable(true);

                }
                else {
                    registerUser(email, password); //Registration successful
                }
            }
        });
        //User already had an account
        mAlreadyAccountT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); //Switches to login page
                finish();
            }
        });

    }

    private void registerUser(String email, String password) { //Both valid, show loading bar

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password) //Firebase authorisation
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();

                            //Store user data

                            HashMap<Object, String> hashMap = new HashMap<>();

                            //Information stored to hashmap

                            hashMap.put("email", email); //Linked to Firebase, store items in key/value pairs accessed by the strings
                            hashMap.put("uid", "");
                            hashMap.put("name", "");
                            hashMap.put("image", "");

                            //Firebase database

                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            //Store data location

                            DatabaseReference reference = database.getReference("Users");

                            //Database Hashmap has data inputted

                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Progress Bar not shown and error message given

                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override //Previous activity when clicked
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}