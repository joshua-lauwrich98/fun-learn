package anchovy.net.funlearn.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by DarKnight98 on 3/14/2017.
 */

public class SimplificaFontEditText extends EditText {
    public SimplificaFontEditText(Context context) {
        super(context);
        init();
    }

    public SimplificaFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimplificaFontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SIMPLIFICA Typeface.ttf");
            setTypeface(tf);
        }
    }
}
