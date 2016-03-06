package com.clam.camps.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
    private List<Fragment> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        return view;
    }

    private void initView() {

        list.add(new LastFragment());
        list.add(new CategoryFragment("App"));
        list.add(new CategoryFragment("Android"));
        list.add(new CategoryFragment("iOS"));
        list.add(new CategoryFragment("前端"));
        list.add(new BeenFragment("福利",false));


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        tabAdapter = new TabAdapter(getContext(), list, getChildFragmentManager());
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(6);

       // viewPager.setCurrentItem(0);
    }


    @Override
    public void onStart() {
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs_ly);
        tabLayout.setupWithViewPager(viewPager);
        super.onStart();
    }

    public void refresh(){
        switch (viewPager.getCurrentItem()){
            case 0:
                ((LastFragment)list.get(0)).refresh();
                break;
            case 5:
                ((BeenFragment)list.get(5)).refresh();
                break;
            default:
                ((CategoryFragment)list.get(viewPager.getCurrentItem())).refresh();
                break;
        }
    }
}
