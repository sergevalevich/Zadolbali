package com.valevich.zadolbali.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.valevich.zadolbali.R;
import com.valevich.zadolbali.ui.fragments.FavoriteFragment_;
import com.valevich.zadolbali.ui.fragments.StoriesFragment_;

/**
 * Created by NotePad.by on 06.06.2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    private int[] imageResId = {
            R.drawable.ic_tab_stories,
            R.drawable.ic_tab_favorite,
    };

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StoriesFragment_();
            case 1:
                return new FavoriteFragment_();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(mContext, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

}
