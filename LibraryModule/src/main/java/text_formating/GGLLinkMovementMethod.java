package text_formating;

import android.graphics.RectF;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GGLLinkMovementMethod extends LinkMovementMethod {

    public interface GGLLinkMovementMethodListener {
        void handleClickedLink(String url);
    }

    private static int clickResponseColor = -1;
    private boolean isUrlHighlighted = false;
    private final RectF touchedLineBounds = new RectF();
    private URLSpan urlSpanUnderTouchOnActionDown;

    private static GGLLinkMovementMethod linkMovementMethod = new GGLLinkMovementMethod();
    private static List<GGLLinkMovementMethodListener> listeners = new ArrayList<>();

    @Override
    public boolean onTouchEvent(TextView textView, Spannable text, MotionEvent motionEvent) {

        final URLSpan urlSpanUnderTouch = findUrlSpanUnderTouch(textView, text, motionEvent);
        final boolean touchStartedOverALink = urlSpanUnderTouchOnActionDown != null;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (urlSpanUnderTouch == null) return false;
                highlightUrl(textView, urlSpanUnderTouch, text);
                urlSpanUnderTouchOnActionDown = urlSpanUnderTouch;
                return true;
            case MotionEvent.ACTION_UP:
                if (urlSpanUnderTouch != null && urlSpanUnderTouch == urlSpanUnderTouchOnActionDown)
                    notifyListeners(urlSpanUnderTouch.getURL());
                unHighlightUrl(textView);
                break;
            case MotionEvent.ACTION_CANCEL:
                unHighlightUrl(textView);
                return false;
        }
        return touchStartedOverALink;
    }


    private URLSpan findUrlSpanUnderTouch(TextView textView, Spannable text, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        final Layout layout = textView.getLayout();
        final int line = layout.getLineForVertical(y);
        final int offset = layout.getOffsetForHorizontal(line, x);

        touchedLineBounds.left = layout.getLineLeft(line);
        touchedLineBounds.top = layout.getLineTop(line);
        touchedLineBounds.right = layout.getLineRight(line);
        touchedLineBounds.bottom = layout.getLineBottom(line);

        if (touchedLineBounds.contains(x, y)) {
            URLSpan[] link = text.getSpans(offset, offset, URLSpan.class);
            if (link.length != 0)
                return link[0];
        }
        return null;
    }

    private void highlightUrl(TextView textView, URLSpan urlSpanUnderTouch, Spannable text) {
        if (isUrlHighlighted) return;
        isUrlHighlighted = true;
        final int spanStart = text.getSpanStart(urlSpanUnderTouch);
        final int spanEnd = text.getSpanEnd(urlSpanUnderTouch);
        text.setSpan(new BackgroundColorSpan(clickResponseColor), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(text);
        Selection.setSelection(text, spanStart, spanEnd);
    }

    private void unHighlightUrl(TextView textView) {
        if (!isUrlHighlighted) return;
        isUrlHighlighted = false;
        final Spannable text = (Spannable) textView.getText();
        BackgroundColorSpan[] highlightSpans = text.getSpans(0, text.length(), BackgroundColorSpan.class);
        for (BackgroundColorSpan highlightSpan : highlightSpans)
            text.removeSpan(highlightSpan);
        textView.setText(text);
        Selection.removeSelection(text);
    }

    private void notifyListeners(String url) {
        for (GGLLinkMovementMethodListener listener : listeners) {
            listener.handleClickedLink(url);
        }
    }

    public static MovementMethod getInstance() {
        return linkMovementMethod;
    }

    public static void addListener(GGLLinkMovementMethodListener listener) {
        if (listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }

    public static void setClickResponseColor(int clickResponseColor) {
        GGLLinkMovementMethod.clickResponseColor = clickResponseColor;
    }

    public static boolean removeListener(GGLLinkMovementMethodListener listener) {
        return listeners.remove(listener);
    }

    public static void removeAllListeners() {
        listeners.clear();
    }
}