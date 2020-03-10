/* https://www.developer.com/ws/android/programming/working-with-fragments-in-android-applications.html
http://www.downloadinformer.com/how-to-use-switch-statement-in-androidjava/
 */
package com.example.gamedna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    //Firebase Authentication

    FirebaseAuth firebaseAuth;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Change title of the Actionbar

        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        //Firebase Initialisation

        firebaseAuth = FirebaseAuth.getInstance();

        //Navigation

        BottomNavigationView navigationView = findViewById(R.id.bottomnav);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //Default fragment when user logs in

        actionBar.setTitle("Home");
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction fragt1 = getSupportFragmentManager().beginTransaction();
        fragt1.replace(R.id.content, fragment1, ""); //Replace current fragment with the home page
        fragt1.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //handle menu clicks
                    switch (menuItem.getItemId()) { //switch case for the different pages on the app
                        case R.id.nav_home:
                            //Home Fragment
                            actionBar.setTitle("Home");
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction fragt1 = getSupportFragmentManager().beginTransaction();
                            fragt1.replace(R.id.content, fragment1, "");
                            fragt1.commit();
                            return true;
                        case R.id.nav_profile:
                            //Profile Fragment
                            actionBar.setTitle("Profile");
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction fragt2 = getSupportFragmentManager().beginTransaction();
                            fragt2.replace(R.id.content, fragment2, "");
                            fragt2.commit();
                            return true;
                        case R.id.nav_inbox:
                            //Inbox Fragment
                            actionBar.setTitle("Inbox");
                            InboxFragment fragment3 = new InboxFragment();
                            FragmentTransaction fragt3 = getSupportFragmentManager().beginTransaction();
                            fragt3.replace(R.id.content, fragment3, "");
                            fragt3.commit();
                            return true;
                    }
                    return false;
                }
            };

    private void checkLoginStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //User already signed in

            //mProfileT.setText(user.getEmail());

        } else {
            //User not signed in
            startActivity(new Intent(MenuActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() { //When app is first opened

        checkLoginStatus();
        super.onStart();
    }

    /*Menu options inflated*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*Menu clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkLoginStatus();
        }

        return super.onOptionsItemSelected(item);
    }
}