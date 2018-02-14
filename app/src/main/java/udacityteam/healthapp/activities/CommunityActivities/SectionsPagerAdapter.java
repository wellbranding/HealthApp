package udacityteam.healthapp.activities.CommunityActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by vvost on 2/11/2018.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles;
    private Bundle queryParam;

    public SectionsPagerAdapter(FragmentManager fm, String[] tabTitles, Bundle qureryparam) {
        super(fm);
        this.tabTitles = tabTitles;
        this.queryParam = qureryparam;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CommunityFoodListsDisplayFragment0MVVM communityFoodListsDisplayFragment0 = new CommunityFoodListsDisplayFragment0MVVM();
//                communityFoodListsDisplayFragment0.setArguments(queryParam);
                return communityFoodListsDisplayFragment0;
            case 1:
                return new CommunityFoodListsDisplayFragment1();
            case 2:
                return new CommunityFoodListsDisplayFragment2();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }


}