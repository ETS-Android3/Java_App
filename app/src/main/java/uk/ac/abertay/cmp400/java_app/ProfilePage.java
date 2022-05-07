package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;

public class ProfilePage extends AppCompatActivity {

    //firebase auth and store
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //views
    EditText usernameEditText;
    ActionBar actionBar;
    TextView textViewLogo;
    Button setUsername;

    //values
    String userName;
    private static final String TAG = "LOGOUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        //configure action bar. load custom layout to center text.
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //firebase auth and store instances.
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //find view
        usernameEditText = findViewById(R.id.usernameEditText);
        textViewLogo = findViewById(R.id.textViewCustomLogo);
        setUsername = findViewById(R.id.updateUsernameButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userID = fAuth.getCurrentUser().getUid();
        fStore.collection("users").document(userID).addSnapshotListener(MetadataChanges.INCLUDE,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    userName = value.getString("Username");
                    textViewLogo.setText(userName);
                    usernameEditText.setText(userName);
                } catch (Exception e) {
                    Log.e("Profile page", e.getMessage());
                }
            }
        });
    }

    public void logout(View view){
        Log.d(TAG, "User: " + fAuth.getCurrentUser().getUid() + " Logged out");
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void UpdateValues(View view){
        //check if new username is over 4 characters then updates fireStore.
        if (usernameEditText.length() < 4) {
            usernameEditText.setError("Username must be more that 3 characters.");
        } else {
            String tmp = usernameEditText.getText().toString().trim();
            fStore.collection("users").document(userID).update("Username", tmp);
            textViewLogo.setText(userName);
            //Toast.makeText(this, "Username Updated", Toast.LENGTH_SHORT).show();
        }
    }

}