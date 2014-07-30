package codepath.watsiapp.customview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Custom EditText that displays a fixed prefix in line with the text.
 * The trick here is to draw the prefix as a drawable and attach it via
 * setCompoundDrawables().
 */
public class PrefixedEditText extends EditText {

    private ColorStateList mPrefixTextColor;

    public PrefixedEditText(Context context) {
        this(context, null);
    }

    public PrefixedEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PrefixedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPrefixTextColor = getTextColors();
    }

    public void setPrefix(String prefix) {
        setCompoundDrawables(new TextDrawable(prefix), null, null, null);
    }
    
    public void setPrefixTextColor(int color) {
        mPrefixTextColor = ColorStateList.valueOf(color);
    }
    
    public void setPrefixTextColor(ColorStateList color) {
        mPrefixTextColor = color;
    }

    private class TextDrawable extends Drawable {
        private String mText = "";

        public TextDrawable(String text) {
            mText = text;
            setBounds(0, 0, (int) getPaint().measureText(mText) + 2, (int) getTextSize());
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = getPaint();
            paint.setColor(mPrefixTextColor.getColorForState(getDrawableState(), 0));
            int lineBaseline = getLineBounds(0, null);
            canvas.drawText(mText, 0, canvas.getClipBounds().top + lineBaseline, paint);
        }

        @Override
        public void setAlpha(int alpha) {/* Not supported */}

        @Override
        public void setColorFilter(ColorFilter colorFilter) {/* Not supported */}

        @Override
        public int getOpacity() {
            return 1;
        }
    }
}
