package com.micdm.nobadhabits.misc;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.micdm.nobadhabits.R;

public class KernedTextView extends TextView {

    private static final String NON_BREAKING_SPACE = "\u00a0";
    private static final float DEFAULT_LETTER_SPACING = 1;

    private final float letterSpacing;

    public KernedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        letterSpacing = getLetterSpacing(context, attrs);
    }

    private float getLetterSpacing(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KernedTextView, 0, 0);
        try {
            return a.getFloat(R.styleable.KernedTextView_letterSpacing, DEFAULT_LETTER_SPACING);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        for (int i = text.length() - 1; i >= 1; i -= 1) {
            builder.insert(i, NON_BREAKING_SPACE);
            builder.setSpan(new ScaleXSpan(letterSpacing), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.setText(builder, type);
    }
}
