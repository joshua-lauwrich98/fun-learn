package anchovy.net.funlearn.other;

/**
 * Created by DarKnight98 on 7/21/2017.
 */

public class Post {
    private String photo, title, desc, fullname, path;

    public Post() {
        //required empty constructor
    }

    public Post(String photo, String title, String desc, String fullname, String path) {
        this.photo = photo;
        this.title = title;
        this.desc = desc;
        this.fullname = fullname;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
