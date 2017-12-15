package com.example.lfranken.grundgesetzlexikalibrary.custom_views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.lfranken.grundgesetzlexikalibrary.R;

/**
 * Created by lfranken on 19.09.2017.
 */

/*The problem with ExpandableTextView is, that there is created a new instance every time it is
* expanded or collapsed, every time it comes to the screen again and during an Animation multiple
* times. This is why the interface implemented by the context tells us if you should expand or na*/
public class ExpandableTextView extends AppCompatTextView {
    public static final String TAG = ExpandableTextView.class.getSimpleName();

    private static final int MAX_COLLAPSED_LINES_DEFAULT = 2;
    private static final int MIN_COLLAPSED_LINES_DEFAULT = 1;
    private static final int ANIMATION_DURATION_DEFAULT = 250;
    private static final int ANIMATION_DURATION_MINIMUM = 80;

    private int maxCollapsedLines;
    private int animationDuration;
    private ExpansionListener expansionListener;
    private boolean currentlyAnimating = false;
    private Animator.AnimatorListener animatorListener;
    private View rotationArrow;

    public interface ExpansionListener {
        boolean shouldExpandableTextViewExpand();

        boolean isItemExpanded();

        void setMaxExpandLines(int maxExpandLines);

        int getMaxExpandLines();
    }

    public ExpandableTextView(Context context) {
        super(context);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet) {
        handleAttributes(context, attributeSet);
        setEllipsize(TextUtils.TruncateAt.END);
        if (maxCollapsedLines < MIN_COLLAPSED_LINES_DEFAULT) {
            maxCollapsedLines = MIN_COLLAPSED_LINES_DEFAULT;
            Log.e(TAG, "maxCollapsedLines needs to be at least " + MIN_COLLAPSED_LINES_DEFAULT +
                    ". It has been set to this value automatically.");
        }

        animationDuration = ANIMATION_DURATION_DEFAULT;

        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                currentlyAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentlyAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentlyAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    public void setExpansionListener(ExpansionListener expansionListener) {
        this.expansionListener = expansionListener;
        /*
         * This stuff is necessary for several bug fixes. ExpansionListener has to hold
         * maxExpandedLines so we can assure the same value every time it's accessed. If it's not
         * set yet, we have to set it in post when the ExpandableTextView is inflated and has its
         * lineCount set. But if it is set we have to collapse immediately because when scrolling it
         * would jump down the list if we wouldn't. We also have to expandWithoutAnimation when the
         * ETV is inflated again if it was expanded and scrolled out of and back to the screen or if
         * we return to the fragment otherwise the first collapse has no effect.
         */
        rotateExpansionArrow();
        if (!expansionListener.isItemExpanded() && !currentlyAnimating) {
            if (expansionListener.getMaxExpandLines() <= 0) {
                post(() -> {
                    setMaxExpandLines();
                    collapseWithoutAnimation();
                });
            } else
                collapseWithoutAnimation();
        } else
            expandWithoutAnimation();
    }

    private void handleAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        maxCollapsedLines = typedArray.getInteger(R.styleable.ExpandableTextView_setMaxCollapsedLines, MAX_COLLAPSED_LINES_DEFAULT);
        typedArray.recycle();
    }

    public synchronized void toggle() {
        if (currentlyAnimating || expansionListener == null) return;
        if (!expansionListener.shouldExpandableTextViewExpand()) {
            rotateExpansionArrow();
            collapse();
        } else {
            rotateExpansionArrow();
            expand();
        }
    }

    private void expand() {
        ObjectAnimator animator = ObjectAnimator.ofInt(
                this, "maxLines", expansionListener.getMaxExpandLines());
        animator.setDuration(animationDuration);
        animator.addListener(animatorListener);
        animator.start();
    }

    private void collapse() {
        setMaxExpandLines();
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "maxLines", maxCollapsedLines);
        animator.setDuration(animationDuration);
        animator.addListener(animatorListener);
        animator.start();
    }

    private void collapseWithoutAnimation() {
        setMaxLines(maxCollapsedLines);
    }

    public void expandWithoutAnimation() {
        setMaxLines(expansionListener.getMaxExpandLines());
    }

    private void setMaxExpandLines() {
        if (expansionListener.getMaxExpandLines() <= maxCollapsedLines)
            expansionListener.setMaxExpandLines(getLineCount());
        setAnimationDurationBasedOnMaxExpandedLines();
    }

    private void setAnimationDurationBasedOnMaxExpandedLines() {
        int expansionLineDelta = expansionListener.getMaxExpandLines() - maxCollapsedLines;
        animationDuration = expansionLineDelta * 40;
        if (animationDuration > ANIMATION_DURATION_DEFAULT || animationDuration <= 0)
            animationDuration = ANIMATION_DURATION_DEFAULT;
        else if (animationDuration < ANIMATION_DURATION_MINIMUM)
            animationDuration = ANIMATION_DURATION_MINIMUM;
    }

    @Override
    public void setMaxLines(int maxLines) {
        if (maxLines > 0)
            super.setMaxLines(maxLines);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null)
            super.setText(text, type);
    }

    private void rotateExpansionArrow() {
        if (rotationArrow == null) return;
        if (expansionListener.isItemExpanded())
            rotationArrow.animate().rotation(270);
        else if (expansionListener.getMaxExpandLines() > maxCollapsedLines)
            rotationArrow.animate().rotation(90);
    }

    public void setRotationArrow(View rotationArrow) {
        this.rotationArrow = rotationArrow;
    }
}
