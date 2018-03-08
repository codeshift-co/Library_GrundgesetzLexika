package com.example.lfranken.grundgesetzlexikalibrary.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.lfranken.grundgesetzlexikalibrary.R;

/**
 * Created by lfranken on 05.12.2017.
 */

public class ParallaxScrollView extends ScrollView {

    private static final float PARALLAX_FACTOR_DEFAULT = 1.5f;
    private static final float INNER_PARALLAX_FACTOR_DEFAULT = 1.5f;
    private static final int NUMBER_OF_VIEWS_TO_PARALLAX = 1;
    private float parallaxFactor;
    private float innerParallaxFactor;
    private int numberOfViewsToParallax;
    private View[] parallaxViews;
    private boolean numberOfViewsToParallaxHasChanged = false;

    public ParallaxScrollView(Context context) {
        super(context);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParallaxScrollView);
        parallaxFactor = typedArray.getFloat(R.styleable.ParallaxScrollView_parallaxFactor, PARALLAX_FACTOR_DEFAULT);
        innerParallaxFactor = typedArray.getFloat(R.styleable.ParallaxScrollView_innerParallaxFactor, INNER_PARALLAX_FACTOR_DEFAULT);
        numberOfViewsToParallax = typedArray.getInt(R.styleable.ParallaxScrollView_numberOfViewsToParallax, NUMBER_OF_VIEWS_TO_PARALLAX);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        saveParallaxViews();
    }

    private void saveParallaxViews() {
        if (getChildCount() <= 0 || !(getChildAt(0) instanceof ViewGroup)) return;
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        int minNumberOfViewsToParallax = Math.min(this.numberOfViewsToParallax, viewGroup.getChildCount());
        parallaxViews = new View[minNumberOfViewsToParallax];
        for (int i = 0; i < minNumberOfViewsToParallax; i++) {
            parallaxViews[i] = viewGroup.getChildAt(i);
        }
        numberOfViewsToParallaxHasChanged = false;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (numberOfViewsToParallaxHasChanged) saveParallaxViews();
        if (parallaxViews == null || parallaxViews.length == 0) return;
        float currentParallax = parallaxFactor;
        for (View parallaxView : parallaxViews) {
            scrollParallaxView(parallaxView, ((float) t / currentParallax));
            currentParallax *= innerParallaxFactor;
        }
    }

    private void scrollParallaxView(View view, float offset) {
        if (view == null) return;
        view.setTranslationY(Math.round(offset));
    }

    public void setNumberOfViewsToParallax(int numberOfViewsToParallax) {
        this.numberOfViewsToParallax = numberOfViewsToParallax;
        numberOfViewsToParallaxHasChanged = true;
    }
}
