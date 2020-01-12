package com.example.styleyes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

//import com.google.common.io.ByteStreams;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final String AZURE_CUSTOMVISION_PREDICTION_API_KEY = "8d1da701ca294e828465a5961290fb4d";
    static final String AZURE_CUSTOMVISION_ENDPOINT = "https://francisca-prediction.cognitiveservices.azure.com/";

    static final String Endpoint = "https://francisca.cognitiveservices.azure.com/";
    static String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //Buttons
        //takes user to details page
        final ImageButton detailButton = findViewById(R.id.detailButton);
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, detailsPage.class);
                startActivity(a);
            }
        });

        //takes user to add accessories
        final ImageButton plusButton = findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(MainActivity.this, plusPage.class);
                startActivity(b);

            }
        });


    }

    /** Called when the user clicks the "Take photo button" **/
    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            System.out.println("Inside the if statement");
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                System.out.println("Inside the second if statement");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        System.out.println("Before Async");
        /*JSONAsyncTask j = new JSONAsyncTask();
        j.execute();*/
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Printing on result");
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            JSONAsyncTask j = new JSONAsyncTask();
            j.execute();

            //shows results page
            Intent d = new Intent(MainActivity.this, resultsPage.class);
            startActivity(d);

        }
    }

}


class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... urls) {
        try {

            byte[] testImage = GetImageAsByteArray();

            String url = "https://francisca.cognitiveservices.azure.com/customvision/v3.0/Prediction/e5a5b590-a74a-4f0f-aeda-d2275684c8e4/detect/iterations/Iteration5/image";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("Prediction-Key", "8d1da701ca294e828465a5961290fb4d");
            con.setRequestProperty("Content-Type", "application/octet-stream");

            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(
                    con.getOutputStream());

            out.write(testImage);
            out.flush();

            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                JSONObject stupidJson;
                JSONArray predictions;
                HashSet<String> labels = new HashSet<>();
                // System.out.println(response.toString());
                try {

                    stupidJson = new JSONObject(response.toString());
                    predictions = stupidJson.getJSONArray("predictions");

                    for (int i = 0; i < predictions.length(); i++){
                        labels.add(predictions.getJSONObject(i).getString("tagName"));
                        System.out.println(predictions.getJSONObject(i).getString("tagName"));
                    }


                } catch (Throwable t) {


                }
            } else {
                System.out.println("Could not connect to cloud, unable to provide feedback at this time.");

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result




            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private byte[] GetImageAsByteArray() {
        System.out.println("fetching image as byte array from file system");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(MainActivity.currentPhotoPath, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    protected void onPostExecute(Boolean result) {
        System.out.println("done?");
    }

    private int getAccessoriesCount(String piece) {
        JSONObject stupidJson;
//        JSONArray shoes;
//        JSONArray neckwear;
//        JSONArray hat;
        JSONArray pieceArray;
        File file = new File("accessories.json");
        try {
            Scanner myReader = new Scanner(file);
            String buffer = "";
            while (myReader.hasNextLine()) {
                buffer = myReader.nextLine();
            }
            try {
                stupidJson = new JSONObject(buffer);
//                shoes = stupidJson.getJSONArray("shoes");
//                neckwear = stupidJson.getJSONArray("neckwear");
//                hat = stupidJson.getJSONArray("hat");
                pieceArray = stupidJson.getJSONArray(piece);
                return pieceArray.length();
            } catch (Throwable t) {

            }

        } catch (FileNotFoundException e){

        }
        return 0;

    }
    private ArrayList<String> analyze(HashSet<String> tags) {
        HashSet<String> colors = new HashSet<>();
        colors.add("black");
        colors.add("blue");
        colors.add("brown");
        colors.add("green");
        colors.add("grey");
        colors.add("orange");
        colors.add("pink");
        colors.add("purple");
        colors.add("red");
        colors.add("white");
        colors.add("yellow");

        HashSet<String> patterns = new HashSet<>();
        patterns.add("knit");
        patterns.add("stripes");
        patterns.add("Checkered");
        patterns.add("dots");
        patterns.add("floral");

        HashSet<String> warmth = new HashSet<>();
        warmth.add("long sleeve");
        warmth.add("sleeveless");
        warmth.add("jacket");
        warmth.add("knit");

        String colorAdvice;
        String patternAdvice;
        String warmthAdvice;

        Iterator<String> i = tags.iterator();
        while (i.hasNext()) {
//            i.next()
        }


        return null;
    }
}