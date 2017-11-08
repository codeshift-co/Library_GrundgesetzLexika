package text_formating;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GGLLinkMovementMethod extends LinkMovementMethod {
    public static final String TAG = GGLLinkMovementMethod.class.getSimpleName();

    public interface GGLLinkMovementMethodListener {
        void handleClickedLink(String url);
    }

    private static GGLLinkMovementMethod linkMovementMethod = new GGLLinkMovementMethod();
    private static List<GGLLinkMovementMethodListener> listeners = new ArrayList<>();

    @Override
    public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent){
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_UP){
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int offset = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = spannable.getSpans(offset, offset, URLSpan.class);
            if (link.length != 0){
                String url = link[0].getURL();
                notifyListeners(url);
                return true;
            }
        }
        return super.onTouchEvent(textView, spannable, motionEvent);
    }

    private void notifyListeners(String url){
        for (GGLLinkMovementMethodListener listener : listeners){
            listener.handleClickedLink(url);
        }
    }

    public static MovementMethod getInstance(){
        return linkMovementMethod;
    }

    public static void addListener (GGLLinkMovementMethodListener listener){
        if (listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }

    public static boolean removeListener(GGLLinkMovementMethodListener listener){
        return listeners.remove(listener);
    }

    public static void removeAllListeners(){
        listeners = new ArrayList<>();
    }
}
