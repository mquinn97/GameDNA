package com.example.gamedna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Initialisation

    Button mRegisterButton, mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking Java to XML Components

        mRegisterButton = findViewById(R.id.register_button);
        mLoginButton = findViewById(R.id.login_button);

        //When Register Button is Clicked

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Register Activity is started

                startActivity(new Intent(MainActivity.this, RegisterActivity.class));

            }
        });

        //Login button clicked

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login Activity is started

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}
