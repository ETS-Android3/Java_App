package uk.ac.abertay.cmp400.java_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePage extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String userName;
    EditText usernameEditText;
    ActionBar actionBar;
    private static final String TAG = "LOGOUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile Page");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        usernameEditText = findViewById(R.id.usernameEditText);

        userName = getIntent().getStringExtra("username");

        usernameEditText.setText(userName);
    }

    public void logout(View view){
        Log.d(TAG, "User: " + fAuth.getCurrentUser().getUid() + " Logged out");
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }

    public void UpdateValues(View view){

        if (usernameEditText.length() < 4){
            usernameEditText.setError("Username must be more that 3 characters.");
        }else{
            String tmp = usernameEditText.getText().toString().trim();

            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.update("Username", tmp);
            Toast.makeText(this, "Username Updated", Toast.LENGTH_SHORT).show();
        }
    }
}