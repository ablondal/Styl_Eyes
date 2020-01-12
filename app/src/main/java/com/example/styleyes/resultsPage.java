package com.example.styleyes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class resultsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);
        JSONAsyncTask j = MainActivity.getAsyncTask();
        getSupportActionBar().hide();
    }
}


class getFashionAdvice {

    String[] tags;

    public void setTags(String[] t) {
        tags = t;
    }

    public String FashionEyes() {
        // In here, the tags are analyzed, and a suggestion string is given

        // Tags as a List Object
        List<String> tag_list = Arrays.asList(tags);

        // Output string
        String advice = "";
        String accessory = "";

        // Start the testing

        // Requirements
        boolean colacc = false;
        boolean lightshoe = false;

        // Number of patterns
        int patterncount = 0;
        if ( tag_list.contains("Checkered") ) patterncount++;
        if ( tag_list.contains("dots") ) patterncount++;
        if ( tag_list.contains("floral") ) patterncount++;
        if ( tag_list.contains("stripes") ) patterncount++;
        if ( tag_list.contains("knit") ) patterncount++;
        if (patterncount > 1) {
            advice += "Several different types of patterns detected.\nProceed with caution.\n";
        }

        if (tag_list.contains("black") ||
            tag_list.contains("white") ||
            tag_list.contains("grey") ||
            tag_list.contains("brown") ) {
            advice += "A neutral colour was detected. Consider adding any colour accent, or a patterned accessory!\n";
            colacc = true;
        }

        if (tag_list.contains("sleeveless") || tag_list.contains("tshirt")) {
            advice += "A short or no-sleeve top was detected. Wear light shoes, such as heels, sandals, or sneakers.\n";
            lightshoe = true;
        }

        List<String> possiblecols = new ArrayList<>();
        possiblecols.add("blue");
        possiblecols.add("green");
        possiblecols.add("orange");
        possiblecols.add("pink");
        possiblecols.add("purple");
        possiblecols.add("red");
        possiblecols.add("yellow");


        if (tag_list.contains("blue") || tag_list.contains("green")) {
            possiblecols.remove("pink");
            possiblecols.remove("red");
            possiblecols.remove("orange");
        }
        if (tag_list.contains("orange")) {
            possiblecols.remove("purple");
            possiblecols.remove("green");
            possiblecols.remove("pink");
        }
        if (tag_list.contains("pink")) {
            possiblecols.remove("blue");
            possiblecols.remove("green");
            possiblecols.remove("yellow");
        }
        if (tag_list.contains("pink")) {
            possiblecols.remove("blue");
            possiblecols.remove("orange");
        }
        if (tag_list.contains("purple")) {
            possiblecols.remove("orange");
            possiblecols.remove("green");
            possiblecols.remove("red");
        }
        if (tag_list.contains("red")) {
            possiblecols.remove("blue");
            possiblecols.remove("green");
            possiblecols.remove("red"); // too much red
        }
        if (tag_list.contains("yellow")) {
            possiblecols.remove("yellow");
        }

        if ( possiblecols.size() > 0 ) {
            advice += "Based on your current colours, we recommend choosing something with one of these colours:\n";
            for (int i=0; i<possiblecols.size(); ++i){
                advice += possiblecols.get(i);
                if (i != possiblecols.size()-1) advice+= ", ";
                else advice += "\n";
            }
        }else{
            advice += "We recommend adding a neutral colour, since you seem to be so colourful already!\n";
        }

        if (colacc && possiblecols.size()==0) {
            accessory = "scarf1";
        }else if (lightshoe) {
            if ( possiblecols.size() > 2 ) {
                accessory = "shoes3";
            }else{
                accessory = "shoes1";
            }
        }else if (patterncount==0) {
            if (possiblecols.contains("red")) {
                accessory = "floral";
            }else{
                accessory = "floral2";
            }
        }else if (possiblecols.size()==0) {
            accessory = "greyhat";
        }else{
            Random rand = new Random();
            String col = possiblecols.get(rand.nextInt(possiblecols.size()));
            if (col=="blue") accessory = "bluecap";
            else if (col=="green") accessory = "beigehat";
            else if (col=="yellow") accessory = "yellowhat";
            else if (col=="orange") accessory = "redhat";
            else if (col=="red") accessory = "redcap";
            else if (col=="purple") accessory = "shoes5";
            else if (col=="pink") accessory = "pinkcap";
        }


        return accessory + ":" + advice;
    }

}














