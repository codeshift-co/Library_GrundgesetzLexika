package com.example.lfranken.grundgesetzlexikalibrary.custom_views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ParallaxRecylcerViewOnScrollListener extends RecyclerView.OnScrollListener {

    private static final float PARALLAX_FACTOR = 2.5f;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        View view = recyclerView.getChildAt(0);
        if (view != null &&  recyclerView.getChildAdapterPosition(view) == 0){
            view.setTranslationY(((float)-view.getTop()) / PARALLAX_FACTOR);
        }
    }
}
