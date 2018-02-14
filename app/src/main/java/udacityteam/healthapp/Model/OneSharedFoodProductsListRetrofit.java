package udacityteam.healthapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by vvost on 12/29/2017.
 */

    public class OneSharedFoodProductsListRetrofit implements Parcelable {
     private String UserId;
     private String Date;
    private Integer ParentSharedFoodsId;
    private float Calories;
    private float Protein;
    private float Fat;
    private float Carbohydrates;


    protected OneSharedFoodProductsListRetrofit(Parcel in) {
        UserId = in.readString();
        Date = in.readString();
        if (in.readByte() == 0) {
            ParentSharedFoodsId = null;
        } else {
            ParentSharedFoodsId = in.readInt();
        }
        Calories = in.readFloat();
        Protein = in.readFloat();
        Fat = in.readFloat();
        Carbohydrates = in.readFloat();
    }

    public static final Creator<OneSharedFoodProductsListRetrofit> CREATOR = new Creator<OneSharedFoodProductsListRetrofit>() {
        @Override
        public OneSharedFoodProductsListRetrofit createFromParcel(Parcel in) {
            return new OneSharedFoodProductsListRetrofit(in);
        }

        @Override
        public OneSharedFoodProductsListRetrofit[] newArray(int size) {
            return new OneSharedFoodProductsListRetrofit[size];
        }
    };

    public float getCalories() {
        return Calories;
    }

    public void setCalories(float calories) {
        Calories = calories;
    }

    public float getProtein() {
        return Protein;
    }

    public void setProtein(float protein) {
        Protein = protein;
    }

    public float getFat() {
        return Fat;
    }

    public void setFat(float fat) {
        Fat = fat;
    }

    public float getCarbohydrates() {
        return Carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        Carbohydrates = carbohydrates;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserId);
        dest.writeString(Date);
        if (ParentSharedFoodsId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ParentSharedFoodsId);
        }
        dest.writeFloat(Calories);
        dest.writeFloat(Protein);
        dest.writeFloat(Fat);
        dest.writeFloat(Carbohydrates);
    }
}

