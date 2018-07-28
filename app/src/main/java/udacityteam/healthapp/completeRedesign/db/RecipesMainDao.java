package udacityteam.healthapp.completeRedesign.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.UserRetrofitGood;
import udacityteam.healthapp.Model.Userretrofit;


@Dao
public interface RecipesMainDao {
    @Query("SELECT * FROM OneSharedFoodProductsListRetrofit")
    public abstract LiveData<List<OneSharedFoodProductsListRetrofit>> getRecipes();


   @Insert
    public void insertCurrentUser(UserRetrofitGood userretrofit);

  //  @Query("SELECT * FROM ")
//
//    @Transaction
//    @Query("SELECT * FROM Recipe WHERE id = :id")
//    public abstract Recipe getRecipe(int id);
//
//    @Insert
//    public abstract void insertAllRecipes(List<Recipe> recipeList);
    @Insert
    public void insertAllOneSharedFoodProductsListRetrofit(List<OneSharedFoodProductsListRetrofit> oneSharedFoodProductsListRetrofits);
    @Insert
    public abstract void insertOneSharedFoodProductsListRetrofit(OneSharedFoodProductsListRetrofit oneSharedFoodProductsListRetrofit);
}



