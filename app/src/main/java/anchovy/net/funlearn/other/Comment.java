package anchovy.net.funlearn.other;

/**
 * Created by DarKnight98 on 7/22/2017.
 */

public class Comment {

    private String photo, time, date, content, fullname;

    public Comment() {
        //required empty constructor
    }

    public Comment(String photo, String time, String date, String content, String fullname) {
        this.photo = photo;
        this.time = time;
        this.date = date;
        this.content = content;
        this.fullname = fullname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
