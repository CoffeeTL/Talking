package com.tl.coffee.talking.view.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tl.coffee.talking.R;


/**
 * Created by coffee on 2017/8/15.
 */

public class AnswerToggleBtn extends RelativeLayout implements View.OnTouchListener {
    private ImageView toggleBtn;
    private RelativeLayout main;
    private int parentWidth;
    private LayoutParams params;
    public AnswerToggleBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.answer_toggle_btn,this);
        toggleBtn = (ImageView) findViewById(R.id.answer_toggle_btn_main_toggleiv);
        main = (RelativeLayout) findViewById(R.id.answer_toggle_btn_main);
        initView(context);
    }
    public AnswerToggleBtn(Context context) {
        super(context);
    }

    int togglewidth;
    int maxMargin;
    int leftMargin;
    int rightMargin;
    private void initView(Context context){
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        main.measure(w, h);
        parentWidth = main.getMeasuredWidth();

        toggleBtn.measure(w,h);
        togglewidth = toggleBtn.getMeasuredWidth();

        Log.i("pmbar","parentwidth = "+parentWidth+" toggleWidth = "+togglewidth);
        params = (LayoutParams) toggleBtn.getLayoutParams();
        maxMargin = (parentWidth-togglewidth)/2;
        leftMargin = maxMargin;
        rightMargin = maxMargin;
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        toggleBtn.setLayoutParams(params);
        toggleBtn.setOnTouchListener(this);
    }
    float xRaw;
    float offsetX = 0;
    boolean isInLeft;
    boolean isInRight;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xRaw = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float cur_x = event.getRawX();
                offsetX = cur_x-xRaw;
                calculateOffset();
                break;
            case MotionEvent.ACTION_UP:
                isInLeft = false;
                isInRight = false;
                offsetX = 0;
                leftMargin = maxMargin;
                rightMargin = maxMargin;
                params.leftMargin = leftMargin;
                params.rightMargin = rightMargin;
                toggleBtn.setLayoutParams(params);
                break;
        }
        return true;
    }

    private void calculateOffset() {
        float left = maxMargin + offsetX;
        float right = maxMargin - offsetX;
        if(left < 0){
            left = 0;
            right = 2 * maxMargin;
            if(!isInLeft){
                isInLeft = true;
                listener.onPickup();
            }
        }else if(right < 0){
            left = 2 * maxMargin;
            right = 0;
            if(!isInRight){
                isInRight = true;
                listener.onReject();
            }
        }

        leftMargin  = (int) left;
        rightMargin = (int) right;
        params.leftMargin = leftMargin;
        params.rightMargin = rightMargin;
        toggleBtn.setLayoutParams(params);
    }
    public interface ToggleListener{
        void onPickup();
        void onReject();
    }
    private ToggleListener listener;
    public void setOnToggleListener(ToggleListener listener){
        this.listener = listener;
    }
}
