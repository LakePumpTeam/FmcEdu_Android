package com.fmc.edu.customcontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.fmc.edu.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Candy on 2015/5/4.
 */
public class ValidateButtonControl extends Button {

    Timer timer = new Timer();
    int timeSpan = 60;
    int passTime = 0;
    private OnValidatedListener onValidatedListener;
    private OnStartingCountdownListener startingCountdownListener;

    public interface OnValidatedListener {
        void onPreValidate();

        void onValidated();
    }

    public interface OnStartingCountdownListener {
        void onStartingCountdown();
    }

    public ValidateButtonControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);
        this.setOnClickListener(validateOnClick);
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ValidateButtonControl);
        timeSpan = typedArray.getInt(R.styleable.ValidateButtonControl_repeatSendTime, 60);
    }

    public void startCountdown() {
        if (startingCountdownListener != null) {
            startingCountdownListener.onStartingCountdown();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.arg1 = passTime == timeSpan ? 1 : 0;
                handler.sendMessage(msg);
            }
        }, 0, 1000);
    }

    private View.OnClickListener validateOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            passTime = 0;
            if (null != onValidatedListener) {
                onValidatedListener.onPreValidate();
            }
        }
    };

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                setText("重发");
                setEnabled(true);
                timer.cancel();
                if (null != onValidatedListener) {
                    onValidatedListener.onValidated();
                }
                return true;
            }
            setEnabled(false);
            String text = timeSpan - passTime + "s";
            setText(text);
            passTime++;
            return false;
        }
    });

    public void setOnValidatedListener(OnValidatedListener onValidatedListener) {
        this.onValidatedListener = onValidatedListener;
    }

    public void setStartingCountdownListener(OnStartingCountdownListener startingCountdownListener) {
        this.startingCountdownListener = startingCountdownListener;
    }
}
