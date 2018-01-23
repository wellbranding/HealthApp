package udacityteam.healthapp.PHP_Retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by kunda on 10/4/2017.
 */

public class SelectedFoodretrofit implements Parcelable{
    private String foodId;
    private String foodName;
    private String UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String Date;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;

    public SelectedFoodretrofit(String foodid, String foodName, String userId, String date, float calories, float protein,
                                float fat, float carbohydrates) {
        this.foodId = foodid;
        this.foodName = foodName;
        UserId = userId;
        Date = date;
        Calories = calories;
        Protein = protein;
        Fat = fat;
        Carbohydrates = carbohydrates;
    }
    public SelectedFoodretrofit(String foodid, String userId, String date, float calories, float protein,
                                float fat, float carbohydrates) {
        this.foodId = foodid;
        UserId = userId;
        Date = date;
        Calories = calories;
        Protein = protein;
        Fat = fat;
        Carbohydrates = carbohydrates;
    }

    public SelectedFoodretrofit(String foodid, String foodName, String date) {
        this.foodId = foodid;
        this.foodName = foodName;
        this.Date = date;
    }
    public SelectedFoodretrofit(String foodid, String foodName, String UserId, String date) {
        this.foodId = foodid;
        this.foodName = foodName;
        this.UserId = UserId;
        this.Date = date;
    }

    public SelectedFoodretrofit()
    {

    }

    protected SelectedFoodretrofit(Parcel in) {
        foodId = in.readString();
        foodName = in.readString();
        UserId = in.readString();
        Date = in.readString();
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodId);
        parcel.writeString(foodName);
        parcel.writeString(UserId);
        parcel.writeString(Date);
        parcel.writeFloat(Calories);
        parcel.writeFloat(Protein);
        parcel.writeFloat(Fat);
        parcel.writeFloat(Carbohydrates);
    }
}
