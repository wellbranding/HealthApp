package udacityteam.healthapp.PHP_Retrofit_API;


import java.sql.Timestamp;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import udacityteam.healthapp.PHP_Retrofit_Models.Result;
import udacityteam.healthapp.PHP_Retrofit_Models.SelectedFoodretrofitarray;
import udacityteam.healthapp.PHP_Retrofit_Models.SharedFoodProductsRetrofit;
import udacityteam.healthapp.PHP_Retrofit_Models.Usersretrofit;

/**
 * Created by Belal on 14/04/17.
 */

public interface APIService {

    @FormUrlEncoded
    @POST("register")
    Call<Result> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("uid") String uid);

    @FormUrlEncoded
    @POST("login")
    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("loginwithmail")
    Call<Result> userLoginwithmail(
            @Field("email") String email,
            @Field("password") String password,
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @POST("addSelectedFood")
    Call<Result> addSelectedFood(
            @Field("foodId") String foodId,
            @Field("UserId") String UserId,
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
    Call<Result> addSharedList(
            @Field("UserId") String UserId,
            @Field("Date") Timestamp Date,
            @Field("SharedFoodListDatabase") String SharedFoodListDatabase,
            @Field("whichtime") String whichtime
    );

    @GET("getSelectedFoods")
    Call<SelectedFoodretrofitarray> getselectedfoods(
            @Query("UserId") String UserId,
            @Query("whichtime") String whichtime,
            @Query("year") String year,
            @Query("month") String month,
            @Query("day") String day
    );
    @GET("getSelectedFoodsPrieview")
    Call<SelectedFoodretrofitarray> getselectedfoodsPrieview(
            @Query("getParentSharedFoodsId") Integer ParentSharedKey,
            @Query("foodselection") String foodSelection
    );

    @GET("users")
    Call<Usersretrofit> getUsers();

    @GET("getAllSharedDiets")
    Call<SharedFoodProductsRetrofit> getAllSharedDiets(
            @Query("UserId") String UserId,
            @Query("SharedFoodListDatabase") String SharedFoodListDatabase
    );


    @GET("IsShared")
    Call<Result> getIsShared
    (
            @Query("UserId") String UserId,
            @Query("date") Timestamp Date,
            @Query("whichtime") String whichtime
    );


    @GET("usersquery")
    Call<Usersretrofit> getUsersquery();

    @GET("tasks/{sort}")
    Call<Usersretrofit> getTasks(
            @Path("sort") String order);
    @GET("qur")
    Call<Usersretrofit> getqur(
            @Query("value") String value,
            @Query("name") String name);


//    @FormUrlEncoded
//    @POST("sendmessage")
//    Call<MessageResponse> sendMessage(
//            @Field("from") int from,
//            @Field("to") int to,
//            @Field("title") String title,
//            @Field("message") String message);
//
//    @FormUrlEncoded
//    @POST("update/{id}")
//    Call<Result> updateUser(
//            @Path("id") int id,
//            @Field("name") String name,
//            @Field("email") String email,
//            @Field("password") String password,
//            @Field("gender") String gender
//    );
//
//    //getting messages
//    @GET("messages/{id}")
//    Call<Messages> getMessages(@Path("id") int id);
}
