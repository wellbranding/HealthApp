package udacityteam.healthapp.completeRedesign.Data.Networking.API;

import java.sql.Timestamp;

import retrofit2.Call;
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

public interface NEW_API_GOOD_SERVICE {
    public static final String BASE_URL = "http://app.wellbranding.com/";

    @FormUrlEncoded
    @POST("register")
    Observable<Result> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("uid") String uid);


    @FormUrlEncoded
    @POST("login")
    Observable<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("loginwithmail")
    Observable<Result> userLoginwithmail(
            @Field("email") String email,
            @Field("password") String password,
            @Field("uid") String uid
    );
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
}
