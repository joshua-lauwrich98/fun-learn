package anchovy.net.funlearn.other;

/**
 * Created by DarKnight98 on 7/23/2017.
 */

public class ClassMember {
    private String fullname, photo, uid, username;

    public ClassMember() {
        //required empty constructor
    }

    public ClassMember(String fullname, String photo, String uid, String username) {
        this.fullname = fullname;
        this.photo = photo;
        this.uid = uid;
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
