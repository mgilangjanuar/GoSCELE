package com.mgilangjanuar.dev.goscele.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mgilangjanuar.dev.goscele.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class TabPagerAdapterUtil extends FragmentPagerAdapter {

    private final List<BaseFragment> fragmentList = new ArrayList<>();
    private final List<String> stringList = new ArrayList<>();

    public TabPagerAdapterUtil(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addFragment(BaseFragment fragment, String string) {
        fragmentList.add(fragment);
        stringList.add(string);
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
