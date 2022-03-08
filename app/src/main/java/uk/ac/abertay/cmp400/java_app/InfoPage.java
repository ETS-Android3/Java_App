package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class InfoPage extends AppCompatActivity {

    //Firebase auth and store
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //values
    String CurrentUserName;
    boolean ShowInfoPage;

    //views/references
    ListenerRegistration registration;
    DocumentReference documentReference;
    TextView WelcomeTextView;
    CheckBox checkBox;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);

        //actionBar
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.hide();

        //Firebase auth and store instances
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        documentReference = fStore.collection("users").document(userID);

        WelcomeTextView = findViewById(R.id.InfoPageWelcomeTextBox);
        checkBox = findViewById(R.id.ShowMeCheckBox);
    }

    @Override
    protected void onStart() {
        //set listeners
        super.onStart();
        registration = documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    CurrentUserName = value.getString("Username");
                    WelcomeTextView.setText("Welcome "+CurrentUserName);
                    ShowInfoPage = value.getBoolean("ShowInfoPage");
                    checkBox.setChecked(!ShowInfoPage);
                } catch (Exception e) {
                    Log.e("InfoPage", "OnEvent: " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        registration.remove();
    }

    public void MoveToHome(View view){
        boolean checked;
        checked = !checkBox.isChecked();
        documentReference.update("ShowInfoPage",checked);
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}