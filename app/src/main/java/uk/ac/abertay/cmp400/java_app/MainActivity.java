package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.gson.Gson;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide Action Bar
        this.getSupportActionBar().hide();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        sharedPref = getSharedPreferences(getResources().getString(R.string.preference_file_key), MODE_PRIVATE);

        if(!sharedPref.contains("basics_of_java_version")){
            sharedPref.edit().putInt("basics_of_java_version",0).
                    putInt("variables_version",0).
                    putInt("data_type_version",0).
                    putInt("operators_version",0).
                    putInt("conditional_version",0).apply();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(isConected(this)) {
            DocumentReference documentReferenceVersions = fStore.collection("topics").document("versions");
            documentReferenceVersions.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.getDouble("basics_of_java").intValue() != sharedPref.getInt("basics_of_java_version", 0)) {
                            LoadData("basics_of_java");
                            updateVersion("basics_of_java", "basics_of_java_version");
                        }
                        if (document.getDouble("variables").intValue() != sharedPref.getInt("variables_version", 0)) {
                            LoadData("variables");
                            updateVersion("variables", "variables_version");
                        }
                        if (document.getDouble("data_types").intValue() != sharedPref.getInt("data_type_version", 0)) {
                            LoadData("data_types");
                            updateVersion("data_types", "data_type_version");
                        }
                        if (document.getDouble("operators").intValue() != sharedPref.getInt("operators_version", 0)) {
                            LoadData("operators");
                            updateVersion("operators", "operators_version");
                        }
                        if (document.getDouble("conditional").intValue() != sharedPref.getInt("conditional_version", 0)) {
                            LoadData("conditional");
                            updateVersion("conditional", "conditional_version");
                        }
                        run();
                    }
                }
            });
        }else{
            run();
        }
    }

    public void LoadData(String DocumentName){
        DocumentReference documentReference = fStore.collection("topics").document(DocumentName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Gson gson = new Gson();
                    ArrayList<String> title_arraylist = (ArrayList<String>) document.getData().get("title");
                    ArrayList<String> description_arraylist = (ArrayList<String>) document.getData().get("description");

                    String title_json = gson.toJson(title_arraylist);
                    String description_json = gson.toJson(description_arraylist);

                    sharedPref.edit().putString(document.getId()+"_title", title_json).apply();
                    sharedPref.edit().putString(document.getId()+"_description", description_json).apply();
                }else{
                    Log.e("InfoPage", "Error Getting Data");
                }
            }
        });
    }

    public void updateVersion(String version, String versionName){
        DocumentReference documentReferenceVersions = fStore.collection("topics").document("versions");
        documentReferenceVersions.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    //Log.i("tag", ""+document.getDate("basics_of_java");
                    sharedPref.edit().putInt(versionName,document.getDouble(version).intValue()).apply();
                }
            }
        });

    }

    public void run() {
        //check if user is logeed in or not. eiter send the to the loign screen or the Home screen
        Intent intent;
        if(fAuth.getCurrentUser() != null){
            intent = new Intent(getApplicationContext(), HomeScreen.class);
        }else {
            intent = new Intent(getApplicationContext(), Login.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    private boolean isConected(Context c){
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
