package uk.ac.abertay.cmp400.java_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DisplayScreen extends AppCompatActivity {

    //firebase auth and store
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //views
    RecyclerView recyclerView;
    FloatingActionButton FAB;
    ActionBar actionBar;
    SeekBar audioSeekBar;

    //values
    double playbackSpeed;
    boolean ShowAudioPlayer;
    boolean isPlaying;
    int audioID;
    int index;

    //references
    MediaPlayer mediaPlayer;
    Handler handler;
    DocumentReference documentReferenceUsers;
    DisplayAdapter displayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_screen);

        //firebase auth and store instances.
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

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

        //Firebase Listeners
        documentReferenceUsers = fStore.collection("users").document(userID);
        //references
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayAdapter = new DisplayAdapter(this, getMyList(index));
        recyclerView.setAdapter(displayAdapter);
        audioSeekBar.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        documentReferenceUsers.addSnapshotListener(this, (value, error) -> {
            try {
                playbackSpeed = value.getDouble("PlaybackSpeed");
                ShowAudioPlayer = value.getBoolean("ShowAudioPlayer");

                if(ShowAudioPlayer){
                    FAB.setVisibility(View.VISIBLE);
                }else{
                    FAB.setVisibility(View.INVISIBLE);
                }
            }catch(Exception e){
                Log.e("DisplayScreen", e.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    private ArrayList<DisplayModel> getMyList(int Index) {
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.preference_file_key), MODE_PRIVATE);
        ArrayList<DisplayModel> models = new ArrayList<>();

        DisplayModel m;

        String title_json;
        String Description_json;

        ArrayList<String> Title;
        ArrayList<String> Description;
        Gson gson = new Gson();
        String tmp_red = getResources().getString(R.string.html_red);

        switch (Index) {
            case 0:
                Toast.makeText(this, "Whoops, Something went wrong", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                //Audio
                audioID = getResources().getIdentifier("the_basics_syntax_of_java_1", "raw", getPackageName());

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
                    m.setDescription(Description.get(i).replace("@keyword", tmp_red));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
            case 2:
                //Audio
                audioID = getResources().getIdentifier("variables", "raw", getPackageName());

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
                    m.setDescription(Description.get(i).replace("@keyword", tmp_red));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;

            case 3:
                //Audio
                audioID = getResources().getIdentifier("data_types", "raw", getPackageName());

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
                    m.setDescription(Description.get(i).replace("@keyword", tmp_red));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
            case 4:
                //Audio
                audioID = getResources().getIdentifier("operators", "raw", getPackageName());

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
                    m.setDescription(Description.get(i).replace("@keyword", tmp_red));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
            case 5:
                //Audio
                audioID = getResources().getIdentifier("conditional_statements", "raw", getPackageName());

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
                    m.setDescription(Description.get(i).replace("@keyword", tmp_red));
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
        }
        return models;
    }

    //FAB Audio functionality
    public void play(View view){
        if(audioID != 0) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, audioID);
                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    stopPlayer();
                    FAB.setImageResource(R.drawable.ic_play_arrow);
                    isPlaying = false;
                });
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
}