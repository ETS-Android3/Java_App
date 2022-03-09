package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeScreen extends AppCompatActivity {

    //Firebase auth and store
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //Views
    RecyclerView recyclerView;
    HomeScreenAdapter homeScreenAdapter;
    BottomNavigationItemView profile;
    BottomNavigationItemView settings;
    ActionBar actionBar;

    ListenerRegistration registration;
    DocumentReference documentReference;

    //values
    String CurrentUserName;
    int currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //get Action Bar
        actionBar = getSupportActionBar();

        //assign settings MenuItem
        profile = findViewById(R.id.miProfile);
        settings = findViewById(R.id.miSettings);

        //Firebase auth and store instances
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        //set document reference for users
        documentReference = fStore.collection("users").document(userID);


        //recycler view setup
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        homeScreenAdapter = new HomeScreenAdapter(this, GetMyList());
        recyclerView.setAdapter(homeScreenAdapter);

        //get the current time.
        currentTime = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
    }

    @Override
    protected void onStart() {
        //set listeners
        registration = documentReference.addSnapshotListener(this, (value, error) -> {
            try {
                String txt;
                CurrentUserName = value.getString("Username");
                if(currentTime < 12) {
                    txt = "Good morning " + CurrentUserName;
                }else{
                    txt = "Good afternoon " + CurrentUserName;
                }
                actionBar.setTitle(txt);
            }catch(Exception e){
                Log.e("InfoPage", "OnEvent: " + e.getMessage());
            }
        });
        profile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
            intent.putExtra("username", CurrentUserName);
            startActivity(intent);
        });

        settings.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);
        });
        super.onStart();
    }

    @Override
    protected void onStop() {
        //unregister and remove all listeners
        registration.remove();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_info){
            Intent intent = new Intent(getApplicationContext(), InfoPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<HomeScreenModel> GetMyList() {
        ArrayList<HomeScreenModel> homeScreenModels = new ArrayList<>();

        HomeScreenModel m;

        String[] title = getResources().getStringArray(R.array.HomeScreenTitle);
        String[] description = getResources().getStringArray(R.array.HomeScreenDescriptions);
        String[] images = getResources().getStringArray(R.array.HomeScreenImages);

        for (int i = 0; i < title.length; i++) {
            m = new HomeScreenModel();
            m.setTitle(title[i]);
            m.setDescription(description[i]);
            m.setImg(getResources().getIdentifier(images[i], "drawable", getPackageName()));
            homeScreenModels.add(m);
        }
        return homeScreenModels;
    }
}