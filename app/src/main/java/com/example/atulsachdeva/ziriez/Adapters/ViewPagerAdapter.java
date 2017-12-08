package com.example.atulsachdeva.ziriez.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.atulsachdeva.ziriez.Fragments.SummerFragment;
import com.example.atulsachdeva.ziriez.Fragments.WinterFragment;

/**
 * Created by AtulSachdeva on 08/12/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    int nTabs;

    public ViewPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        nTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SummerFragment();
            case 1:
                return new WinterFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}
