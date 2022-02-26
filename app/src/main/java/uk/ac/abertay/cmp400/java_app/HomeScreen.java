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
    BottomNavigationItemView settings;
    String CurrentUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        ActionBar actionBar = getSupportActionBar();

        //assign settings MenuItem
        profile = findViewById(R.id.miProfile);
        settings = findViewById(R.id.miSettings);


        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        homeScreenAdapter = new HomeScreenAdapter(this, GetMyList());
        recyclerView.setAdapter(homeScreenAdapter);

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

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<HomeScreenModel> GetMyList() {
        ArrayList<HomeScreenModel> homeScreenModels = new ArrayList<>();

        HomeScreenModel m = new HomeScreenModel();

        String[] title = getResources().getStringArray(R.array.HomeScreenTitle);
        String[] description = getResources().getStringArray(R.array.HomeScreenDescriptions);
        String[] images = getResources().getStringArray(R.array.HomeScreenImages);

        for (int i = 0; i < title.length; i++) {
            m = new HomeScreenModel();;
            m.setTitle(title[i]);
            m.setDescription(description[i]);
            m.setImg(getResources().getIdentifier(images[i], "drawable", getPackageName()));
            homeScreenModels.add(m);
        }
        return homeScreenModels;
    }
}