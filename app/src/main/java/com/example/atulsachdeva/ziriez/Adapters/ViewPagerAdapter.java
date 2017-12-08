package com.example.atulsachdeva.ziriez.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.atulsachdeva.ziriez.Fragments.MainFragment;

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

        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                bundle.putString("TabHeader", "Summer");
                break;
            case 1:
                bundle.putString("TabHeader", "Winter");
                break;
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}
