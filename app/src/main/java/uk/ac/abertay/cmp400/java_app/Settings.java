package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Settings extends AppCompatActivity {

    //firebase auth and store
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //values
    boolean showAudioPlayer;
    double playbackSpeed;

    //views
    Switch audioBoolSwitch;
    ActionBar actionBar;
    Spinner speedSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Action Bar Setup.
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Settings");

        //Firebase and current user ID Setup.
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        //Find View By ID Setup
        audioBoolSwitch = findViewById(R.id.ToggleAudioSwitch);
        speedSpinner = findViewById(R.id.SpeedSpinner);
    }

    @Override
    protected void onStart() {
        super.onStart();

            userID = fAuth.getCurrentUser().getUid();
            fStore.collection("users").document(userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        showAudioPlayer = document.getBoolean("ShowAudioPlayer");
                        playbackSpeed = document.getDouble("PlaybackSpeed");
                    } else {
                        Log.d("Settings: GetData", "No such document");
                    }

                } else {
                    Log.d("Settings: GetData", "get failed with ", task.getException());
                }

                //Set current spinner selection depening on what is returned from firebase for the logged in user
                if (playbackSpeed == 1.2) {
                    speedSpinner.setSelection(0);
                } else if (playbackSpeed == 1.1) {
                    speedSpinner.setSelection(1);
                } else if (playbackSpeed == 0.9) {
                    speedSpinner.setSelection(3);
                } else if (playbackSpeed == 0.8) {
                    speedSpinner.setSelection(4);
                } else {
                    speedSpinner.setSelection(2);
                }

                //set Switch to value from Firebase
                audioBoolSwitch.setChecked(showAudioPlayer);
            });


            audioBoolSwitch.setOnCheckedChangeListener((compoundButton, ShowAudioBool) -> {
                //Update ShowAudioPlayer Boolean state in Firebase. Log results.
                fStore.collection("users").document(userID).update("ShowAudioPlayer", ShowAudioBool).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Settings: Update", "DocumentSnapshot successfully updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Settings: Update", "Error updating document: ", e);
                    }
                });
            });

            speedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    //update on item selected
                    float playBackSpeed;
                    switch (position) {
                        case 0:
                            playBackSpeed = 1.2f;
                            break;
                        case 1:
                            playBackSpeed = 1.1f;
                            break;
                        case 2:
                            playBackSpeed = 1f;
                            break;
                        case 3:
                            playBackSpeed = 0.9f;
                            break;
                        case 4:
                            playBackSpeed = 0.8f;
                            break;
                        default:
                            playBackSpeed = 1f;
                    }
                    fStore.collection("users").document(userID).update("PlaybackSpeed", playBackSpeed);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
    }

    public void goToSettings(View view){
        //Sends the user to the Accessibility settings page.
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);

    }
}