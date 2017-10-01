package com.legend.ys8.adaper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.legend.ys8.view.ImageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by legend on 2017/9/17.
 */

public class CharacterImageAdapter extends FragmentStatePagerAdapter{

    String[] strings;
    List<ImageFragment> fragments;

    public CharacterImageAdapter(FragmentManager fm) {
        super(fm);
//        initFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments!=null){
            return fragments.get(position);
        }

        return null;
    }

    @Override
    public int getCount() {
        if (fragments!=null){
            return fragments.size();
        }
        return 0;
    }

    //关键方法
    public void setStrings(String[] strings) {
        this.strings = strings;
        initFragment();
    }

    private void initFragment(){
        if (strings!=null){
            fragments=new ArrayList<>();
            for (String s:strings){
                ImageFragment imageFragment=ImageFragment.newInstance();
                imageFragment.setUrl(s);
                fragments.add(imageFragment);
            }
            notifyDataSetChanged();
        }
    }
}
