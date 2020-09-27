package com.fonekey.mainpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

public class CCalendarViewScrollable extends CalendarView {

    public CCalendarViewScrollable(@NonNull Context context) {
        super(context);
    }

    /*public CCalendarViewScrollable(Context context) {
        super(context);
    }

    public CCalendarViewScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CCalendarViewScrollable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }*/

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }

}