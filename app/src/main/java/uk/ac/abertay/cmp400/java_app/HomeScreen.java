package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.Nullable;
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
    TextView username;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, GetMyList());
        recyclerView.setAdapter(myAdapter);

        //hide action bar
        this.getSupportActionBar().hide();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        username = findViewById(R.id.userameTextView);

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    String txt = "Hello " + value.getString("Username");
                    username.setText(txt);
                }catch(Exception e){}
            }
        });
    }

    private ArrayList<Model> GetMyList() {
        ArrayList<Model> models = new ArrayList<>();
        Model m = new Model();

        m.setTitle("Variables and Operators");
        m.setDescription("This section will look over all the Java Varibles and Operators");
        m.setImg(R.drawable.ic_person);
        models.add(m);

        m.setTitle("Variables and Operators");
        m.setDescription("This section will look over all the Java Varibles and Operators");
        m.setImg(R.drawable.ic_person);
        models.add(m);

        m.setTitle("Variables and Operators");
        m.setDescription("This section will look over all the Java Varibles and Operators");
        m.setImg(R.drawable.ic_person);
        models.add(m);

        m.setTitle("Variables and Operators");
        m.setDescription("This section will look over all the Java Varibles and Operators");
        m.setImg(R.drawable.ic_person);
        models.add(m);

        m.setTitle("Variables and Operators");
        m.setDescription("This section will look over all the Java Varibles and Operators");
        m.setImg(R.drawable.ic_person);
        models.add(m);

        m.setTitle("Variables and Operators");
        m.setDescription("This section will look over all the Java Varibles and Operators");
        m.setImg(R.drawable.ic_person);
        models.add(m);

        m.setTitle("Variables and Operators");
        m.setDescription("This section will look over all the Java Varibles and Operators");
        m.setImg(R.drawable.ic_person);
        models.add(m);

        m.setTitle("Variables and Operators");
        m.setDescription("This section will look over all the Java Varibles and Operators");
        m.setImg(R.drawable.ic_person);
        models.add(m);

        return models;
    }

    public void logout(View view){
        Log.d(TAG, "User: " + fAuth.getCurrentUser().getUid() + " Logged out");
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}