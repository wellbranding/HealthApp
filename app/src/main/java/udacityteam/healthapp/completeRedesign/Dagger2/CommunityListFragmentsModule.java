package udacityteam.healthapp.completeRedesign.Dagger2;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import udacityteam.healthapp.activities.CommunityActivities.SharedFoodListFragmentNetwork;

@Module
public abstract class CommunityListFragmentsModule {
//    @ContributesAndroidInjector()
//    abstract DetailStepFragment contributeDetailStepFragmenty();
//
   @ContributesAndroidInjector()
   abstract SharedFoodListFragmentNetwork contributeSharedFoodListFragmentNetwork();

}
