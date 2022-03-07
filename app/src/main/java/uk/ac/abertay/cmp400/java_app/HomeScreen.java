package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.protobuf.Value;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        ActionBar actionBar = getSupportActionBar();

        //assign settings MenuItem
        profile = findViewById(R.id.miProfile);
        settings = findViewById(R.id.miSettings);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        homeScreenAdapter = new HomeScreenAdapter(this, GetMyList());
        recyclerView.setAdapter(homeScreenAdapter);

        int currentTime = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));

        DocumentReference documentReference1 = fStore.collection("users").document(userID);
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    String txt;
                    CurrentUserName = value.getString("Username");
                    if(currentTime < 12) {
                        txt = "Good morning " + CurrentUserName;
                    }else{
                        txt = "Good afternoon " + CurrentUserName;
                    }
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
                Intent intent = new Intent(getApplicationContext(), Settings.class);
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