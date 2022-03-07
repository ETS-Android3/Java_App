package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;

public class DisplayScreen extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    RecyclerView recyclerView;
    DisplayAdapter displayAdapter;
    MediaPlayer mediaPlayer;
    FloatingActionButton FAB;
    Boolean isPlaying;
    int audioID;
    ActionBar actionBar;
    double playbackSpeed;
    boolean ShowAudioPlayer;
    SeekBar audioSeekBar;
    Handler handler;
    DocumentReference documentReferenceUsers;
    DocumentReference documentReferenceTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_screen);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        handler = new Handler();

        audioSeekBar = findViewById(R.id.AudioSeekBar);

        playbackSpeed = 1.00;
        ShowAudioPlayer = false;
        isPlaying = false;

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        documentReferenceUsers = fStore.collection("users").document(userID);
        documentReferenceUsers.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    playbackSpeed = value.getDouble("PlaybackSpeed");
                    ShowAudioPlayer = value.getBoolean("ShowAudioPlayer");

                    if(ShowAudioPlayer){
                        FAB.setVisibility(View.INVISIBLE);
                    }else{
                        FAB.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){}
            }
        });

        int index = getIntent().getIntExtra("index", 0);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayAdapter = new DisplayAdapter(this, getMyList(index));
        recyclerView.setAdapter(displayAdapter);
        FAB = findViewById(R.id.AudioFAB);
        audioSeekBar.setVisibility(View.INVISIBLE);
    }

    private ArrayList<DisplayModel> getMyList(int Index) {
        ArrayList<DisplayModel> models = new ArrayList<>();

        DisplayModel m = new DisplayModel();

        String[] title;
        CharSequence[] description;
        String[] images;

        switch (Index) {
            case 0:
                Toast.makeText(this, "Whoops, Something went wrong", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                //Audio
                audioID = getResources().getIdentifier("the_basics_syntax_of_java_1", "raw", getPackageName());
                //Basics Of Java
                title = getResources().getStringArray(R.array.basics_of_java_title);
                description = getResources().getTextArray(R.array.basics_of_java_description);

                //images = getResources().getStringArray(R.array.basics_of_java_images);

                //Action Bar Title
                actionBar.setTitle(title[0]);

                for (int i = 0; i < title.length; i++) {
                    m = new DisplayModel();
                    m.setTitle(title[i]);
                    m.setDescription(description[i]);
                    m.setHasImage(false);
                    models.add(m);
                }
                break;
            case 2:
                //Audio
                audioID = getResources().getIdentifier("variables", "raw", getPackageName());
                //Variables
                title = getResources().getStringArray(R.array.variables_title);
                description = getResources().getTextArray(R.array.variables_description);
                //images = getResources().getStringArray(R.array.variables_images);

                //Action Bar Title
                actionBar.setTitle(title[0]);

                for (int i = 0; i < title.length; i++) {
                    m = new DisplayModel();
                    m.setTitle(title[i]);
                    m.setDescription(description[i]);
                    m.setHasImage(false);
                    models.add(m);
                }
                break;

            case 3:
                //Audio
                audioID = getResources().getIdentifier("data_types", "raw", getPackageName());
                //Data Types
                title = getResources().getStringArray(R.array.data_types_title);
                description = getResources().getTextArray(R.array.data_types_description);
                images = getResources().getStringArray(R.array.data_types_images);

                //Action Bar Title
                actionBar.setTitle(title[0]);

                for (int i = 0; i < title.length; i++) {
                    if(images[i].equals("")){
                        m = new DisplayModel();
                        m.setTitle(title[i]);
                        m.setDescription(description[i]);
                        m.setHasImage(false);
                        models.add(m);
                    }else{
                        m = new DisplayModel();
                        m.setTitle(title[i]);
                        m.setDescription(description[i]);
                        m.setImg(getResources().getIdentifier(images[i], "drawable", getPackageName()));
                        m.setHasImage(true);
                        models.add(m);
                    }
                }
                break;
            case 4:
                //Audio
                audioID = getResources().getIdentifier("operators", "raw", getPackageName());
                //Operators
                title = getResources().getStringArray(R.array.operators_title);
                description = getResources().getTextArray(R.array.operators_description);
                images = getResources().getStringArray(R.array.operators_images);

                //Action Bar Title
                actionBar.setTitle(title[0]);

                for (int i = 0; i < title.length; i++) {
                    if(images[i].equals("")){
                        m = new DisplayModel();
                        m.setTitle(title[i]);
                        m.setDescription(description[i]);
                        m.setHasImage(false);
                        models.add(m);
                    }else {
                        m = new DisplayModel();
                        m.setTitle(title[i]);
                        m.setDescription(description[i]);
                        m.setImg(getResources().getIdentifier(images[i], "drawable", getPackageName()));
                        m.setHasImage(true);
                        models.add(m);
                    }
                }
                break;
            case 5:
                //Audio
                audioID = getResources().getIdentifier("conditional_statements", "raw", getPackageName());
                //Conditional Statements
                title = getResources().getStringArray(R.array.conditional_title);
                description = getResources().getTextArray(R.array.conditional_description);
                images = getResources().getStringArray(R.array.conditional_images);

                //Action Bar Title
                actionBar.setTitle(title[0]);

                for (int i = 0; i < description.length; i++) {
                    if(images[i].equals("")){
                        m = new DisplayModel();
                        m.setTitle(title[i]);
                        m.setDescription(description[i]);
                        m.setHasImage(false);
                        models.add(m);
                    }else {
                        m = new DisplayModel();
                        m.setTitle(title[i]);
                        m.setDescription(description[i]);
                        m.setImg(getResources().getIdentifier(images[i], "drawable", getPackageName()));
                        m.setHasImage(true);
                        models.add(m);
                    }
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
                mediaPlayer.setOnCompletionListener(    new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stopPlayer();
                        FAB.setImageResource(R.drawable.ic_play_arrow);
                        isPlaying = false;
                    }
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
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            audioSeekBar.setProgress(0);
            audioSeekBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    public void ToggleButton(View view) {
        if (!isPlaying) {
            play(view);
            FAB.setImageResource(R.drawable.ic_pause);
            isPlaying = true;
        } else {
            pause(view);
            FAB.setImageResource(R.drawable.ic_play_arrow);
            isPlaying = false;
        }
        FAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                stop(view);
                FAB.setImageResource(R.drawable.ic_play_arrow);
                //Toast.makeText(DisplayScreen.this, "Audio Reset", Toast.LENGTH_SHORT).show();
                return true;
            }
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