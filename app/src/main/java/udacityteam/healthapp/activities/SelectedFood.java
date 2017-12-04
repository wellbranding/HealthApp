package udacityteam.healthapp.activities;

/**
 * Created by kunda on 10/4/2017.
 */

public class SelectedFood {
    private String foodid;
    private String foodName;

    public SelectedFood(String foodid, String foodName) {
        this.foodid = foodid;
        this.foodName = foodName;
    }
    public SelectedFood()
    {

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
