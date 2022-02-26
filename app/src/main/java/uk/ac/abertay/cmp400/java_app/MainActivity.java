package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

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

        handler.postDelayed(mSplashDelay, 1000);
    }

    private Runnable mSplashDelay = new Runnable() {
        @Override
        public void run() {
            if(fAuth.getCurrentUser() != null){
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                finish();
            }else {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        }
    };
}