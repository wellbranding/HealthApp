package udacityteam.healthapp.activities;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;



import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import udacityteam.healthapp.Model.SelectedFoodretrofit;


/**
 * ViewModel for the RepositoryActivity
 */
public class FoodNutritiensDisplayViewModel implements ViewModel {

    private static final String TAG = "RepositoryViewModel";

    private SelectedFoodretrofit selectedFoodretrofit;
    private Context context;
    private Subscription subscription;

    public ObservableField<String> foodId;
    public ObservableField<String> nutritional;
//    public ObservableInt ownerEmailVisibility;
//    public ObservableInt ownerLocationVisibility;
//    public ObservableInt ownerLayoutVisibility;

    public FoodNutritiensDisplayViewModel(Context context, final SelectedFoodretrofit repository) {
        this.selectedFoodretrofit = repository;
        this.context = context;
        this.foodId = new ObservableField<>();
        this.nutritional = new ObservableField<>();
//        this.ownerName = new ObservableField<>();
//        this.ownerEmail = new ObservableField<>();
//        this.ownerLocation = new ObservableField<>();
//        this.ownerLayoutVisibility = new ObservableInt(View.INVISIBLE);
//        this.ownerEmailVisibility = new ObservableInt(View.VISIBLE);
//        this.ownerLocationVisibility = new ObservableInt(View.VISIBLE);
        // Trigger loading the rest of the user data as soon as the view model is created.
        // It's odd having to trigger this from here. Cases where accessing to the data model
        // needs to happen because of a change in the Activity/Fragment lifecycle
        // (i.e. an activity created) don't work very well with this MVVM pattern.
        // It also makes this class more difficult to test. Hopefully a better solution will be found
      //
    }

    public String getFoodId() {
        return selectedFoodretrofit.getFoodid();
    }

    public float getNutritional() {
        return selectedFoodretrofit.getCalories();
    }



    @Override
    public void destroy() {
        this.context = null;
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }



//    private void loadFullUser(String url) {
//        ArchiApplication application = ArchiApplication.get(context);
//        GithubService githubService = application.getGithubService();
//        subscription = githubService.userFromUrl(url)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(application.defaultSubscribeScheduler())
//                .subscribe(new Action1<User>() {
//                    @Override
//                    public void call(User user) {
//                        Log.i(TAG, "Full user data loaded " + user);
//                        ownerName.set(user.name);
//                        ownerEmail.set(user.email);
//                        ownerLocation.set(user.location);
//                        ownerEmailVisibility.set(user.hasEmail() ? View.VISIBLE : View.GONE);
//                        ownerLocationVisibility.set(user.hasLocation() ? View.VISIBLE : View.GONE);
//                        ownerLayoutVisibility.set(View.VISIBLE);
//                    }
//                });
//    }
}
