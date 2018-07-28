package udacityteam.healthapp.completeRedesign;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasServiceInjector;

public class App extends Application
     //   implements HasActivityInjector, HasServiceInjector, HasBroadcastReceiverInjector
{

//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        DaggerAppComponent.builder()
//                .application(this)
//                .build()
//                .inject(this);
//    }
//
//    @Inject
//    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
//
//    @Inject
//    DispatchingAndroidInjector<BroadcastReceiver> dispatchingBroadcastReceiverInjector;
//
//    @Inject
//    DispatchingAndroidInjector<Service> dispatchingServiceInjector;
//
//    @Override
//    public DispatchingAndroidInjector<Activity> activityInjector() {
//        return dispatchingAndroidInjector;
//    }
//
//    @Override
//    public DispatchingAndroidInjector<Service> serviceInjector() {
//        return dispatchingServiceInjector;
//    }
//
//    @Override
//    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
//        return dispatchingBroadcastReceiverInjector;
//    }
}
