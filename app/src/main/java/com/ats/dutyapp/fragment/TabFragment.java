package com.ats.dutyapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.dutyapp.R;
import com.ats.dutyapp.interfaces.CloseInterface;
import com.ats.dutyapp.interfaces.OpenInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tab;
    FragmentPagerAdapter adapterViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tab, container, false);
        getActivity().setTitle("Details");

        viewPager = view.findViewById(R.id.viewPager);
        tab = view.findViewById(R.id.tab);

        adapterViewPager = new ViewPagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapterViewPager);
        tab.post(new Runnable() {
            @Override
            public void run() {
                try {
                    tab.setupWithViewPager(viewPager);
                } catch (Exception e) {
                }
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ////Log.e("POSITION : ", "----------------------" + position);

                if (position == 0) {

                    OpenInterface openInterface = (OpenInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (openInterface != null) {
                        openInterface.fragmentBecameVisible();
                    }

                } else if (position == 1) {

                    CloseInterface closeInterface = (CloseInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (closeInterface != null) {
                        closeInterface.fragmentBecameVisible();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public ViewPagerAdapter(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new OpenFragment();
            } else {
                return new CloseFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Open";
                case 1:
                    return "Close";
                default:
                    return null;
            }
        }
    }


}
