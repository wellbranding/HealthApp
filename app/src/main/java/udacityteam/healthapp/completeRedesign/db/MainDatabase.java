package udacityteam.healthapp.completeRedesign.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.UserRetrofitGood;


@Database(entities = {OneSharedFoodProductsListRetrofit.class,
        UserRetrofitGood.class}, version = MainDatabase.VERSION)
public abstract class MainDatabase extends RoomDatabase {

    public abstract RecipesMainDao recipeDao();

    static final int VERSION = 6;

}
