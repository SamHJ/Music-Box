package havotech.com.musicbox;

public class LibraryModel {

    public String image_url, key, name, mKey;
    Long no_of_songs;

    public LibraryModel() {

    }

    public LibraryModel(String image_url, String key, String name, Long no_of_songs,String mKey) {
        this.image_url = image_url;
        this.key = key;
        this.name = name;
        this.no_of_songs = no_of_songs;
        this.mKey = mKey;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNo_of_songs() {
        return no_of_songs;
    }

    public void setNo_of_songs(Long no_of_songs) {
        this.no_of_songs = no_of_songs;
    }
}
