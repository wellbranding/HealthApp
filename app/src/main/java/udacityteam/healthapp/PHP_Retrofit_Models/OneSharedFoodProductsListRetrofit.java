package udacityteam.healthapp.PHP_Retrofit_Models;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by vvost on 12/29/2017.
 */

    public class OneSharedFoodProductsListRetrofit {
     private String UserId;
     private String Date;
    private Integer ParentSharedFoodsId;
    public OneSharedFoodProductsListRetrofit()
    {

    }

    public OneSharedFoodProductsListRetrofit(String userId, String date,Integer parentSharedFoodsId) {
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

    public Integer getParentSharedFoodsId() {
        return ParentSharedFoodsId;
    }

    public void setParentSharedFoodsId(Integer parentSharedFoodsId) {
        ParentSharedFoodsId = parentSharedFoodsId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }



}

