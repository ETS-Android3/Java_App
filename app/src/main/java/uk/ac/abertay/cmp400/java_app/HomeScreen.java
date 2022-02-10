package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "LOGOUT";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ActionBar actionBar = getSupportActionBar();

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, GetMyList());
        recyclerView.setAdapter(myAdapter);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    String txt = "Hello " + value.getString("Username");
                    actionBar.setTitle(txt);
                }catch(Exception e){}
            }
        });
    }

    private ArrayList<Model> GetMyList() {
        ArrayList<Model> models = new ArrayList<>();

        Model m = new Model();
        m.setTitle("The Basic Syntax of Java");
        m.setDescription("This section provide and overview of basics syntax in java.");
        m.setImg(R.drawable.ic_varibles_image);
        models.add(m);

        m = new Model();
        m.setTitle("Variables");
        m.setDescription("This section will look at how variables are formatted in java.");
        m.setImg(R.drawable.ic_varibles_image);
        models.add(m);

        m = new Model();
        m.setTitle("Operators in Java");
        m.setDescription("This section will look at the various operators used is java.");
        m.setImg(R.drawable.ic_varibles_image);
        models.add(m);

        m = new Model();
        m.setTitle("Conditional Statements");
        m.setDescription("This section will Build on previous information and look as Conditional Statements.");
        m.setImg(R.drawable.ic_varibles_image);
        models.add(m);

        m = new Model();
        m.setTitle("Course Summary");
        m.setDescription("This section will Summarise and conclude all the previous sections.");
        m.setImg(R.drawable.ic_varibles_image);
        models.add(m);

        m = new Model();
        models.add(m);

        return models;
    }

    //todo Move Logout to Settings page as a sign out button instead.
    public void logout(View view){
        Log.d(TAG, "User: " + fAuth.getCurrentUser().getUid() + " Logged out");
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}