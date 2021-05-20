package com.example.handler_one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Handler handler2,handler1;
    private TextView tv_one;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_one = findViewById(R.id.tv_one);
        /**
         * 主线程中的handler，将子线程的数据传入主线程
         */
        handler1 = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    tv_one.setText("第一个按钮改变的文本");
                }else if (msg.what==2){
                    tv_one.setText("第二个按钮改变的文本");
                }else if (msg.what==3){
                    String s = "msg1="+msg.arg1;
                    Log.e("TAG",s);
                }
            }
        };
        /**
         * 子线程中的handler，将主线程数据传入子线程
         */
        new Thread(){
            @Override
            public void run() {
                super.run();
                //子线程中需要自己开启Looper
                Looper.prepare();
                handler2 = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.e("TAG","msg.what="+msg.what);
                    }
                };
                //循环相当于产生了一个while（true）{...}
                Looper.loop();
            }
        }.start();
    }



    /**
     * 三个按钮的点击事件
     * @param view
     */
    public void myclick(View view) {
        switch (view.getId()){
            //点击按钮在子线程中处理主线程的操作
            case R.id.btn_one:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        //handler.sendEmptyMessage(1);
                        Log.e("TAG","你点击了第一个按钮");
                        handler1.sendEmptyMessage(1);
                    }
                }.start();
                break;
                //点击按钮在子线程中处理主线程的操作
            case R.id.btn_two:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Log.e("TAG","你点击了第二个按钮");
                        handler1.sendEmptyMessage(2);
                    }
                }.start();
                break;
                //把子线程中的数据传入到主线程
            case R.id.btn_three:
                Log.e("TAG","你点击了第三个按钮");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message str = new Message();
                        str.what = 3;
                        str.arg1 = 3;
                        handler1.sendMessage(str);
                    }
                }.start();
                break;
                //将主线程的数据传入到子线程
            case R.id.btn_four:
                Log.e("TAG","你点击了第四个按钮");
                handler2.sendEmptyMessage(1000);
                break;
        }
    }
}