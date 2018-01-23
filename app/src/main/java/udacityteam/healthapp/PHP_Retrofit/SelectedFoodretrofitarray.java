package udacityteam.healthapp.PHP_Retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by kunda on 10/4/2017.
 */

public class SelectedFoodretrofitarray{
    private ArrayList<SelectedFoodretrofit> users;

    public SelectedFoodretrofitarray() {

    }

    public ArrayList<SelectedFoodretrofit> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<SelectedFoodretrofit> users) {
        this.users = users;
    }
}
