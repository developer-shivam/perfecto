package com.algowire.perfecto;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import developer.shivam.perfecto.OnRequestComplete;
import developer.shivam.perfecto.Perfecto;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Perfecto.with(MainActivity.this).fromUrl("http://www.google.com").ofTypeGet().connect(new OnRequestComplete() {

                    @Override
                    public void onSuccess(String s) {
                        Log.d("Response", s);
                    }

                    @Override
                    public void onFailure(String s) {
                        Log.d("Error", s);
                    }
                });
            }
        };
        handler.postDelayed(runnable, 1000);

    }
}
