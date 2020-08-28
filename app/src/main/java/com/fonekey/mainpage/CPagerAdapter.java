package com.fonekey.mainpage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CPagerAdapter extends FragmentPagerAdapter {

    CRentFragment m_rent;
    CSurrenderFragment m_surrender;

    public CPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);

        m_rent = new CRentFragment();
        m_surrender = new CSurrenderFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return m_rent;
            case 1:
                return m_surrender;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
