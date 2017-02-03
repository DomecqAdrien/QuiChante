package kagura.project.com.a102;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game {

    private Context context;
    private Music music;
    private int yearMusic;
    private Random rand = new Random();
    private String artist;
    private int placementGoodAnswerSinger;
    private int placementGoodAnswerSing;
    private int artistPositionInJson;



    public Game(Context context, int yearMusic){
        this.context = context;
        this.yearMusic = yearMusic;
    }

    public List<String> initGameSingers(int artistPosition){
        this.artistPositionInJson = artistPosition;
        artist = null;
        List<String> artists = null;
        try {
            JSONObject object = new JSONObject(loadJSONFromAsset("chanteurs.json"));
            JSONArray arrayYear = object.getJSONArray(Integer.toString(yearMusic));
            artist = arrayYear.get(artistPosition).toString();
            Log.i("artist", artist);
            artists = new ArrayList<>();
            for(int i = 0; i < arrayYear.length(); i++){
                artists.add(arrayYear.get(i).toString());
            }
            Log.i("artisteS", artists.toString());
            artists.remove(artist);
            Log.i("artisteS - ", artists.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return buildListSingersAnswer(artist, artists);
    }

    private List<String> buildListSingersAnswer(String artist, List<String> artists) {
        List<String> listSingersAnswer = new ArrayList<>(Collections.nCopies(4 , ""));
        placementGoodAnswerSinger = rand.nextInt(3);

        for (int i =0; i < 4; i++){
            if(i == placementGoodAnswerSinger){
                listSingersAnswer.set(i, artist);

            }else{
                int random = rand.nextInt(artists.size());
                listSingersAnswer.set(i, artists.get(random));
                artists.remove(random);
            }

            Log.i("listsingersanswer", listSingersAnswer.toString());
        }

        return listSingersAnswer;
    }

    public void initGameSings(){

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset("musiques" + yearMusic + ".json"));
            JSONArray jsonArray = obj.getJSONArray("musiques");
            JSONObject jsonObjArtiste = jsonArray.getJSONObject(artistPositionInJson);
            //artistes = new ArrayList<>();
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
                images.add(context.getResources().getDrawable(context.getResources().getIdentifier(jsonObjChansons.getString("path_image"), "drawable", context.getPackageName())));
            }

            music.setTitres(titres);
            music.setPathMusics(pathMusics);
            music.setImages(images);


        } catch (JSONException e){
            e.printStackTrace();
        }

        //return buildListSingsAnswer(sing, sings);
    }

    public List<String> buildListSingsAnswer(int musicPosition) {

        Music music = this.music;

        List<String> listSingsAnswer = new ArrayList<>(Collections.nCopies(4 , ""));

        placementGoodAnswerSing = rand.nextInt(3);
        listSingsAnswer.set(placementGoodAnswerSing, music.getTitres().get(musicPosition));
        Log.i("size string", Integer.toString(music.getTitres().size()));
        Log.i("randomGood", Integer.toString(placementGoodAnswerSing));
        Log.i("titres", music.getTitres().toString());
        //goodAnswer = music.getTitres().get(placementGoodAnswer);

        List<Integer> listPositions = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            listPositions.add(i);
        }

        for (int i =0; i < 4; i++){

            if(i != placementGoodAnswerSing){
                int randomMusic = rand.nextInt(music.getTitres().size());
                if(!listSingsAnswer.contains(music.getTitres().get(randomMusic))){
                    listSingsAnswer.set(i, music.getTitres().get(randomMusic));
                }
                else{
                    i--;
                }
            }

            //Log.i("random", Integer.toString(random));
            //Log.i("titres", music.getTitres().toString());



            //imageSongs.get(i).setImageDrawable(music.getImages().get(i));
            //buttonsSongs.get(i).setVisibility(View.VISIBLE);

            Log.i("listsingsAnswer", listSingsAnswer.toString());
        }
        return listSingsAnswer;
    }

    private String loadJSONFromAsset(String jsonPath) {
        String json;
        try {
            InputStream is = context.getAssets().open(jsonPath);
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

    public String[] checkIfArtistFound(String artist) {
        String[] answers = new String[3];
        if(this.artist.equals(artist)){
            answers[0] = "good";
        }else{
            answers[0] = "false";
            answers[1] = Integer.toString(placementGoodAnswerSinger);
        }

        return answers;
    }

    public String getMusicPath(int musicPosition) {
        Log.i("pathMusic", music.getPathMusics().get(musicPosition));
        return music.getPathMusics().get(musicPosition);
    }

    public int[] getAnswerSinger(){
        return new int[]{placementGoodAnswerSinger};
    }
}
