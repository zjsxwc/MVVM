package com.example.arial.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.core.AbsFragment;
import com.example.arial.mvvm.activity.AbsActivityTest;
import com.example.arial.mvvm.base.BaseFragment;
import com.example.arial.mvvm.databinding.ActivityMainBinding;
import com.example.arial.mvvm.fragment.AbsDialogFragmentTest;
import com.example.arial.mvvm.fragment.AbsFragmentTest;
import com.example.arial.mvvm.fragment.ModuleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/4/12.
 */
public class MainActivity extends AbsActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDLayout;
    @InjectView(R.id.nav_view)
    NavigationView mNv;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    List<AbsFragment> mFragments = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDLayout.setDrawerListener(toggle);
        toggle.syncState();
        mNv.setNavigationItemSelectedListener(this);
        mFragments.add(ModuleFragment.newInstance());
        mFragments.add(AbsFragmentTest.newInstance());
        mFragments.add(AbsDialogFragmentTest.newInstance());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (AbsFragment fragment : mFragments) {
            ft.add(R.id.content, fragment).commit();
        }
        showFragment(mFragments.get(0));
        mToolbar.setTitle("Module使用");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (mDLayout.isDrawerOpen(GravityCompat.START)) {
            mDLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showFragment(AbsFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (AbsFragment f : mFragments) {
            if (f.hashCode() == fragment.hashCode()) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.module:
                showFragment(mFragments.get(0));
                mToolbar.setTitle(item.getTitle());
                break;
            case R.id.activity:
                startActivity(new Intent(this, AbsActivityTest.class));
                break;
            case R.id.fragment:
                break;
            case R.id.dialog:
                break;
            case R.id.pop:
                break;
            case R.id.crash:
                break;
            case R.id.temp:
                break;
            case R.id.permission:
                break;
        }
        mDLayout.closeDrawers();
        return false;
    }

    @Override
    protected void dataCallback(int result, Object data) {

    }
}
