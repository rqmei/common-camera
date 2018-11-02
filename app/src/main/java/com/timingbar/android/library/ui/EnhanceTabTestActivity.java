package com.timingbar.android.library.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import butterknife.BindView;
import com.timingbar.android.library.R;
import com.timingbar.android.library.adapter.EnhanceTabPagerAdapter;
import com.timingbar.android.library.ui.fragment.EnhanceItemFragment;
import lib.android.timingbar.com.base.activity.BaseActivity;
import lib.android.timingbar.com.view.EnhanceTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EnhanceTabTestActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/11/1
 */

public class EnhanceTabTestActivity extends BaseActivity {
    @BindView(R.id.enhance_tab_layout)
    EnhanceTabLayout enhanceTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    String[] sTitle = {"首页", "新闻", "公告", "热点", "咨询好久疾风剑豪"};

    @Override
    public int getLayoutResId() {
        return R.layout.enhance_tab_test;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        for (int i = 0; i < sTitle.length; i++) {
            enhanceTabLayout.addTab (sTitle[i]);
        }
        EnhanceTabPagerAdapter adapter = new EnhanceTabPagerAdapter (getSupportFragmentManager ());
        viewPager.setAdapter (adapter);
        viewPager.addOnPageChangeListener (new TabLayout.TabLayoutOnPageChangeListener (enhanceTabLayout.getTabLayout ()));
        viewPager.setCurrentItem (2);
        enhanceTabLayout.setupWithViewPager (viewPager);
    }
 

}
