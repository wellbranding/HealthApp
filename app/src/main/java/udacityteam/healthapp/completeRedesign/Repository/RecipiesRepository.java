package udacityteam.healthapp.completeRedesign.Repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;


import udacityteam.healthapp.Model.OneSharedFoodProductsListRetrofit;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.completeRedesign.Dagger2.AppExecutors;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.APIService;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.ApiResponse;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.RetrofitFactoryNew;
import udacityteam.healthapp.completeRedesign.db.MainDatabase;
import udacityteam.healthapp.completeRedesign.db.RecipesMainDao;

public class RecipiesRepository {

    APIService webservice;
    RecipesMainDao userDao;
    APIService apiService;
    AppExecutors appExecutors;
    MainDatabase mainDatabase;

    public MainDatabase getMainDatabase() {
        return mainDatabase;
    }



    public RecipesMainDao getUserDao() {
        return userDao;
    }



    @Inject
    RecipiesRepository(AppExecutors appExecutors, APIService apiService, MainDatabase mainDatabase) {
        userDao = mainDatabase.recipeDao();
        this.appExecutors = appExecutors;
        this.mainDatabase = mainDatabase;
        this.apiService = apiService;

    }

    public LiveData<Resource<List<OneSharedFoodProductsListRetrofit>>> loadRecipes() {
        return new NetworkBoundResource<List<OneSharedFoodProductsListRetrofit>,SharedFoodProductsRetrofit >(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull SharedFoodProductsRetrofit item) {
                userDao.insertAllOneSharedFoodProductsListRetrofit(item.getSelectedFoodretrofits());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<OneSharedFoodProductsListRetrofit> data) {
                return data == null ||
                        data.size()==0;
            }

            @NonNull @Override
            protected LiveData<List<OneSharedFoodProductsListRetrofit>> loadFromDb() {
                return userDao.getRecipes();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SharedFoodProductsRetrofit>> createCall() {
                return RetrofitFactoryNew.create().getAllSharedDiets(2,"Dinner" );
            }


        }.getAsLiveData();
    }
}

