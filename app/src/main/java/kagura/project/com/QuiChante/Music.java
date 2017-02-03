package kagura.project.com.QuiChante;


import android.graphics.drawable.Drawable;

import java.util.List;

public class Music {
    private String auteur;
    private List<String> titres;
    private List<String> pathMusics;
    private List<Drawable> images;

    public Music(){}

    public List<String> getPathMusics() {
        return pathMusics;
    }

    public void setPathMusics(List<String> pathMusics) {
        this.pathMusics = pathMusics;
    }

    public List<Drawable> getImages() {
        return images;
    }

    public void setImages(List<Drawable> images) {
        this.images = images;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public List<String> getTitres() {
        return titres;
    }

    public void setTitres(List<String> titres) {
        this.titres = titres;
    }
}
