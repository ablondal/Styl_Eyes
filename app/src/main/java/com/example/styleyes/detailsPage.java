package com.example.styleyes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class detailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);
        getSupportActionBar().hide();

        JSONAsyncTask j = new JSONAsyncTask(MainActivity.context());
        int numhat = j.getAccessoriesCount("hat");
        int numscarves = j.getAccessoriesCount("neckwear");
        int numshoes = j.getAccessoriesCount("shoes");
        TextView tv1 = (TextView)findViewById(R.id.text1);
        tv1.setText("You have " + numhat + " hats, " + numscarves + " scarves and " + numshoes + " pairs of shoes.");

        TextView tv2 = (TextView)findViewById(R.id.text2);
        tv2.setText("Approximately " + (numhat + numscarves + numshoes)*2700 + " liters of water was used.");

        TextView tv3 = (TextView)findViewById(R.id.text3);
        tv3.setText("Approximately " + (numhat + numscarves + numshoes)*11 + " kg of carbon dioxide was produced.");


        final Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d = new Intent(detailsPage.this, MainActivity.class);
                startActivity(d);
            }
        });
    }


}
