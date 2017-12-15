package com.example.lfranken.grundgesetzlexikalibrary.custom_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.AttributeSet;
import android.view.Gravity;

import com.example.lfranken.grundgesetzlexikalibrary.R;

public class Button extends AppCompatButton {

    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private static final int VERTICAL_WITHOUT_FRAME = 2;
    private static final int HORIZONTAL_WITHOUT_FRAME = 3;
    private static final int TEXT = 4;
    private static final int TEXT_LEFT = 5;
    private static final int HORIZONTAL_CENTER = 6;
    private static final int HORIZONTAL_CENTER_WITHOUT_FRAME= 7;
    private int style = TEXT;
    private boolean smallText = false;

    public Button(Context context) {
        super(context);
    }

    public Button(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        setStandardLayout();
        handleAttributes(context, attrs);
    }

    private void setStandardLayout(){
        setBackgroundResource(R.drawable.button);
        setAllCaps(false);
        setPadding((int) getResources().getDimension(R.dimen.button_padding_left),
                (int) getResources().getDimension(R.dimen.button_padding_top),
                (int) getResources().getDimension(R.dimen.button_padding_right),
                (int) getResources().getDimension(R.dimen.button_padding_bottom));
        setClickable(true);
        //apparently calling setTextAppearance twice has no effect on the second call
    }

    private void handleAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Button);
        int drawableId = typedArray.getResourceId(R.styleable.Button_setIcon, R.drawable.ic_lexikon_36);
        style = typedArray.getInt(R.styleable.Button_setStyle, HORIZONTAL);
        smallText = typedArray.getBoolean(R.styleable.Button_smallText, false);
        typedArray.recycle();
        setSpecificLayout(context, style, drawableId);
    }

    private void setSpecificLayout(Context context, int style, int drawableId) {
        switch (style){
            case HORIZONTAL:
                setHorizontalLayout(context,drawableId);
                break;
            case VERTICAL:
                setVerticalLayout(context, drawableId);
                break;
            case TEXT:
                setTextLayout(context);
                break;
            case HORIZONTAL_CENTER:
                setHorizontalLayout(context, drawableId);
                setPadding((int) getResources().getDimension(R.dimen.button_padding_left_large),
                        (int) getResources().getDimension(R.dimen.button_padding_top),
                        (int) getResources().getDimension(R.dimen.button_padding_right_large),
                        (int) getResources().getDimension(R.dimen.button_padding_bottom));
                break;
            case HORIZONTAL_CENTER_WITHOUT_FRAME:
                setHorizontalWithoutFrameLayout(context, drawableId);
                setPadding((int) getResources().getDimension(R.dimen.button_padding_left_large),
                        (int) getResources().getDimension(R.dimen.button_padding_top),
                        (int) getResources().getDimension(R.dimen.button_padding_right_large),
                        (int) getResources().getDimension(R.dimen.button_padding_bottom));
                break;
            case TEXT_LEFT:
                setTextLeftLayout(context);
                break;
            case VERTICAL_WITHOUT_FRAME:
                setVerticalWithoutFrameLayout(context, drawableId);
                break;
            case HORIZONTAL_WITHOUT_FRAME:
            default:
                setHorizontalWithoutFrameLayout(context, drawableId);

        }
    }

    private void setHorizontalLayout(Context context, int drawableId){
        setTextAppearance(context, R.style.Grundgesetz_Text_Button);
        setPadding((int) getResources().getDimension(R.dimen.button_horizontal_padding_left),
                0,
                (int) getResources().getDimension(R.dimen.button_horizontal_padding_right),
                0);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        setIconDrawable(context, drawableId, true);
        setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.button_icon_padding));
    }

    private void setVerticalLayout(Context context, int drawableId){
        setTextAppearance(context, R.style.Grundgesetz_Text_Button);
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        setIconDrawable(context, drawableId, false);
    }

    private void setTextLayout(Context context){
        setTextAppearance(context, R.style.Grundgesetz_Text_Button);
        setGravity(Gravity.CENTER);
        setPadding((int)getResources().getDimension(R.dimen.layout_common_margin),
                0,
                (int)getResources().getDimension(R.dimen.layout_common_margin),
                0);
    }

    private void setTextLeftLayout(Context context) {
        setTextAppearance(context, R.style.Grundgesetz_Text_Button);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding((int) getResources().getDimension(R.dimen.button_textLeft_padding),
                0,
                (int) getResources().getDimension(R.dimen.button_textLeft_padding),
                0);
    }

    private void setVerticalWithoutFrameLayout(Context context, int drawableId) {
        setBackgroundResource(R.drawable.button_without_frame);
        setTextAppearance(context, R.style.Grundgesetz_Text_Button);
        if (smallText) setTextAppearance(context, R.style.Grundgesetz_Text_TabMenu_VerticalButtonWithoutFrame);
        setIconDrawable(context, drawableId, false);
        setGravity(Gravity.CENTER);
//        setPadding(0,0,0,0);
        setCompoundDrawablePadding(
                (int) getResources().getDimension(R.dimen.button_verticalWithoutFrame_textPadding));
    }

    private void setHorizontalWithoutFrameLayout(Context context, int drawableId){
        setBackgroundResource(R.drawable.button_without_frame);
        setTextAppearance(context, R.style.Grundgesetz_Text_Button);
        setPadding((int) getResources().getDimension(R.dimen.button_horizontal_padding_left),
                0,
                (int) getResources().getDimension(R.dimen.button_horizontal_padding_right),
                0);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        setIconDrawable(context, drawableId, true);
        setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.button_icon_padding));
    }

    private void setIconDrawable(Context context, int drawableId, boolean horizontal){
        if (drawableId != -1) {
            @SuppressLint("RestrictedApi") Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            if (horizontal) setCompoundDrawables(null, null, drawable, null);
            else setCompoundDrawables(null, drawable, null, null);
        }
    }

    public void setNewIconDrawable(Context context, int drawableId){
        if (style == HORIZONTAL) setIconDrawable(context, drawableId, true);
        else if (style == VERTICAL || style == VERTICAL_WITHOUT_FRAME)
            setIconDrawable(context, drawableId, false);
    }

}
