package uk.ac.abertay.cmp400.java_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfilePage extends AppCompatActivity {

    FirebaseAuth fAuth;
    String UserName;
    TextView userNameTextView;
    ActionBar actionBar;
    private static final String TAG = "LOGOUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        fAuth = FirebaseAuth.getInstance();

        userNameTextView = findViewById(R.id.UserNameTextView);

        UserName = getIntent().getStringExtra("username");

        userNameTextView.setText(UserName);
    }

    public void logout(View view){
        Log.d(TAG, "User: " + fAuth.getCurrentUser().getUid() + " Logged out");
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}