package com.dexterous.charityhub.font;

/**
 * Created by mayank on 14-04-2015.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoTextView
        extends TextView
{
    private static Typeface sRoboto;

    public RobotoTextView(Context paramContext)
    {
        super(paramContext);
        if (isInEditMode()) {
            return;
        }
        setTypeface();
    }

    public RobotoTextView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        if (isInEditMode()) {
            return;
        }
        setTypeface();
    }

    public RobotoTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        if (isInEditMode()) {
            return;
        }
        setTypeface();
    }

    private void setTypeface()
    {
        if (sRoboto == null) {
            sRoboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Thin.ttf");
        }
        setTypeface(sRoboto);
    }
}
