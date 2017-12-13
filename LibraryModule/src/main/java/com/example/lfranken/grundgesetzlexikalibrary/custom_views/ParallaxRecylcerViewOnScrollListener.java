package com.example.lfranken.grundgesetzlexikalibrary.custom_views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * {@link ParallaxRecylcerViewOnScrollListener} allows you to have the first item in your recyclerview
 * parallaxed. Be aware tho that the instance you give to your recyclerview should be final.
 */
public class ParallaxRecylcerViewOnScrollListener extends RecyclerView.OnScrollListener {

    private static final float PARALLAX_FACTOR = 2.5f;
    private int lastOffset;
    private Rect clippingRect = new Rect();
    private boolean veryFirstScroll = true;


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        View view = recyclerView.getChildAt(0);
        if (view == null || recyclerView.getChildAdapterPosition(view) != 0) return;

        int offset = Math.round(((float) -view.getTop()) / PARALLAX_FACTOR);
        if (veryFirstScroll) {
            lastOffset = offset;
            initClippingRect(view);
            veryFirstScroll = false;
        }
        clippingRect.bottom += lastOffset - offset;
        view.setTranslationY(offset);
        view.setClipBounds(clippingRect);
        lastOffset = offset;
        checkVisbility(view);
    }

    private void initClippingRect(View view) {
        clippingRect.left = view.getLeft();
        clippingRect.top = view.getTop();
        clippingRect.right = view.getRight();
        clippingRect.bottom = view.getBottom();
    }

    private void checkVisbility(View view) {
        if (clippingRect.bottom <= clippingRect.top) view.setVisibility(View.INVISIBLE);
        else view.setVisibility(View.VISIBLE);
    }
}