package havotech.com.musicbox;

public class AdvertModel {
    public String advert_image_url, advert_url, date_and_time, title,mKey;

    public AdvertModel() {
    }

    public AdvertModel(String advert_image_url, String advert_url, String date_and_time, String title,String mKey) {
        this.advert_image_url = advert_image_url;
        this.advert_url = advert_url;
        this.date_and_time = date_and_time;
        this.title = title;
        this.mKey = mKey;
    }


    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getAdvert_image_url() {
        return advert_image_url;
    }

    public void setAdvert_image_url(String advert_image_url) {
        this.advert_image_url = advert_image_url;
    }

    public String getAdvert_url() {
        return advert_url;
    }

    public void setAdvert_url(String advert_url) {
        this.advert_url = advert_url;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}