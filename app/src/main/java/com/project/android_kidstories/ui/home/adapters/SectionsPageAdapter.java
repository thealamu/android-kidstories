/*
package com.project.android_kidstories.ui.home.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SectionsPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragList = new ArrayList<>();
    private ArrayList<String> fragTitle = new ArrayList<>();

    public SectionsPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitle.get(position);
    }

    public void addFragment(Fragment f, String title){
        fragList.add(f);
        fragTitle.add(title);
    }
}
*/
