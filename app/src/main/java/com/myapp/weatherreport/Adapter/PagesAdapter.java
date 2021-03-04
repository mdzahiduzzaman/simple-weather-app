package com.myapp.weatherreport.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagesAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fList = new ArrayList<>();
    private final List<String> fTitle = new ArrayList<>();

    public PagesAdapter(FragmentManager manager) {
        super(manager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fList.get(position);
    }

    @Override
    public int getCount() {
        return fList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fList.add(fragment);
        fTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fTitle.get(position);
    }
}
