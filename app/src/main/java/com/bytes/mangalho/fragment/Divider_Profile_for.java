package com.bytes.mangalho.fragment;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.bytes.mangalho.R;

public class Divider_Profile_for extends DividerItemDecoration {

    public Divider_Profile_for(Context context, int orientation) {
        super(context, orientation);
        setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_profile_for));
    }
}
