package kagura.project.com.QuiChante;


import android.graphics.drawable.Drawable;

import java.util.List;

class Music {
    private List<String> titres;
    private List<String> pathMusics;
    private List<Drawable> images;

    Music(){}

    List<String> getPathMusics() {
        return pathMusics;
    }

    void setPathMusics(List<String> pathMusics) {
        this.pathMusics = pathMusics;
    }

    List<Drawable> getImages() {
        return images;
    }

    void setImages(List<Drawable> images) {
        this.images = images;
    }

    List<String> getTitres() {
        return titres;
    }

    void setTitres(List<String> titres) {
        this.titres = titres;
    }
}
