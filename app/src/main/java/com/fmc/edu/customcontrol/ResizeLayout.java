package com.fmc.edu.customcontrol;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Candy on 2015/6/3.
 */
public class ResizeLayout extends LinearLayout {

    private final int CHANGE_SIZE = 100;

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//		Log.i("demo", "w :" + w);
//		Log.i("demo", "h :" + h);
//		Log.i("demo", "oldw :" + oldw);
//		Log.i("demo", "oldh :" + oldh);

        if (oldw == 0 || oldh == 0)
            return;

        if (boardListener != null) {
            boardListener.keyBoardStatus(w, h, oldw, oldh);
            if (oldw != 0 && h - oldh < -CHANGE_SIZE) {
                boardListener.keyBoardVisable(Math.abs(h - oldh));
            }

            if (oldw != 0 && h - oldh > CHANGE_SIZE) {
                boardListener.keyBoardInvisable(Math.abs(h - oldh));
            }
        }
    }

    public interface SoftkeyBoardListener {
        public void keyBoardStatus(int w, int h, int oldw, int oldh);

        public void keyBoardVisable(int move);

        public void keyBoardInvisable(int move);
    }

    SoftkeyBoardListener boardListener;

    public void setSoftKeyBoardListener(SoftkeyBoardListener boardListener) {
        this.boardListener = boardListener;
    }
}