package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Settings extends AppCompatActivity {

    //firebase auth and store
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //values
    boolean ShowAudioPlayer;
    double PlaybackSpeed;

    //views
    Switch audioBoolSwitch;
    DocumentReference documentReference;
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
        userID = fAuth.getCurrentUser().getUid();

        //Find View By ID Setup
        audioBoolSwitch = findViewById(R.id.ToggleAudioSwitch);
        speedSpinner = findViewById(R.id.SpeedSpinner);

        documentReference = fStore.collection("users").document(userID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ShowAudioPlayer = document.getBoolean("ShowAudioPlayer");
                        PlaybackSpeed = document.getDouble("PlaybackSpeed");
                    } else {
                        Log.d("Settings: GetData", "No such document");
                    }

                } else {
                    Log.d("Settings: GetData", "get failed with ", task.getException());
                }

                //Set current spinner selection depening on what is returned from firebase for the logged in user
                if(PlaybackSpeed == 1.2){
                    speedSpinner.setSelection(0);
                }else if(PlaybackSpeed == 1.1){
                    speedSpinner.setSelection(1);
                }else if(PlaybackSpeed == 0.9){
                    speedSpinner.setSelection(3);
                }else if(PlaybackSpeed == 0.8){
                    speedSpinner.setSelection(4);
                }else{
                    speedSpinner.setSelection(2);
                }

                //set Switch to value from Firebase
                audioBoolSwitch.setChecked(ShowAudioPlayer);
            }
        });

        audioBoolSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ShowAudioBool) {
                //Update ShowAudioPlayer Boolean state in Firebase. Log results.
                documentReference.update("ShowAudioPlayer", ShowAudioBool).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            }
        });

        speedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //update on item selected
                //Toast.makeText(Settings.this, adapterView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        documentReference.update("PlaybackSpeed",1.2);
                        break;
                    case 1:
                        documentReference.update("PlaybackSpeed",1.1);
                        break;
                    case 2:
                        documentReference.update("PlaybackSpeed",1);
                        break;
                    case 3:
                        documentReference.update("PlaybackSpeed",0.9);
                        break;
                    case 4:
                        documentReference.update("PlaybackSpeed",0.8);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void goToSettings(View view){
        //Sends the user to the Accessibility settings page.
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);

    }
}