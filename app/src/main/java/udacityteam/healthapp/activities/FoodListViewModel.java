package udacityteam.healthapp.activities;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;

/**
 * View model for the MainActivity
 */
public class FoodListViewModel implements ViewModel {

    private static final String TAG = "MainViewModel";

    public ObservableInt infoMessageVisibility;
    public ObservableInt progressVisibility;
    public ObservableInt recyclerViewVisibility;
    public ObservableInt searchButtonVisibility;
    public ObservableField<String> infoMessage;

    private Context context;
    private Subscription subscription;
    //private List<Repository> repositories;
    private String editTextUsernameValue;

    public FoodListViewModel(Context context) {
        this.context = context;
        infoMessageVisibility = new ObservableInt(View.VISIBLE);
        progressVisibility = new ObservableInt(View.INVISIBLE);
        recyclerViewVisibility = new ObservableInt(View.INVISIBLE);
        searchButtonVisibility = new ObservableInt(View.GONE);
        infoMessage = new ObservableField<>("message");
    }


    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
      //  dataListener = null;
    }

    public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String username = view.getText().toString();
            if (username.length() > 0) loadGithubRepos(username);
            return true;
        }
        return false;
    }

    public void onClickSearch(View view) {
        loadGithubRepos(editTextUsernameValue);
    }

    public TextWatcher getUsernameEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextUsernameValue = charSequence.toString();
                searchButtonVisibility.set(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void loadGithubRepos(String username) {
        progressVisibility.set(View.VISIBLE);
        recyclerViewVisibility.set(View.INVISIBLE);
        infoMessageVisibility.set(View.INVISIBLE);
    }
      //  if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe()
//        subscription = githubService.publicRepositories(username)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(application.defaultSubscribeScheduler())
//                .subscribe(new Subscriber<List<Repository>>() {
//                    @Override
//                    public void onCompleted() {
//                        if (dataListener != null) dataListener.onRepositoriesChanged(repositories);
//                        progressVisibility.set(View.INVISIBLE);
//                        if (!repositories.isEmpty()) {
//                            recyclerViewVisibility.set(View.VISIBLE);
//                        } else {
//                            infoMessage.set(context.getString(R.string.text_empty_repos));
//                            infoMessageVisibility.set(View.VISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                        Log.e(TAG, "Error loading GitHub repos ", error);
//                        progressVisibility.set(View.INVISIBLE);
//                        if (isHttp404(error)) {
//                            infoMessage.set(context.getString(R.string.error_username_not_found));
//                        } else {
//                            infoMessage.set(context.getString(R.string.error_loading_repos));
//                        }
//                        infoMessageVisibility.set(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onNext(List<Repository> repositories) {
//                        Log.i(TAG, "Repos loaded " + repositories);
//                        FoodListViewModel.this.repositories = repositories;
//                    }
//                });
//    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }


}
