package udacityteam.healthapp.models;

import java.util.ArrayList;

/**
 * Created by vvost on 12/29/2017.
 */

    public class SharedFoodProductsold {
     private String UserId;
    private String DateTime;
   private ArrayList<SelectedFood> SelectedFoods;
    private float Calories;
    private float Carbohydrates;
    private float Protein;
    private float Fats;
    public SharedFoodProductsold()
    {

    }

    public SharedFoodProductsold(String userId, String dateTime, ArrayList<SelectedFood> selectedFoods, float calories,
                                 float carbohydrates, float protein, float fats) {
        this.UserId= userId;
        this.DateTime = dateTime;
        SelectedFoods = selectedFoods;
        Calories = calories;
        Carbohydrates = carbohydrates;
        Protein = protein;
        Fats = fats;
    }

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
}

