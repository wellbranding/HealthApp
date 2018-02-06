package udacityteam.healthapp.Model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by vvost on 12/29/2017.
 */

    public class OneSharedFoodProductsListRetrofit {
     private String UserId;
     private String Date;
    private Integer ParentSharedFoodsId;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;


    public float getCalories() {
        return Calories;
    }

    public void setCalories(float calories) {
        Calories = calories;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Integer getParentSharedFoodsId() {
        return ParentSharedFoodsId;
    }

    public void setParentSharedFoodsId(Integer parentSharedFoodsId) {
        ParentSharedFoodsId = parentSharedFoodsId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }



}

