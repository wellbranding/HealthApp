package udacityteam.healthapp.PHP_Retrofit;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @POST("addSelectedFood")
    Call<Result> addSelectedFood(
            @Field("foodId") String foodId,
            @Field("UserId") String UserId,
            @Field("Date") String Date,
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
            @Field("Date") String Date,
            @Field("SharedFoodListDatabase") String SharedFoodListDatabase,
            @Field("whichtime") String whichtime
    );

    @GET("getSelectedFoods")
    Call<SelectedFoodretrofitarray> getselectedfoods(
            @Query("UserId") String UserId,
            @Query("whichtime") String whichtime
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
            @Query("date") String Date,
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
