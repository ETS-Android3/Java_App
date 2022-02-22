package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    RecyclerView recyclerView;
    HomeScreenAdapter homeScreenAdapter;
    String userID;
    BottomNavigationItemView profile;
    String CurrentUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //todo use this to Set Theme's 
        //this.setTheme(R.style.Theme_Java_App);

        //assign settings MenuItem
        profile = findViewById(R.id.miProfile);

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        homeScreenAdapter = new HomeScreenAdapter(this, GetMyList());
        recyclerView.setAdapter(homeScreenAdapter);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        ActionBar actionBar = getSupportActionBar();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    CurrentUserName = value.getString("Username");
                    String txt = "Hello " + CurrentUserName;
                    actionBar.setTitle(txt);
                }catch(Exception e){}
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                intent.putExtra("username", CurrentUserName);
                startActivity(intent);
            }
        });
    }

    private ArrayList<HomeScreenModel> GetMyList() {
        ArrayList<HomeScreenModel> homeScreenModels = new ArrayList<>();

        HomeScreenModel m = new HomeScreenModel();
        m.setTitle("The Basic Syntax of Java");
        m.setDescription("This section provide and overview of basics syntax in java.");
        m.setImg(R.drawable.java_icon_bw);
        homeScreenModels.add(m);

        m = new HomeScreenModel();
        m.setTitle("Variables");
        m.setDescription("This section will look at how variables are formatted in java.");
        m.setImg(R.drawable.variable_icon);
        homeScreenModels.add(m);

        m = new HomeScreenModel();
        m.setTitle("Data Types");
        m.setDescription("This section will look at the Data Types used is java.");
        m.setImg(R.drawable.data_types_icon);
        homeScreenModels.add(m);

        m = new HomeScreenModel();
        m.setTitle("Operators in Java");
        m.setDescription("This section will look at the various operators used is java.");
        m.setImg(R.drawable.operator_icon);
        homeScreenModels.add(m);

        m = new HomeScreenModel();
        m.setTitle("Conditional Statements");
        m.setDescription("This section will Build on previous information and look as Conditional Statements.");
        m.setImg(R.drawable.condition_icon);
        homeScreenModels.add(m);

        m = new HomeScreenModel();
        homeScreenModels.add(m);

        return homeScreenModels;
    }
}