package com.peihuo.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.lang.reflect.Field;

/**
 * Created by hb on 2017/9/19.
 */

public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setReadEffect();
        setScrollerDuration();
    }

    private void setScrollerDuration() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), new DecelerateInterpolator());
            field.set(this, scroller);
            scroller.setmDuration(300);
        } catch (Exception e) {
        }
    }

    public void setReadEffect() {
        setPageTransformer(true, new PageTransformer() {
            private static final float MIN_SCALE = 0.75f;

            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();
                if (position < -1) {
                    view.setAlpha(0);
                } else if (position <= 0) {
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    view.setScaleX(1);
                    view.setScaleY(1);
                } else if (position <= 1) {
                    view.setAlpha(1 - position);
                    view.setAlpha(1);
                    view.setTranslationX(pageWidth * -position);
                } else {
                    view.setAlpha(0);
                }
            }
        });
    }
}