package udacityteam.healthapp.activities;

import android.app.Application;

/**
 * Created by vvost on 1/27/2018.
 */

public class ApplicationClass extends Application{
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
   public ApplicationClass()
   {

   }
}
