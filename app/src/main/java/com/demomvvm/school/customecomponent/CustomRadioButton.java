package com.demomvvm.school.customecomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.demomvvm.school.R;
import com.demomvvm.school.util.FontCache;


public class CustomRadioButton extends AppCompatRadioButton {

    private Typeface fontType;

    /**
     * Set the font according to style
     *
     * @param context context
     * @param attrs   style attribute like bold, italic
     */


    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode())
            return;
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontName = ta.getString(R.styleable.CustomTextView_font_name);
        if (fontName != null) {
            final Typeface font = FontCache.getInstance().getFont(context, "fonts/" + fontName);
            setTypeface(font);
        }
        ta.recycle();
    }


}