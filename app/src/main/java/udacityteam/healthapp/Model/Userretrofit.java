package udacityteam.healthapp.Model;

/**
 * Created by Belal on 14/04/17.
 */

public class Userretrofit {

    private int id;
    private String name;
    private String email;
    private String password;
    private String uid;
    private String gender;

    public String getUid() {
        return uid;
    }

    public Userretrofit(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public Userretrofit(String name, String email, String password, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public Userretrofit(int id, String name, String email, String gender){
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
    }

    public Userretrofit(int id, String name, String email, String password, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getGender() {
        return gender;
    }
}
