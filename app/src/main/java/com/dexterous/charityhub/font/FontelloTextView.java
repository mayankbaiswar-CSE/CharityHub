package com.dexterous.charityhub.font;

/**
 * Created by mayank on 14-04-2015.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontelloTextView
        extends TextView
{
    private static Typeface sFontello;

    public FontelloTextView(Context paramContext)
    {
        super(paramContext);
        if (isInEditMode()) {
            return;
        }
        setTypeface();
    }

    public FontelloTextView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        if (isInEditMode()) {
            return;
        }
        setTypeface();
    }

    public FontelloTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        if (isInEditMode()) {
            return;
        }
        setTypeface();
    }

    private void setTypeface()
    {
        if (sFontello == null) {
            sFontello = Typeface.createFromAsset(getContext().getAssets(), "fonts/Fontello.ttf");
        }
        setTypeface(sFontello);
    }
}
