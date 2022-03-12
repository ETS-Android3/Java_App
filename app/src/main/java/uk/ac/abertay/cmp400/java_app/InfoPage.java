package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    //views/references
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

        welcomeTextView = findViewById(R.id.InfoPageWelcomeTextBox);
        checkBox = findViewById(R.id.ShowMeCheckBox);
        mainTextView = findViewById(R.id.InfoPageMainTextView);
    }

    @Override
    protected void onStart() {
        //set listeners
        super.onStart();

        userID = fAuth.getCurrentUser().getUid();
        fStore.collection("global").document("variables").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    String title = value.getString("InfoPageTitle");
                    String maintext = value.getString("InfoPageMainText");
                    Spanned output = Html.fromHtml(maintext, Html.FROM_HTML_MODE_LEGACY);

                    if (!title.equals("") && !output.equals("")) {
                        welcomeTextView.setText(title);
                        mainTextView.setText(output);
                    }
                } catch (Exception e) {
                    Log.e("InfoPage", "OnEvent: " + e.getMessage());
                }
            }
        });

        fStore.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Boolean ShowInfoPage = document.getBoolean("ShowInfoPage");
                    checkBox.setChecked(!ShowInfoPage);
                }
            }
        });
    }

    public void MoveToHome(View view){
        boolean checked;
        checked = !checkBox.isChecked();
        fStore.collection("users").document(userID).update("ShowInfoPage", checked);
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}