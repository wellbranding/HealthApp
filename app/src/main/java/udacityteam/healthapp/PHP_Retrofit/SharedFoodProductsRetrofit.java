package udacityteam.healthapp.PHP_Retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import udacityteam.healthapp.models.SelectedFood;

/**
 * Created by vvost on 12/29/2017.
 */

    public class SharedFoodProductsRetrofit  {
   private ArrayList<OneSharedFoodProductsListRetrofit>  selectedFoodretrofits = new ArrayList<>();
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

