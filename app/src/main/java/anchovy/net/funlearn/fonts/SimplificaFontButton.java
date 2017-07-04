package anchovy.net.funlearn.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by DarKnight98 on 4/23/2017.
 */

public class SimplificaFontButton extends Button {
    public SimplificaFontButton(Context context) {
        super(context);
        init();
    }

    public SimplificaFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimplificaFontButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimplificaFontButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SIMPLIFICA Typeface.ttf");
            setTypeface(tf);
        }
    }
}
