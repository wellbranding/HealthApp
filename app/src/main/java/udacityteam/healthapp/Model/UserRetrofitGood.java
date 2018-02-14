package udacityteam.healthapp.Model;

/**
 * Created by vvost on 1/27/2018.
 */

public class UserRetrofitGood {

    private  Integer id;
    private String mail;
    private String uid;
    private String displayname;



        public UserRetrofitGood(int id, String mail, String uid, String displayname) {
        this.id = id;
        this.mail = mail;
        this.uid = uid;
        this.displayname = displayname;
    }
    public UserRetrofitGood() {
    }

    public  Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    @Override
    public String toString() {
        return id + "  " + mail + " " +uid + " " + displayname;
    }
}
