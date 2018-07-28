package udacityteam.healthapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class _0 {

    @SerializedName("displayname")
    @Expose
    private String displayname;
    @SerializedName("mail")
    @Expose
    private String mail;

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
