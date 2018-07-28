package udacityteam.healthapp.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import udacityteam.healthapp.Model.Result;
import udacityteam.healthapp.Model.SelectedFoodretrofitarray;
import udacityteam.healthapp.Model.SharedFoodProductsRetrofit;
import udacityteam.healthapp.Model.Usersretrofit;
import udacityteam.healthapp.completeRedesign.Data.Networking.API.LiveDataCallAdapterFactory;

import static udacityteam.healthapp.PHP_Retrofit_API.APIUrl.BASE_URL;

public interface PHPService {

    public static final String BASE_URL = "http://app.wellbranding.com/";


@FormUrlEncoded
@POST("addSelectedFood")
Call<Result> addSelectedFood(
        @Field("foodId") String foodId,
        @Field("foodName") String foodName,
        @Field("UserId") Integer UserId,
        @Field("Date") Timestamp Date,
        @Field("Calories") Float Calories,
        @Field("Protein") Float Protein,
        @Field("Fat") Float Fat,
        @Field("Carbohydrates") Float Carbohyrates,
        @Field("whichtime") String whichtime,
        @Field("sharedfoodId") Integer sharedFoodId
);
    @FormUrlEncoded
    @POST("addSharedList")
   Observable<Result> addSharedList(
            @Field("UserId") Integer UserId,
            @Field("Date") Timestamp Date,
            @Field("SharedFoodListDatabase") String SharedFoodListDatabase,
            @Field("whichtime") String whichtime,
            @Field("Calories") Float Calories,
            @Field("Protein") Float Protein,
            @Field("Fat") Float Fat,
            @Field("Carbohydrates") Float Carbohyrates
    );

    @GET("getUserByUid")
    Observable<Result> getCurrentUser(
            @Query("UserId") String UserId

    );
    @GET("getSelectedFoods")
    Observable<SelectedFoodretrofitarray> getselectedfoods(
            @Query("UserId") Integer UserId,
            @Query("whichtime") String whichtime,
            @Query("year") String year,
            @Query("month") String month,
            @Query("day") String day
    );
    @GET("getSelectedFoodsPrieview")
    Observable<SelectedFoodretrofitarray> getselectedfoodsPrieview(
            @Query("getParentSharedFoodsId") Integer ParentSharedKey,
            @Query("foodselection") String foodSelection
    );

    @GET("users")
    Observable<Usersretrofit> getUsers();

    @GET("getAllSharedDiets")
    Observable<SharedFoodProductsRetrofit> getAllSharedDiets(
            @Query("UserId") Integer UserId,
            @Query("SharedFoodListDatabase") String SharedFoodListDatabase
    );
    @GET("getAllFilteredSharedDiets")
    Observable<SharedFoodProductsRetrofit> getAllFilteredSharedDiets(
            @Query("UserId") Integer UserId,
            @Query("SharedFoodListDatabase") String SharedFoodListDatabase,
            @Query("ProteinBegin") Integer proteinbegin,
            @Query("ProteinEnd") Integer proteinend,
            @Query("CaloriesBegin") Integer caloriesbegin,
            @Query("CaloriesEnd") Integer caloriesend,
            @Query("CarbohydratesBegin") Integer carbohydratesbegin,
            @Query("CarbohydratesEnd") Integer carbohydratesend,
            @Query("FatsBegin") Integer fatsbegin,
            @Query("FatsEnd") Integer fatssend

    );

    @GET("IsShared")
    Observable<Result> getIsShared
            (
                    @Query("UserId") Integer UserId,
                    @Query("date") Timestamp Date,
                    @Query("whichtime") String whichtime
            );

    @GET("usersquery")
    Observable<Usersretrofit> getUsersquery();

    @GET("tasks/{sort}")
    Observable<Usersretrofit> getTasks(
            @Path("sort") String order);
    @GET("qur")
    Call<Usersretrofit> getqur(
            @Query("value") String value,
            @Query("name") String name);


    class Factory {
        public static PHPService create() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(PHPService.class);

        }
    }
}
