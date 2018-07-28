package udacityteam.healthapp.Model;

public class UserProfile {

    private String mail;
    private String displayname;

    public UserProfile(String mail, String displayname) {
        this.mail = mail;
        this.displayname = displayname;
    }

    public UserProfile() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
}
