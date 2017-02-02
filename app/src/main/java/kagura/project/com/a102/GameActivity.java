package kagura.project.com.a102;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    String goodAnswer;
    Music music;
    Drawable resume;
    Drawable pause;
    MediaPlayer mp;
    Button buttonNext;
    TextView buttonState;
    List<TextView> buttonsAnswer;
    List<TextView> buttonsSelectionArtist;
    List<TextView> buttonsSongs;
    List<ImageView> imageSongs;
    ImageView remoteSings;
    int yearMusic;
    boolean isMusicStarted = false;
    int compteur;
    int random;
    int goodAnswerButtonPosition;
    List<String> artistes;

    Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        yearMusic = getIntent().getIntExtra("year", 0);
        switch(yearMusic){
            case(50):
                setContentView(R.layout.activity_game_50);
                break;
            case(60):
                setContentView(R.layout.activity_game_60);
                break;
            case(70):
                setContentView(R.layout.activity_game_70);
                break;
            case(80):
                setContentView(R.layout.activity_game_80);
                break;
        }

        game = new Game(this, yearMusic);




        resume = getResources().getDrawable(android.R.drawable.ic_media_play);
        pause = getResources().getDrawable(android.R.drawable.ic_media_pause);

        buttonsAnswer = new ArrayList<>();

        buttonsAnswer.add((TextView) findViewById(R.id.buttonAnswer1));
        buttonsAnswer.add((TextView) findViewById(R.id.buttonAnswer2));
        buttonsAnswer.add((TextView) findViewById(R.id.buttonAnswer3));
        buttonsAnswer.add((TextView) findViewById(R.id.buttonAnswer4));

        buttonsSelectionArtist = new ArrayList<>();

        buttonsSelectionArtist.add((TextView) findViewById(R.id.buttonArtist1));
        buttonsSelectionArtist.add((TextView) findViewById(R.id.buttonArtist2));
        buttonsSelectionArtist.add((TextView) findViewById(R.id.buttonArtist3));
        buttonsSelectionArtist.add((TextView) findViewById(R.id.buttonArtist4));
        buttonsSelectionArtist.add((TextView) findViewById(R.id.buttonArtist5));

        buttonsSongs = new ArrayList<>();

        buttonsSongs.add((TextView) findViewById(R.id.buttonChanson1));
        buttonsSongs.add((TextView) findViewById(R.id.buttonChanson2));
        buttonsSongs.add((TextView) findViewById(R.id.buttonChanson3));
        buttonsSongs.add((TextView) findViewById(R.id.buttonChanson4));

        imageSongs = new ArrayList<>();

        /*imageSongs.add((ImageView) findViewById(R.id.imageChanson1));
        imageSongs.add((ImageView) findViewById(R.id.imageChanson2));
        imageSongs.add((ImageView) findViewById(R.id.imageChanson3));
        imageSongs.add((ImageView) findViewById(R.id.imageChanson4)); */


        buttonState = (TextView) findViewById(R.id.buttonState);
        remoteSings = (ImageView) findViewById(R.id.imageRemoteZ);

        mp = new MediaPlayer();

    }

    @Override
    public void onBackPressed() {
        mp.stop();
        super.onBackPressed();
    }

    public void StartMusic(View view) {
        switch (compteur % 2){
            case 1:
                mp.pause();
                buttonState.setText("Jouer");
                break;
            case 0:
                mp.start();
                buttonState.setText("Pause");
                break;
        }
        compteur++;
    }

    public void response(View view) {}

    public void nextQuestion(View view) {
        mp.stop();
        //loadGame(artist);
    }

    private void loadGame(int artist){
    }

    public void switchArtist(View view) {
        int artistPosition = 0;
        if(mp.isPlaying()){
            mp.stop();
            buttonState.setBackground(resume);
            compteur++;
        }
        if(buttonState.getVisibility() == View.VISIBLE){
            buttonState.setVisibility(View.INVISIBLE);
        }
        switch (view.getId()){
            case R.id.buttonArtist1:
                artistPosition = 0;
                break;
            case R.id.buttonArtist2:
                artistPosition = 1;
                break;
            case R.id.buttonArtist3:
                artistPosition = 2;
                break;
            case R.id.buttonArtist4:
                artistPosition = 3;
                break;
            case R.id.buttonArtist5:
                artistPosition = 4;
                break;
        }

        List<String> artistesAnswers = game.initGameSingers(artistPosition);
        for(int i = 0; i < artistesAnswers.size(); i++){
            buttonsAnswer.get(i).setText(artistesAnswers.get(i));
        }

        //loadGame(artistPosition);

    }

    public void switchSong(View view) {
        for (int i =0; i < 4; i++){
            if(view.getId() == buttonsSongs.get(i).getId()){
                if(mp.isPlaying()){
                    mp.stop();
                    buttonState.setBackground(resume);
                    compteur++;
                }

                String currentMusicPath = game.getMusicPath(i);
                mp = MediaPlayer.create(this, getResources().getIdentifier(currentMusicPath, "raw", getPackageName()));

                List<String> singsAnswersList = game.buildListSingsAnswer(i);

                Log.i("testButtonSongIds", Integer.toString(view.getId()) + "//" + Integer.toString(buttonsSongs.get(i).getId()));
                buttonState.setVisibility(View.VISIBLE);

            }
        }
    }

    public void checkArtistAnswer(View view) {
        TextView buttonAnswer = (TextView) view;
        String buttonAnswerText = buttonAnswer.getText().toString();
        String[] answers = game.checkIfArtistFound(buttonAnswerText);

        if(answers[0].equals("good")){
            view.setBackgroundColor(getResources().getColor(R.color.green));
        }else{
            view.setBackgroundColor(getResources().getColor(R.color.red));
             buttonsAnswer.get(Integer.parseInt(answers[1])).setBackgroundColor(getResources().getColor(R.color.green));
        }
        remoteSings.setVisibility(View.VISIBLE);

        game.initGameSings();
    }
}
