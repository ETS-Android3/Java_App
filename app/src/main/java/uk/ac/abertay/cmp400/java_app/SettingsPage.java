package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class SettingsPage extends PreferenceActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        Load_settings();
    }

    private void Load_settings(){

        CheckBoxPreference CP = (CheckBoxPreference) findPreference("PLAYBACK");

        CP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Boolean Bool = (Boolean) o;

                DocumentReference documentReference = fStore.collection("users").document(userID);
                if(Bool){
                    documentReference.update("HideAudioPlayer",false);
                }else{
                    documentReference.update("HideAudioPlayer",true);
                }
                return true;
            }
        });


        ListPreference LP = (ListPreference)findPreference("PLAYBACKSPEED");

        LP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String item = (String)o;
                if(preference.getKey().equals("PLAYBACKSPEED")){
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    switch (item){
                        case "1":
                            documentReference.update("PlaybackSpeed",1.2);
                            break;
                        case "2":
                            documentReference.update("PlaybackSpeed",1.1);
                            break;
                        case "3":
                            documentReference.update("PlaybackSpeed",1);
                            break;
                        case "4":
                            documentReference.update("PlaybackSpeed",0.9);
                            break;
                        case "5":
                            documentReference.update("PlaybackSpeed",0.8);
                            break;
                    }
                }
                return true;
            }
        });
    }

}