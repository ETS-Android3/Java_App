package uk.ac.abertay.cmp400.java_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide Action Bar
        this.getSupportActionBar().hide();

        fAuth = FirebaseAuth.getInstance();

        //flash the splash screen
        handler.postDelayed(mSplashDelay, 600);
    }

    private final Runnable mSplashDelay = new Runnable() {
        @Override
        public void run() {
            //check if user is logeed in or not. eiter send the to the loign screen or the Home screen
            Intent intent;
            if(fAuth.getCurrentUser() != null){
                intent = new Intent(getApplicationContext(), HomeScreen.class);
            }else {
                intent = new Intent(getApplicationContext(), Login.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
    };
}