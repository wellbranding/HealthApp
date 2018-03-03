package udacityteam.healthapp.Model;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vvost on 12/29/2017.
 */

    public class SharedFoodProductsRetrofit  {
   private List<OneSharedFoodProductsListRetrofit>  selectedFoodretrofits;
    public SharedFoodProductsRetrofit()
    {

    }

    public SharedFoodProductsRetrofit(List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits) {
        this.selectedFoodretrofits = selectedFoodretrofits;
    }

    public List<OneSharedFoodProductsListRetrofit> getSelectedFoodretrofits() {
        return selectedFoodretrofits;
    }

    public void setSelectedFoodretrofits(List<OneSharedFoodProductsListRetrofit> selectedFoodretrofits) {
        this.selectedFoodretrofits = selectedFoodretrofits;
    }
}

