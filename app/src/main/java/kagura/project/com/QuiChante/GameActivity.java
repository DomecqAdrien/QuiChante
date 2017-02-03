package kagura.project.com.QuiChante;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {


    String resume, pause;
    MediaPlayer mp;
    TextView buttonState;
    List<TextView> buttonsAnswer, buttonsSelectionArtist, buttonsSongs;
    ImageView remoteSings, imageTv;
    int yearMusic;
    boolean isSongGameStarted = false;

    Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //noinspection ConstantConditions
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

        resume = getString(R.string.resume);
        pause = getString(R.string.pause);


        buttonsAnswer = new ArrayList<>();
        buttonsSongs = new ArrayList<>();

        for(int i = 1; i < 5; i++){
            buttonsAnswer.add((TextView) findViewById(getResources().getIdentifier("buttonAnswer" + i, "id", getPackageName())));
            buttonsSongs.add((TextView) findViewById(getResources().getIdentifier("buttonChanson" + i, "id", getPackageName())));
        }

        buttonsSelectionArtist = new ArrayList<>();

        for(int i = 1; i < 6; i++){
            buttonsSelectionArtist.add((TextView) findViewById(getResources().getIdentifier("buttonArtist" + i, "id", getPackageName())));
        }

        buttonState = (TextView) findViewById(R.id.buttonState);
        remoteSings = (ImageView) findViewById(R.id.imageRemoteZ);
        imageTv = (ImageView) findViewById(R.id.imageLayoutTv);

        hideSongsRemote();
        hideMusicStateButton();

        mp = new MediaPlayer();

    }

    @Override
    public void onBackPressed() {
        if(mp.isPlaying()){
            mp.stop();
        }
        super.onBackPressed();
    }

    public void setMusicState(View view) {
        if(mp.isPlaying()){
            pauseMusic();
        }else{
            startMusic();
        }
    }

    public void response(View view) {}



    public void switchArtist(View view) {
        isSongGameStarted = false;
        clearTv();
        clearButtons();
        pauseMusic();

        if(buttonState.getVisibility() == View.VISIBLE){
            hideMusicStateButton();
        }

        int artistPosition = getArtistPosition(view.getId());
        List<String> artistesAnswers = game.initGameSingers(artistPosition);
        for(int i = 0; i < artistesAnswers.size(); i++){
            buttonsAnswer.get(i).setText(artistesAnswers.get(i));
        }
    }



    public void switchSong(View view) {
        isSongGameStarted = true;
        clearButtons();
        showMusicStateButton();

        for (int i =0; i < 4; i++){
            if(view.getId() == buttonsSongs.get(i).getId()){
                pauseMusic();

                String currentMusicPath = game.getMusicPath(i);
                Log.i("currentMusicPath", currentMusicPath);
                mp = MediaPlayer.create(this, getResources().getIdentifier(currentMusicPath, "raw", getPackageName()));

                List<List> singsAnswersList = game.buildListSingsAnswer(i);

                for(int j = 0; j < singsAnswersList.get(0).size(); j++){
                    buttonsAnswer.get(j).setText((String) singsAnswersList.get(0).get(j));
                }

                Drawable ekelele = game.getImage(i);
                imageTv.setImageDrawable(ekelele);

                Log.i("testButtonSongIds", Integer.toString(view.getId()) + "//" + Integer.toString(buttonsSongs.get(i).getId()));
                //buttonState.setVisibility(View.VISIBLE);
            }
        }
    }

    public void checkAnswer(View view) {
        TextView buttonAnswer = (TextView) view;
        String buttonAnswerText = buttonAnswer.getText().toString();
        String[] answers;
        if(!isSongGameStarted){
            answers = checkArtistAnswers(buttonAnswerText);
        }else{
            answers = checkSongAnswers(buttonAnswerText);
        }

        if(answers[0].equals("good")){
            view.setBackgroundColor(getResources().getColor(R.color.green));
        }else{
            view.setBackgroundColor(getResources().getColor(R.color.red));
             buttonsAnswer.get(Integer.parseInt(answers[1])).setBackgroundColor(getResources().getColor(R.color.green));
        }
    }

    private String[] checkSongAnswers(String buttonAnswerText) {
        return game.checkIfSongFound(buttonAnswerText);
    }

    private String[] checkArtistAnswers(String buttonAnswerText) {
        showSongsRemote();
        game.initGameSings();
        return game.checkIfArtistFound(buttonAnswerText);
    }

    public int getArtistPosition(int idButtonArtist){
        int artistPositionInJson;
        switch (idButtonArtist){
            case R.id.buttonArtist1:
                artistPositionInJson = 0;
                break;
            case R.id.buttonArtist2:
                artistPositionInJson = 1;
                break;
            case R.id.buttonArtist3:
                artistPositionInJson = 2;
                break;
            case R.id.buttonArtist4:
                artistPositionInJson = 3;
                break;
            case R.id.buttonArtist5:
                artistPositionInJson = 4;
                break;
            default:
                artistPositionInJson = 0;
                break;
        }
        return artistPositionInJson;
    }

    public void clearButtons(){
        for(int i = 0; i < 4; i++){
            buttonsAnswer.get(i).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    public void clearTv(){
        imageTv.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("television_19" + yearMusic, "drawable", getPackageName())));
    }

    public void hideSongsRemote(){
        remoteSings.setVisibility(View.INVISIBLE);
        for(int i =0; i < buttonsSongs.size(); i++){
            buttonsSongs.get(i).setVisibility(View.INVISIBLE);
        }
    }

    public void showSongsRemote(){
        remoteSings.setVisibility(View.VISIBLE);
        for(int i =0; i < buttonsSongs.size(); i++){
            buttonsSongs.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void hideMusicStateButton(){
        buttonState.setVisibility(View.INVISIBLE);
    }

    public void showMusicStateButton(){
        buttonState.setVisibility(View.VISIBLE);
    }

    public void startMusic(){
        buttonState.setText(pause);
        mp.start();
    }

    public void pauseMusic(){
        if(mp.isPlaying()){
            buttonState.setText(resume);
            mp.pause();
        }

    }
}
