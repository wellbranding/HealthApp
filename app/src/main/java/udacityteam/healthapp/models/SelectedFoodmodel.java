package udacityteam.healthapp.models;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by kunda on 10/4/2017.
 */

public class SelectedFoodmodel {
    private String foodid;
    private String foodName;
    private String UserId;
    private String Date;

    public SelectedFoodmodel(String foodid, String foodName, String UserId, String date) {
        this.foodid = foodid;
        this.foodName = foodName;
        this.UserId = UserId;
        this.Date = date;
    }
    public  SelectedFoodmodel()
    {

    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
