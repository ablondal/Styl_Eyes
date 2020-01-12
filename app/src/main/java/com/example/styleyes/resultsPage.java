package com.example.styleyes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class resultsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);
        JSONAsyncTask j = MainActivity.getAsyncTask();
    }
}


class getFashionAdvice {


}