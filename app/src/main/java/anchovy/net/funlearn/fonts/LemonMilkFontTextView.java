package anchovy.net.funlearn.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DarKnight98 on 3/14/2017.
 */

public class LemonMilkFontTextView extends TextView {
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
