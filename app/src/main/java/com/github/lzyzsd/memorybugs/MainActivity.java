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

    private TextView mTextView;//去掉static
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
    private StringBuffer mStringBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tv_text);
        mTextView.setText("Hello World!");

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
        mToast.setText("请注意查看通知栏LeakMemory");
        mToast.show();
    }

    private void startAllocationLargeNumbersOfObjects() {
        mToast.setText("请注意查看MemoryMonitor 以及AllocationTracker");
        mToast.show();
        mStringBuffer = new StringBuffer("-------: ");
        for (int i = 0; i < 10000; i++) {
            if (mRect == null) {          //避免创建大量的 mRect
                mRect = new Rect(0, 0, 100, 100);
            } else {
                mRect.right = 100;
                mRect.bottom = 100;
            }
            mStringBuffer.append(mRect.width()); //避免创建大量的String
            System.out.println(mStringBuffer);
            mStringBuffer.delete(9, mStringBuffer.length());
        }
    }
}
