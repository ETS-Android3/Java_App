package uk.ac.abertay.cmp400.java_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class InfoPage extends AppCompatActivity {

    //Firebase auth and store
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //views/references
    ListenerRegistration registration;
    DocumentReference documentReference;
    TextView welcomeTextView;
    CheckBox checkBox;
    ActionBar actionBar;
    TextView mainTextView;


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

        welcomeTextView = findViewById(R.id.InfoPageWelcomeTextBox);
        checkBox = findViewById(R.id.ShowMeCheckBox);
        mainTextView = findViewById(R.id.InfoPageMainTextView);
    }

    @Override
    protected void onStart() {
        //set listeners
        super.onStart();
        registration = documentReference.addSnapshotListener(this, (value, error) -> {
            try {
                String title = value.getString("InfoPageTitle");
                String maintext = value.getString("InfoPageMainText");
                Spanned output = Html.fromHtml(title + maintext, Html.FROM_HTML_MODE_LEGACY);

                if(!title.equals("") && !output.equals("")){
                    welcomeTextView.setText(title);
                    mainTextView.setText(output);
                }

                Boolean ShowInfoPage = value.getBoolean("ShowInfoPage");
                checkBox.setChecked(!ShowInfoPage);
            } catch (Exception e) {
                Log.e("InfoPage", "OnEvent: " + e.getMessage());
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