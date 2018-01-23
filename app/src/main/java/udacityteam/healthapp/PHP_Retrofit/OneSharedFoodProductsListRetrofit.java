package udacityteam.healthapp.PHP_Retrofit;

import java.util.ArrayList;

/**
 * Created by vvost on 12/29/2017.
 */

    public class OneSharedFoodProductsListRetrofit {
     private String UserId;
    private String Date;
    private String ParentSharedFoodsId;
    public OneSharedFoodProductsListRetrofit()
    {

    }

    public OneSharedFoodProductsListRetrofit(String userId, String date, String parentSharedFoodsId) {
        UserId = userId;
        Date = date;
        ParentSharedFoodsId = parentSharedFoodsId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getParentSharedFoodsId() {
        return ParentSharedFoodsId;
    }

    public void setParentSharedFoodsId(String parentSharedFoodsId) {
        ParentSharedFoodsId = parentSharedFoodsId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }



}

