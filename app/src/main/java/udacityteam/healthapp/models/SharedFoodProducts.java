package udacityteam.healthapp.models;

import java.util.ArrayList;

/**
 * Created by vvost on 12/29/2017.
 */

public class SharedFoodProducts {
   public String UserId;
   public String DateTime;
    ArrayList<SelectedFood> SelectedFoods;
    public SharedFoodProducts()
    {

    }


    public SharedFoodProducts(String userId, String dateTime, ArrayList<SelectedFood> selectedFoods) {
        this.UserId= userId;
        this.DateTime = dateTime;
        SelectedFoods = selectedFoods;
    }

}

