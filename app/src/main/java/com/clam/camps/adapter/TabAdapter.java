package com.clam.camps.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clam.camps.fragment.LastFragment;

import java.util.List;

/**
 * Created by clam314 on 2016/3/1.
 */
public class TabAdapter  extends FragmentStatePagerAdapter{

    private Context mContext;
    private String[] tabTitle = {"最新","App","Android","iOS","前端","妹子图"};
    private List<Fragment> list;

    public TabAdapter(Context context ,List<Fragment> list,FragmentManager fragmentManager){
        super(fragmentManager);
        this.list = list;
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    //

}
