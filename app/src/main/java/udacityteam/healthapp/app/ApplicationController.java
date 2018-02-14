package udacityteam.healthapp.app;

import android.app.Application;
import android.content.Context;

import rx.Scheduler;
import rx.schedulers.Schedulers;
import udacityteam.healthapp.Network.PHPService;

/**
 * Created by vvost on 2/8/2018.
 */

public class ApplicationController extends Application {
    private PHPService phpService;
    private Scheduler defaultSubscribeScheduler;

    public static ApplicationController get(Context context) {
        return (ApplicationController) context.getApplicationContext();
    }
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public ApplicationController()
    {

    }

    public PHPService getPHPService() {
        if (phpService == null) {
            phpService = PHPService.Factory.create();
        }
        return phpService;
    }

    //For setting mocks during testing
    public void setGithubService(PHPService githubService) {
        this.phpService = githubService;
    }
    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }
}
