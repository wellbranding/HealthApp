package udacityteam.healthapp.completeRedesign;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import udacityteam.healthapp.Model.UserProfile;
import udacityteam.healthapp.models.User;

public class DataConverterStep {
    @TypeConverter
    public String fromRoom(UserProfile roomStep) {
        if (roomStep == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<UserProfile>() {
        }.getType();
        return gson.toJson(roomStep, type);
    }

    @TypeConverter
    public UserProfile toSteps(String step) {
        if (step == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<UserProfile>() {
        }.getType();
        return gson.fromJson(step, type);
    }
}
