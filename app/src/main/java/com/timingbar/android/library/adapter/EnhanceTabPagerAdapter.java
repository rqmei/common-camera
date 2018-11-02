package com.timingbar.android.library.adapter;

import android.support.v4.app.FragmentManager;
import com.timingbar.android.library.ui.fragment.EnhanceItemFragment;
import lib.android.timingbar.com.base.adapter.BaseFragmentPagerAdapter;
import lib.android.timingbar.com.base.fragment.BaseLazyFragment;

import java.util.List;

/**
 * EnhanceTabPagerAdapter
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on2018/11/1
 */

public class EnhanceTabPagerAdapter extends BaseFragmentPagerAdapter<BaseLazyFragment> {
   

    public EnhanceTabPagerAdapter(FragmentManager fm) {
        super (fm);
    }

    @Override
    protected void init(FragmentManager fm, List<BaseLazyFragment> list) {
        for (int i = 0; i < 5; i++) {
            list.add (EnhanceItemFragment.newInstance (i + 1));
        }
    }

}