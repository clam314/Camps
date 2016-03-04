package com.clam.camps.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam.camps.R;

import com.clam.camps.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clam314 on 2016/3/2.
 */
public class MainFragment  extends Fragment {

    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        return view;
    }

    private void initView() {
        List<Fragment> list = new ArrayList<>();


        list.add(new LastFragment());
        list.add(new AndroidFragment("App"));
        list.add(new AndroidFragment("Android"));
        list.add(new AndroidFragment("iOS"));
        list.add(new AndroidFragment("前端"));
        list.add(new BeenFragment("福利"));


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs_ly);
        tabAdapter = new TabAdapter(getContext(), list, getChildFragmentManager());
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(6);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment","mainFragment onstop");
        super.onDetach();
        viewPager.removeAllViews();
    }

    @Override
    public void onDetach() {
        Log.d("Fragment","mainFragment ondetach");
        super.onDetach();
        viewPager.removeAllViews();
    }

    @Override
    public void onDestroyView() {
        Log.d("Fragment","mainFragment ondestroyview");
        super.onDestroyView();
        viewPager.removeAllViews();
    }
}
