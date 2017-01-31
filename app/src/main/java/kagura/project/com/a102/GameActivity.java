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
    List<TextView> buttonAnswer;
    List<TextView> buttonSelectionArtist;
    List<TextView> buttonSongs;
    List<ImageView> imageSongs;
    int yearMusic;
    int compteur;
    int random;
    int goodAnswerButtonPosition;
    List<String> artistes;


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




        resume = getResources().getDrawable(android.R.drawable.ic_media_play);
        pause = getResources().getDrawable(android.R.drawable.ic_media_pause);

        buttonAnswer = new ArrayList<>();

        buttonAnswer.add((TextView) findViewById(R.id.buttonAnswer1));
        buttonAnswer.add((TextView) findViewById(R.id.buttonAnswer2));
        buttonAnswer.add((TextView) findViewById(R.id.buttonAnswer3));
        buttonAnswer.add((TextView) findViewById(R.id.buttonAnswer4));

        buttonSelectionArtist = new ArrayList<>();

        buttonSelectionArtist.add((TextView) findViewById(R.id.buttonArtist1));
        buttonSelectionArtist.add((TextView) findViewById(R.id.buttonArtist2));
        buttonSelectionArtist.add((TextView) findViewById(R.id.buttonArtist3));
        buttonSelectionArtist.add((TextView) findViewById(R.id.buttonArtist4));
        buttonSelectionArtist.add((TextView) findViewById(R.id.buttonArtist5));

        buttonSongs = new ArrayList<>();

        buttonSongs.add((TextView) findViewById(R.id.buttonChanson1));
        buttonSongs.add((TextView) findViewById(R.id.buttonChanson2));
        buttonSongs.add((TextView) findViewById(R.id.buttonChanson3));
        buttonSongs.add((TextView) findViewById(R.id.buttonChanson4));

        imageSongs = new ArrayList<>();

      /*  imageSongs.add((ImageView) findViewById(R.id.imageChanson1));
        imageSongs.add((ImageView) findViewById(R.id.imageChanson2));
        imageSongs.add((ImageView) findViewById(R.id.imageChanson3));
        imageSongs.add((ImageView) findViewById(R.id.imageChanson4)); */


        buttonState = (TextView) findViewById(R.id.buttonState);

        mp = new MediaPlayer();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mp.stop();
        Intent back = new Intent(this, MenuActivity.class);
        finish();
        startActivity(back);

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

    public void response(View view) {


    }

    public void nextQuestion(View view) {
        mp.stop();
        //loadGame(artist);
    }

    private void loadGame(int artist){

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset("musiques" + yearMusic + ".json"));
            JSONArray jsonArray = obj.getJSONArray("musiques");
            JSONObject jsonObjArtiste = jsonArray.getJSONObject(artist);
            artistes = new ArrayList<>();
            music = new Music();

            music.setAuteur(jsonObjArtiste.getString("artiste"));
            JSONArray jsonArrayChansons = jsonObjArtiste.getJSONArray("chansons");


            List<String> titres = new ArrayList<>();
            List<String> pathMusics = new ArrayList<>();
            List<Drawable> images = new ArrayList<>();

            for(int i = 0; i < jsonArrayChansons.length(); i++){
                JSONObject jsonObjChansons = jsonArrayChansons.getJSONObject(i);
                titres.add(jsonObjChansons.getString("titre"));
                pathMusics.add(jsonObjChansons.getString("path_music"));
                images.add(getResources().getDrawable(getResources().getIdentifier(jsonObjChansons.getString("path_image"), "drawable", getPackageName())));
            }

            music.setTitres(titres);
            music.setPathMusics(pathMusics);
            music.setImages(images);


        } catch (JSONException e){
            e.printStackTrace();
        }

        Random r = new Random();
        int placementGoodAnswer = r.nextInt(3);
        Log.i("size string", Integer.toString(music.getTitres().size()));
        Log.i("randomGood", Integer.toString(placementGoodAnswer));
        Log.i("titres", music.getTitres().toString());
        goodAnswer = music.getTitres().get(placementGoodAnswer);

        for (int i =0; i < 4; i++){
            if(i == placementGoodAnswer){
                buttonAnswer.get(i).setText(goodAnswer);
            }else{
                random = r.nextInt(music.getTitres().size());
                buttonAnswer.get(i).setText(music.getTitres().get(random));
                music.getTitres().remove(random);
            }

            Log.i("random", Integer.toString(random));
            Log.i("titres", music.getTitres().toString());



            imageSongs.get(i).setImageDrawable(music.getImages().get(i));
            buttonSongs.get(i).setVisibility(View.VISIBLE);
        }




    }

    public String loadJSONFromAsset(String jsonPath) {
        String json;
        try {
            InputStream is = getAssets().open(jsonPath);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.i("json", json);
        return json;
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

        loadGame(artistPosition);

    }

    public void switchSong(View view) {
        for (int i =0; i < 4; i++){
            if(view.getId() == buttonSongs.get(i).getId()){
                if(mp.isPlaying()){
                    mp.stop();
                    buttonState.setBackground(resume);
                    compteur++;
                }
                mp = MediaPlayer.create(this, getResources().getIdentifier(music.getPathMusics().get(i), "raw", getPackageName()));

                Log.i("testButtonSongIds", Integer.toString(view.getId()) + "//" + Integer.toString(buttonSongs.get(i).getId()));
                buttonState.setVisibility(View.VISIBLE);

            }
        }
    }
}
