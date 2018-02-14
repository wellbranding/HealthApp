package udacityteam.healthapp.Model;

import java.util.ArrayList;

import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;

/**
 * Created by vvost on 12/29/2017.
 */

    public class SharedFoodProductsRetrofit  {
   private ArrayList<OneSharedFoodProductsListRetrofit>  selectedFoodretrofits;
    public SharedFoodProductsRetrofit()
    {

    }

    public SharedFoodProductsRetrofit(ArrayList<OneSharedFoodProductsListRetrofit> selectedFoodretrofits) {
        this.selectedFoodretrofits = selectedFoodretrofits;
    }

    public ArrayList<OneSharedFoodProductsListRetrofit> getSelectedFoodretrofits() {
        return selectedFoodretrofits;
    }

    public void setSelectedFoodretrofits(ArrayList<OneSharedFoodProductsListRetrofit> selectedFoodretrofits) {
        this.selectedFoodretrofits = selectedFoodretrofits;
    }
}

