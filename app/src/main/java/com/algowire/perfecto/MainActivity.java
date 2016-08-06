package com.algowire.perfecto;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import developer.shivam.perfecto.OnRequestComplete;
import developer.shivam.perfecto.Perfecto;

public class MainActivity extends AppCompatActivity {

    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Perfecto.with(context)
                .fromUrl("https://www.google.co.in")
                .ofTypeGet()
                .connect(new OnRequestComplete() {

                    @Override
                    public void onSuccess(String response) {
                        Log.d("Response", response);
                    }

                    @Override
                    public void onFailure(String error) {
                        /**
                         *  This will return the errorStream(), responseCode and responseMessage
                         *  Use Log to get the error.
                         */

                    }
                });

    }
}
