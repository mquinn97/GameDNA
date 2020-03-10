/*To make this code possible I used the following sources
https://developer.android.com/studio/write/firebase
https://firebase.google.com/docs/auth
https://firebase.google.com/docs/auth/web/password-auth
https://firebase.google.com/docs/auth/web/manage-users
 */
package com.example.gamedna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //Initialisation

    EditText mEmailEdit, mPasswordEdit;
    TextView mAlreadyAccount, mRecoverPassword;
    Button mLoginButton;

    //Loading bar for Registration

    ProgressDialog progressDialog;

    //Firebase Authentication

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Change title of the Actionbar

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("User Login");

        //Back button to go back

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Initialise Firebase

        mAuth = FirebaseAuth.getInstance();

        //Linking Java to XML Components

        mEmailEdit = findViewById(R.id.emailEdit);
        mPasswordEdit = findViewById(R.id.passwordEdit);
        mAlreadyAccount = findViewById(R.id.already_accountT);
        mRecoverPassword = findViewById(R.id.recoverPassword);
        mLoginButton = findViewById(R.id.login_button);

        //Login button is clicked on

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User data
                String email = mEmailEdit.getText().toString();
                String passw = mPasswordEdit.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //Invalid email
                    mEmailEdit.setError("Invalid Email");
                    mEmailEdit.setFocusable(true);
                } else {
                    //Valid email address
                    loginUser(email, passw);
                }
            }
        });

        //If the user doesn't have an account

        mAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        //When recover password is clicked

        mRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPassword();
            }
        });

        //Initialise Loading bar

        progressDialog = new ProgressDialog(this);

    }

    private void showRecoverPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //Set title
        builder.setTitle("Recover Password");

        LinearLayout linearLayout = new LinearLayout(this);

        final EditText emailT = new EditText(this);
        emailT.setHint("Email Address");
        emailT.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailT.setMinEms(15); //Changing the size of letters regardless of text size

        linearLayout.addView(emailT);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //User email inputted

                String email = emailT.getText().toString().trim();
                startRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { //If the user cancels the request
                dialog.dismiss();
            }
        });


        //Show forgot password form

        builder.create().show();

    }

    private void startRecovery(String email) {

        // Progress of logging in
        progressDialog.setMessage("Sending email...");
        progressDialog.show();

        //Firebase Authentication

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener < Void > () {
                    @Override
                    public void onComplete(@NonNull Task < Void > task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email has been sent, check your inbox", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email not sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.show();
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show(); //Generated Error message

            }
        });
    }

    private void loginUser(String email, String passw) {

        // Progress of logging in
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        //Checking Firebase for the correct username and password

        mAuth.signInWithEmailAndPassword(email, passw) //Checks the current user on the app and transfers the user to the main menu
                .addOnCompleteListener(this, new OnCompleteListener < AuthResult > () {
                    @Override
                    public void onComplete(@NonNull Task < AuthResult > task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override //Previous activity when clicked
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}