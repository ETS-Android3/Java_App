package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide Action Bar
        this.getSupportActionBar().hide();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);



        //flash the splash screen
        handler.postDelayed(mSplashDelay, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CollectionReference collectionReference = fStore.collection("topics");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                   for(QueryDocumentSnapshot document : task.getResult()){
                       Log.i("TAG", document.getId() + " => " + document.getData());

                   }
                }else{

                    Log.e("InfoPage", "Error Getting Data");
                }
            }
        });
    }

    private final Runnable mSplashDelay = new Runnable() {
        @Override
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
    };
}