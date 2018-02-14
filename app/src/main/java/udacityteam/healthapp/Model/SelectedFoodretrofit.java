package udacityteam.healthapp.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.health.TimerStat;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * Created by kunda on 10/4/2017.
 */

public class SelectedFoodretrofit implements  Parcelable {
    private String foodId;
    private String foodName;
    private String UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String SendDate;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;



    public SelectedFoodretrofit()
    {

    }

    protected SelectedFoodretrofit(Parcel in) {
        foodId = in.readString();
        foodName = in.readString();
        UserId = in.readString();
        SendDate = in.readString();
        Calories = in.readFloat();
        Protein = in.readFloat();
        Fat = in.readFloat();
        Carbohydrates = in.readFloat();
    }

    public static final Creator<SelectedFoodretrofit> CREATOR = new Creator<SelectedFoodretrofit>() {
        @Override
        public SelectedFoodretrofit createFromParcel(Parcel in) {
            return new SelectedFoodretrofit(in);
        }

        @Override
        public SelectedFoodretrofit[] newArray(int size) {
            return new SelectedFoodretrofit[size];
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
        return SendDate;
    }

    public void setDate(String date) {
        SendDate = date;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFoodid() {
        return foodId;
    }

    public void setFoodid(String foodid) {
        this.foodId = foodid;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodId);
        dest.writeString(foodName);
        dest.writeString(UserId);
        dest.writeString(SendDate);
        dest.writeFloat(Calories);
        dest.writeFloat(Protein);
        dest.writeFloat(Fat);
        dest.writeFloat(Carbohydrates);
    }
}
