package udacityteam.healthapp.PHP_Retrofit;

import java.util.ArrayList;

/**
 * Created by Belal on 14/04/17.
 */

public class Usersretrofit {

    private ArrayList<Userretrofit> selectedfoods;

    public Usersretrofit() {

    }

    public ArrayList<Userretrofit> getSelectedfoods() {
        return selectedfoods;
    }

    public void setSelectedfoods(ArrayList<Userretrofit> selectedfoods) {
        this.selectedfoods = selectedfoods;
    }
}
