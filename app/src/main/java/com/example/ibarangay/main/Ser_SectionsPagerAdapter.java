package com.example.ibarangay.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ibarangay.R;
import com.example.ibarangay.Req_Fragment1;
import com.example.ibarangay.Req_Fragment2;
import com.example.ibarangay.Req_Fragment3;
import com.example.ibarangay.Req_Fragment4;
import com.example.ibarangay.Ser_Fragment1;
import com.example.ibarangay.Ser_Fragment2;
import com.example.ibarangay.Ser_Fragment3;
import com.example.ibarangay.Ser_Fragment4;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class Ser_SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public Ser_SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new Ser_Fragment1();
                break;
            case 1:
                fragment = new Ser_Fragment2();
                break;
            case 2:
                fragment = new Ser_Fragment3();
                break;
            case 3:
                fragment = new Ser_Fragment4();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }
}