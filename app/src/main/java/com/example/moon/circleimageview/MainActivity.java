package com.example.moon.circleimageview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private int mScreenWidth;
    private int mScreenHeight;

    private RelativeLayout rL;
    private Long time;

    private int lastX,lastY;
    private long durTime = 0;
    private long downTime = 0;
    private boolean isConsumed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleImageView = (CircleImageView) findViewById(R.id.circleImage);
        rL = (RelativeLayout) findViewById(R.id.activity_main);
        init();
        doStartAnimation();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击事件触发",Toast.LENGTH_SHORT).show();
            }
        });

        circleImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


               switch (event.getAction()){
                   case MotionEvent.ACTION_DOWN:
                       lastX = (int) event.getRawX();
                       lastY = (int) event.getRawY();
                       downTime =System.currentTimeMillis();

                      break;
                   case MotionEvent.ACTION_MOVE:

                       int dx = (int) (event.getRawX()-lastX);
                       int dy = (int)  (event.getRawY()-lastY );
                       float nextY = circleImageView.getY() + dy;
                       float nextX = circleImageView.getX() + dx;

                       //不能移出屏幕
                       if (nextY < 0) {
                           nextY = 0;
                       } else if (nextY > mScreenHeight - circleImageView.getHeight()) {
                           nextY = mScreenHeight - circleImageView.getHeight();
                       }
                       if (nextX < 0)
                           nextX = 0;
                       else if (nextX > mScreenWidth - circleImageView.getWidth())
                           nextX = mScreenWidth - circleImageView.getWidth();


                       // 属性动画移动
                       ObjectAnimator y = ObjectAnimator.ofFloat(circleImageView, "y", circleImageView.getY(), nextY);
                       ObjectAnimator x = ObjectAnimator.ofFloat(circleImageView, "x", circleImageView.getX(), nextX);

                       AnimatorSet animatorSet = new AnimatorSet();
                       animatorSet.playTogether(x, y);
                       animatorSet.setDuration(0);
                       animatorSet.start();

                       lastX = (int) event.getRawX();
                       lastY = (int) event.getRawY();

                       break;
                   case MotionEvent.ACTION_UP:

                       durTime = System.currentTimeMillis()- downTime;
                       Log.e("~~~","downTime = "+ downTime+"____durTime = "+durTime);

                       if(durTime > 500){
                           return true;
                       }
                       break;


               }
                return false;
            }
        });

    }



    private void init(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight=metrics.heightPixels;


    }

    private void doStartAnimation(){
       // AnimationSet animationSet = new AnimationSet(false);
        TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,1
                ,Animation.ABSOLUTE,circleImageView.getLeft()
                ,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        trans.setDuration(3000);
        trans.setInterpolator(new BounceInterpolator());
        circleImageView.startAnimation(trans);
    }

}
