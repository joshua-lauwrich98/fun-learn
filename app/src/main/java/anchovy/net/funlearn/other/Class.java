package anchovy.net.funlearn.other;

/**
 * Created by DarKnight98 on 7/18/2017.
 */

public class Class {
    private String uid, name;

    public Class() {
        //required empty constructor
    }

    public Class(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
