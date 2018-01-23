package udacityteam.healthapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vvost on 12/29/2017.
 */

    public class SharedFoodProducts implements Parcelable {
     private String UserId;
    private String DateTime;
   private ArrayList<SelectedFood> SelectedFoods = new ArrayList<>();
    private float Calories;
    private float Carbohydrates;
    private float Protein;
    private float Fats;
    public SharedFoodProducts()
    {

    }

    public SharedFoodProducts(String userId, String dateTime, ArrayList<SelectedFood> selectedFoods, float calories,
                              float carbohydrates, float protein, float fats) {
        this.UserId= userId;
        this.DateTime = dateTime;
        SelectedFoods = selectedFoods;
        Calories = calories;
        Carbohydrates = carbohydrates;
        Protein = protein;
        Fats = fats;
    }

    public SharedFoodProducts(Parcel in) {
        this();
        UserId = in.readString();
        DateTime = in.readString();
        Calories = in.readFloat();
        Carbohydrates = in.readFloat();
        Protein = in.readFloat();
        Fats = in.readFloat();
        in.readTypedList(SelectedFoods, SelectedFood.CREATOR);
    }

    public static final Creator<SharedFoodProducts> CREATOR = new Creator<SharedFoodProducts>() {
        @Override
        public SharedFoodProducts createFromParcel(Parcel in) {
            return new SharedFoodProducts(in);
        }

        @Override
        public SharedFoodProducts[] newArray(int size) {
            return new SharedFoodProducts[size];
        }
    };

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public ArrayList<SelectedFood> getSelectedFoods() {
        return SelectedFoods;
    }

    public void setSelectedFoods(ArrayList<SelectedFood> selectedFoods) {
        SelectedFoods = selectedFoods;
    }

    public float getCalories() {
        return Calories;
    }

    public void setCalories(float calories) {
        Calories = calories;
    }

    public float getCarbohydrates() {
        return Carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        Carbohydrates = carbohydrates;
    }

    public float getProtein() {
        return Protein;
    }

    public void setProtein(float protein) {
        Protein = protein;
    }

    public float getFats() {
        return Fats;
    }

    public void setFats(float fats) {
        Fats = fats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      //  parcel.writeList();
        parcel.writeTypedList(SelectedFoods);
        parcel.writeString(UserId);
        parcel.writeString(DateTime);
        parcel.writeFloat(Calories);
        parcel.writeFloat(Carbohydrates);
        parcel.writeFloat(Protein);
        parcel.writeFloat(Fats);
    }
}

