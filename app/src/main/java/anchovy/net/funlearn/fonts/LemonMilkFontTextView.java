package anchovy.net.funlearn.fonts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.TextView;

import anchovy.net.funlearn.R;

/**
 * Created by DarKnight98 on 3/14/2017.
 */

public class LemonMilkFontTextView extends android.support.v7.widget.AppCompatTextView {
    public LemonMilkFontTextView(Context context) {
        super(context);
        init();
    }

    public LemonMilkFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LemonMilkFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/LemonMilk.otf");
            setTypeface(tf);
        }
    }
}
