package havotech.com.musicbox;

public class SongModel {
    public String song_name, artiste_name, song_image_url, mKey, price, song_url;

    public SongModel() {
    }


    public SongModel(String song_name, String artiste_name, String song_image_url, String mKey, String price, String song_url) {
        this.song_name = song_name;
        this.artiste_name = artiste_name;
        this.song_image_url = song_image_url;
        this.mKey = mKey;
        this.price = price;
        this.song_url = song_url;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getArtiste_name() {
        return artiste_name;
    }

    public void setArtiste_name(String artiste_name) {
        this.artiste_name = artiste_name;
    }

    public String getSong_image_url() {
        return song_image_url;
    }

    public void setSong_image_url(String song_image_url) {
        this.song_image_url = song_image_url;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSong_url() {
        return song_url;
    }

    public void setSong_url(String song_url) {
        this.song_url = song_url;
    }
}