/*https://medium.com/@ssaurel/create-a-splash-screen-on-android-the-right-way-93d6fb444857
 */
package com.example.gamedna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.gamedna.LoginActivity;
import com.example.gamedna.R;

public class SplashActivity extends AppCompatActivity {

    private int SLEEP_TIME = 3; //Time in seconds "3" = 3 seconds etc.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //Blank Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Hide bar of the phone

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide(); //Makes the Splash Screen full-sized

        SplashScreen splashScreen = new SplashScreen(); //Creating the Splash Screen's object
        splashScreen.start(); //Starting the object
    }

    private class SplashScreen extends Thread {
        public void run() {
            try {
                sleep(1000 * SLEEP_TIME); //Making the splash screen last three seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            SplashActivity.this.finish(); //Closing off the Splash Screen, user is transferred to the login page
        }
    }
}