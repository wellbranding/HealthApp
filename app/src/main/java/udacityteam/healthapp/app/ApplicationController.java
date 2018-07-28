package udacityteam.healthapp.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasServiceInjector;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import udacityteam.healthapp.Network.PHPService;
import udacityteam.healthapp.completeRedesign.Dagger2.DaggerAppComponent;

/**
 * Created by vvost on 2/8/2018.
 */

public class ApplicationController extends Application
    implements HasActivityInjector, HasServiceInjector, HasBroadcastReceiverInjector
    {

        @Override
        public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }

        @Inject
        DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

        @Inject
        DispatchingAndroidInjector<BroadcastReceiver> dispatchingBroadcastReceiverInjector;

        @Inject
        DispatchingAndroidInjector<Service> dispatchingServiceInjector;

        @Override
        public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

        @Override
        public DispatchingAndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

        @Override
        public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return dispatchingBroadcastReceiverInjector;
    }


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
