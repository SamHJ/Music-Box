package havotech.com.musicbox;

public class VideoModel {

    public String mKey, video_date, video_image_url, video_title, video_url, video_author,price;

    public VideoModel() {
    }

    public VideoModel(String mKey, String video_date, String video_image_url, String video_title, String video_url,String video_author,
                      String price) {
        this.mKey = mKey;
        this.video_date = video_date;
        this.video_image_url = video_image_url;
        this.video_title = video_title;
        this.video_url = video_url;
        this.video_author = video_author;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVideo_author() {
        return video_author;
    }

    public void setVideo_author(String video_author) {
        this.video_author = video_author;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getVideo_date() {
        return video_date;
    }

    public void setVideo_date(String video_date) {
        this.video_date = video_date;
    }

    public String getVideo_image_url() {
        return video_image_url;
    }

    public void setVideo_image_url(String video_image_url) {
        this.video_image_url = video_image_url;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
