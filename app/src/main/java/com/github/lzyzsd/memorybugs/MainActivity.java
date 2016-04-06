package com.github.lzyzsd.memorybugs;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView sTextView;
    private Button mStartBButton;
    private Button mStartAllocationButton;
    Toast mToast ;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
    private Rect mRect = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sTextView = (TextView) findViewById(R.id.tv_text);
        sTextView.setText("Hello World!");

        mStartBButton = (Button) findViewById(R.id.btn_start_b);
        mStartBButton.setOnClickListener(this);

        mStartAllocationButton = (Button) findViewById(R.id.btn_allocation);
        mStartAllocationButton.setOnClickListener(this);

        mToast =  Toast.makeText(this,"",Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_b:
                startB();
                break;
            case R.id.btn_allocation:
                startAllocationLargeNumbersOfObjects();
                break;
        }
    }

    private void startB() {
        finish();
        startActivity(new Intent(this, ActivityB.class));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("post delayed may leak");
            }
        }, 5000);
        Toast.makeText(this, "请注意查看通知栏LeakMemory", Toast.LENGTH_SHORT).show();
    }

    private void startAllocationLargeNumbersOfObjects() {
        mToast.setText("请注意查看MemoryMonitor 以及AllocationTracker");
        mToast.show();
        for (int i = 0; i < 10000; i++) {
            if (mRect == null) {          //避免创建大量的 mRect
                mRect = new Rect(0, 0, 100, 100);
            } else {
                mRect.right = 100;
                mRect.bottom = 100;
            }
            System.out.println("-------: " + mRect.width());
        }
    }
}
