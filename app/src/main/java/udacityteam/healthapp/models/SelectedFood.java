package udacityteam.healthapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kunda on 10/4/2017.
 */

public class SelectedFood implements Parcelable{
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

    protected SelectedFood(Parcel in) {
        foodid = in.readString();
        foodName = in.readString();
        UserId = in.readString();
        Date = in.readString();
        Calories = in.readFloat();
        Protein = in.readFloat();
        Fat = in.readFloat();
        Carbohydrates = in.readFloat();
    }

    public static final Creator<SelectedFood> CREATOR = new Creator<SelectedFood>() {
        @Override
        public SelectedFood createFromParcel(Parcel in) {
            return new SelectedFood(in);
        }

        @Override
        public SelectedFood[] newArray(int size) {
            return new SelectedFood[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodid);
        parcel.writeString(foodName);
        parcel.writeString(UserId);
        parcel.writeString(Date);
        parcel.writeFloat(Calories);
        parcel.writeFloat(Protein);
        parcel.writeFloat(Fat);
        parcel.writeFloat(Carbohydrates);
    }
}
