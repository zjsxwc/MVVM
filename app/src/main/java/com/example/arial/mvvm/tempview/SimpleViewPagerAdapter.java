package com.example.arial.mvvm.tempview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的Viewpager适配器
 */
public class SimpleViewPagerAdapter extends FragmentPagerAdapter {
  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();

  public SimpleViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  public void addFrag(Fragment fragment, String title) {
    mFragmentList.add(fragment);
    mFragmentTitleList.add(title);
  }

  @Override public CharSequence getPageTitle(int position) {
    return mFragmentTitleList.get(position);
  }

  @Override public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  //@Override public int getItemPosition(Object object) {
  //    return mFragmentList.indexOf(object);
  //}

  @Override public int getCount() {
    return mFragmentList.size();
  }
}