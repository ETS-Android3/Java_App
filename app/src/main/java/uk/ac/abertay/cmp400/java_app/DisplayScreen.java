package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DisplayScreen extends AppCompatActivity {

    //firebase auth and store
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    FirebaseStorage firebaseStorage;
    StorageReference audioStorageReference;


    //views
    RecyclerView recyclerView;
    FloatingActionButton FAB;
    ActionBar actionBar;
    SeekBar audioSeekBar;

    //values
    double playbackSpeed;
    boolean ShowAudioPlayer;
    boolean isPlaying;
    Uri uri;
    int index;
    Context c;

    //references
    MediaPlayer mediaPlayer;
    Handler handler;
    DisplayAdapter displayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_screen);

        c = getApplicationContext();

        //firebase auth and store instances.
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        firebaseStorage = FirebaseStorage.getInstance();


        //get and set action bar with back button.
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //initiate handler.
        handler = new Handler();

        //Views
        audioSeekBar = findViewById(R.id.AudioSeekBar);
        recyclerView = findViewById(R.id.recycleView);
        FAB = findViewById(R.id.AudioFAB);


        //values
        playbackSpeed = 1.00;
        ShowAudioPlayer = false;
        isPlaying = false;
        index = getIntent().getIntExtra("index", 0);

        //references
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayAdapter = new DisplayAdapter(this, getMyList(index));
        recyclerView.setAdapter(displayAdapter);
        audioSeekBar.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        userID = fAuth.getCurrentUser().getUid();
        fStore.collection("users").document(userID).addSnapshotListener(MetadataChanges.INCLUDE,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    playbackSpeed = value.getDouble("PlaybackSpeed");
                    ShowAudioPlayer = value.getBoolean("ShowAudioPlayer");

                    if (ShowAudioPlayer) {
                        FAB.setVisibility(View.VISIBLE);
                    } else {
                        FAB.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("DisplayScreen", e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    private boolean connectedToMobileNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null)return false;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(nw);
        return networkCapabilities != null && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
    }

    private ArrayList<DisplayModel> getMyList(int Index) {
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.preference_file_key), MODE_PRIVATE);
        ArrayList<DisplayModel> models = new ArrayList<>();

        DisplayModel m;

        String title_json;
        String Description_json;

        String LineSpace = "1.6";
        String TextSize = "18px";

        ArrayList<String> Title;
        ArrayList<String> Description;
        Gson gson = new Gson();
        String keywordColor = getResources().getString(R.string.html_red);
        String classesColor = getResources().getString(R.string.classesColor);
        String conditionalColor = getResources().getString(R.string.conditionalColor);
        String textColor = getResources().getString(R.string.textColor);

        switch (Index) {
            case 0:
                Toast.makeText(this, "Whoops, Something went wrong", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                //Audio
                audioStorageReference = firebaseStorage.getReferenceFromUrl("gs://java-app-f00a2.appspot.com/audio/the_basics_syntax_of_java_1.mp3");

                //Basics Of Java
                title_json = sharedPref.getString("basics_of_java_title", null);
                Description_json = sharedPref.getString("basics_of_java_description", null);

                 Title = gson.fromJson(title_json, ArrayList.class);
                 Description = gson.fromJson(Description_json, ArrayList.class);

                //Action Bar Title
                actionBar.setTitle(Title.get(0));

                for (int i = 0; i < Title.size(); i++) {
                    m = new DisplayModel();
                    m.setTitle(Title.get(i));
                    m.setDescription(Description.get(i)
                            .replace("@keyword", keywordColor)
                            .replace("@lineHight", LineSpace)
                            .replace("@fontSize", TextSize)
                            .replace("@classes", classesColor)
                            .replace("@conditional", conditionalColor)
                            .replace("@textColor", textColor));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
            case 2:
                //Audio
                audioStorageReference = firebaseStorage.getReferenceFromUrl("gs://java-app-f00a2.appspot.com/audio/variables.mp3");

                //Variables
                title_json = sharedPref.getString("variables_title", null);
                Description_json = sharedPref.getString("variables_description", null);

                Title = gson.fromJson(title_json, ArrayList.class);
                Description = gson.fromJson(Description_json, ArrayList.class);

                //Action Bar Title
                actionBar.setTitle(Title.get(0));

                for (int i = 0; i < Title.size(); i++) {
                    m = new DisplayModel();
                    m.setTitle(Title.get(i));
                    m.setDescription(Description.get(i)
                            .replace("@keyword", keywordColor)
                            .replace("@lineHight", LineSpace)
                            .replace("@fontSize", TextSize)
                            .replace("@classes", classesColor)
                            .replace("@conditional", conditionalColor)
                            .replace("@textColor", textColor));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;

            case 3:
                //Audio
                audioStorageReference = firebaseStorage.getReferenceFromUrl("gs://java-app-f00a2.appspot.com/audio/data_types.mp3");

                //Data Types
                title_json = sharedPref.getString("data_types_title", null);
                Description_json = sharedPref.getString("data_types_description", null);

                Title = gson.fromJson(title_json, ArrayList.class);
                Description = gson.fromJson(Description_json, ArrayList.class);

                //Action Bar Title
                actionBar.setTitle(Title.get(0));

                for (int i = 0; i < Title.size(); i++) {
                    m = new DisplayModel();
                    m.setTitle(Title.get(i));
                    m.setDescription(Description.get(i)
                            .replace("@keyword", keywordColor)
                            .replace("@lineHight", LineSpace)
                            .replace("@fontSize", TextSize)
                            .replace("@classes", classesColor)
                            .replace("@conditional", conditionalColor)
                            .replace("@textColor", textColor));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
            case 4:
                //Audio
                audioStorageReference = firebaseStorage.getReferenceFromUrl("gs://java-app-f00a2.appspot.com/audio/operators.mp3");

                //Operators
                title_json = sharedPref.getString("operators_title", null);
                Description_json = sharedPref.getString("operators_description", null);

                Title = gson.fromJson(title_json, ArrayList.class);
                Description = gson.fromJson(Description_json, ArrayList.class);

                //Action Bar Title
                actionBar.setTitle(Title.get(0));

                for (int i = 0; i < Title.size(); i++) {
                    m = new DisplayModel();
                    m.setTitle(Title.get(i));
                    m.setDescription(Description.get(i)
                            .replace("@keyword", keywordColor)
                            .replace("@lineHight", LineSpace)
                            .replace("@fontSize", TextSize)
                            .replace("@classes", classesColor)
                            .replace("@conditional", conditionalColor)
                            .replace("@textColor", textColor));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
            case 5:
                //Audio
                audioStorageReference = firebaseStorage.getReferenceFromUrl("gs://java-app-f00a2.appspot.com/audio/conditional_statements.mp3");

                //Conditional Statements
                title_json = sharedPref.getString("conditional_title", null);
                Description_json = sharedPref.getString("conditional_description", null);

                Title = gson.fromJson(title_json, ArrayList.class);
                Description = gson.fromJson(Description_json, ArrayList.class);

                //Action Bar Title
                actionBar.setTitle(Title.get(0));

                for (int i = 0; i < Title.size(); i++) {
                    m = new DisplayModel();
                    m.setTitle(Title.get(i));
                    m.setDescription(Description.get(i)
                            .replace("@keyword", keywordColor)
                            .replace("@lineHight", LineSpace)
                            .replace("@fontSize", TextSize)
                            .replace("@classes", classesColor)
                            .replace("@conditional", conditionalColor)
                            .replace("@textColor", textColor));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
        }
        audioStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri firebaseUri) {
                uri = firebaseUri;
            }
        });
        return models;
    }

    //FAB Audio functionality
    public void play(View view){
        if(uri != null || isConected(c)) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                if(connectedToMobileNetwork()) {
                    Toast.makeText(c, "Using Mobile Data", Toast.LENGTH_SHORT).show();
                }
                try {
                    mediaPlayer.setDataSource(c, uri);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.prepare();
                    mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                        stopPlayer();
                        FAB.setImageResource(R.drawable.ic_play_arrow);
                        isPlaying = false;
                    });
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
            //Media Player Settings: Playback Speed
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(((float) playbackSpeed)));

            //show SeekBar
            audioSeekBar.setVisibility(View.VISIBLE);

            audioSeekBar.setMax(mediaPlayer.getDuration());
            //Media Player Settings: Seek Bar Listener
            audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                    if(fromUser && mediaPlayer != null) {
                        mediaPlayer.seekTo(i);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mediaPlayer.start();
            handler.post(new UpdateSeekBar());
        }else{
            Toast.makeText(c, "No Internet Connection", Toast.LENGTH_SHORT).show();
            FAB.setImageResource(R.drawable.ic_play_arrow);
            isPlaying = false;
        }

    }

    public void pause(View view){
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    public void stop(View view){
        if (mediaPlayer != null){
            stopPlayer();
        }
    }

    private void stopPlayer(){
        //Custom stop. Releases media player and preforms reset.
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            audioSeekBar.setProgress(0);
            audioSeekBar.setVisibility(View.INVISIBLE);
        }
    }

    public void ToggleButton(View view) {
        //toggles FAB icon
        if (!isPlaying) {
            play(view);
            FAB.setImageResource(R.drawable.ic_pause);
            isPlaying = true;
        } else {
            pause(view);
            FAB.setImageResource(R.drawable.ic_play_arrow);
            isPlaying = false;
        }
        FAB.setOnLongClickListener(view1 -> {
            stop(view1);
            FAB.setImageResource(R.drawable.ic_play_arrow);
            //Toast.makeText(DisplayScreen.this, "Audio Reset", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    public class UpdateSeekBar implements Runnable{
        @Override
        public void run() {
            if(mediaPlayer != null) {
                audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 100);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null)mediaPlayer.release();
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