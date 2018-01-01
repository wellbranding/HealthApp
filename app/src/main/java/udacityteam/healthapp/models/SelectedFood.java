package udacityteam.healthapp.models;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kunda on 10/4/2017.
 */

public class SelectedFood {
    private String foodid;
    private String foodName;
    private String UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String Date;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;

    public SelectedFood(String foodid, String foodName, String userId, String date, float calories, float protein,
                        float fat, float carbohydrates) {
        this.foodid = foodid;
        this.foodName = foodName;
        UserId = userId;
        Date = date;
        Calories = calories;
        Protein = protein;
        Fat = fat;
        Carbohydrates = carbohydrates;
    }

    public SelectedFood(String foodid, String foodName, String date) {
        this.foodid = foodid;
        this.foodName = foodName;
        this.Date = date;
    }
    public SelectedFood(String foodid, String foodName, String UserId, String date) {
        this.foodid = foodid;
        this.foodName = foodName;
        this.UserId = UserId;
        this.Date = date;
    }

    public SelectedFood()
    {

    }

    public float getProtein() {
        return Protein;
    }

    public void setProtein(float protein) {
        Protein = protein;
    }

    public float getFat() {
        return Fat;
    }

    public void setFat(float fat) {
        Fat = fat;
    }

    public float getCarbohydrates() {
        return Carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        Carbohydrates = carbohydrates;
    }

    public float getCalories() {
        return Calories;
    }

    public void setCalories(float calories) {
        Calories = calories;
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
