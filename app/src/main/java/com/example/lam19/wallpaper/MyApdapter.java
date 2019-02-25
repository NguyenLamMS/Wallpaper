package com.example.lam19.wallpaper;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyApdapter extends FragmentStatePagerAdapter {
    private String titleTab[] = {"FEATURED","CATEGORIES"};
    public MyApdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleTab[position];
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                TabOne one =new TabOne();
                return one;
            case 1:
                TabTwo two =  new TabTwo();
                return two;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titleTab.length;
    }
}
