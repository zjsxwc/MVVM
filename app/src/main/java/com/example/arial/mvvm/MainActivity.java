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
import android.view.Gravity;
import android.view.MenuItem;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.core.AbsFragment;
import com.arialyy.frame.util.show.T;
import com.example.arial.mvvm.activity.AbsActivityTest;
import com.example.arial.mvvm.activity.TempViewActivity;
import com.example.arial.mvvm.config.Constance;
import com.example.arial.mvvm.databinding.ActivityMainBinding;
import com.example.arial.mvvm.dialog.IpDialog;
import com.example.arial.mvvm.fragment.AbsDialogFragmentTest;
import com.example.arial.mvvm.fragment.ModuleFragment;
import com.example.arial.mvvm.fragment.PermissionFragment;
import com.example.arial.mvvm.fragment.TempFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by lyy on 2016/4/12.
 */
public class MainActivity extends AbsActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    DrawerLayout   mDLayout;
    @Bind(R.id.nav_view)
    NavigationView mNv;
    @Bind(R.id.toolbar)
    Toolbar        mToolbar;
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
        mFragments.add(TempFragment.newInstance());
        mFragments.add(PermissionFragment.newInstance());
//        mFragments.add(AbsDialogFragmentTest.newInstance());

        FragmentManager     fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        int                 i  = 0;
        for (AbsFragment fragment : mFragments) {
            ft.add(R.id.content, fragment, i + "");
        }
        ft.commit();
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
        fm.beginTransaction().replace(R.id.content, fragment).commit();
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
            case R.id.dialog:
                IpDialog dialog = new IpDialog(this);
                dialog.show(getSupportFragmentManager(), "ip_dialog");
                break;
            case R.id.pop:
                PopupWindowTest pop = new PopupWindowTest(this, this);
                pop.showAtLocation(mRootView, Gravity.NO_GRAVITY, 0, 0);
                break;
            case R.id.crash:
                break;
            case R.id.temp_view:
                showFragment(mFragments.get(1));
                mToolbar.setTitle(item.getTitle());
                break;
            case R.id.permission:
                showFragment(mFragments.get(2));
                mToolbar.setTitle(item.getTitle());
                break;
        }
        mDLayout.closeDrawers();
        return false;
    }

    @Override
    protected void dataCallback(int result, Object data) {
        if (result == Constance.KEY.GET_IP) {
            T.showShort(this, data + "");
        } else if (result == Constance.KEY.IP_DIALOG) {
            T.showShort(this, data + "");
        } else if (result == Constance.KEY.POP) {
            T.showShort(this, data + "");
        }
    }
}
