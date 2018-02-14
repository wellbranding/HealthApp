package udacityteam.healthapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Belal on 14/04/17.
 */

public class Result {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private UserRetrofitGood user;

    public Result(Boolean error, String message, UserRetrofitGood user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }
    public Result(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public UserRetrofitGood getUser() {
        return user;
    }
}
