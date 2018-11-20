package com.karldenby.dice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;

import java.util.Random;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;


public class MainActivity extends AppCompatActivity {

    // Variable for widget references
    Button bRoll;
    ImageView imgDieOne, imgDieTwo;
    TextView txtResult;

    // Variables for game mechanics
    int die1, die2, total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is an attempt to only create everything once
        if (savedInstanceState == null) {
            setupWidgets();
            if (total == 0) {
                bRoll.callOnClick();    // Roll the dice so we don't start with 0 :)
            }
            txtResult.setText(getString(R.string.Result, total));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupWidgets();

        // Create the default Stitch Client --------------------------------------------------------
        final StitchAppClient client;

        if (Stitch.hasAppClient("dice-mgatp")) {
            client = Stitch.getDefaultAppClient();
        } else {
            client = Stitch.initializeDefaultAppClient("dice-mgatp");
        }

        // Create a Client for MongoDB Mobile (initializing MongoDB Mobile)
        final MongoClient mobileClient = client.getServiceClient(LocalMongoDbService.clientFactory);

        // Point to the target collection and insert a document
        MongoCollection<Document> localCollection =
                mobileClient.getDatabase("mm_db").getCollection("mm_col");
        Document doc = localCollection.find(new Document("_id", 1)).first();
        Log.d("mobile", mobileClient.toString());

        if (doc == null) {
            localCollection.insertOne(new Document("_id", 1));
            Log.v("mobile", "Create initial document");
        }

        // -----------------------------------------------------------------------------------------

        if (doc == null) {
            die1 = 0;
            die2 = 0;
        } else {
            die1 = doc.getInteger("die1");
            die2 = doc.getInteger("die2");
        }

        // Set dice to saved values or new values if they where blank
        die1 = setupDie(imgDieOne, die1);
        die2 = setupDie(imgDieTwo, die2);

        // Calculate total and update txtResult.text
        total = die1 + die2;
        txtResult.setText(getString(R.string.Result, total));
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Create the default Stitch Client --------------------------------------------------------
        final StitchAppClient client =
                Stitch.getDefaultAppClient();

        // Create a Client for MongoDB Mobile (initializing MongoDB Mobile)
        final MongoClient mobileClient =
                client.getServiceClient(LocalMongoDbService.clientFactory);

        // Point to the target collection and insert a document
        MongoCollection<Document> localCollection =
                mobileClient.getDatabase("mm_db").getCollection("mm_col");

        localCollection.updateOne(
                eq("_id", 1),
                combine(set("die1", die1), set("die2", die2)));
        // -----------------------------------------------------------------------------------------
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private int setupDie (ImageView imgDie, int DieValue) {
        // Write code here to do the following
        // 1. Randomly select a number between 1 + 6
        // 2. Set the dice image to the corresponding number
        // 3. Return the number so the program knows
        int result;
        Random die;

        // Randomly select a number
        if (DieValue == 0) {
            die = new Random();
            result = die.nextInt(6) + 1;
        } else {
            result = DieValue;
        }

        // Set the dice image
        switch (result) {
            case 1:
                imgDie.setImageResource(R.drawable.die1);
                break;
            case 2:
                imgDie.setImageResource(R.drawable.die2);
                break;
            case 3:
                imgDie.setImageResource(R.drawable.die3);
                break;
            case 4:
                imgDie.setImageResource(R.drawable.die4);
                break;
            case 5:
                imgDie.setImageResource(R.drawable.die5);
                break;
            case 6:
                imgDie.setImageResource(R.drawable.die6);
                break;
        }
        return result;
    }

    private void setupWidgets () {
        // Bind Java variables to XML widgets
        bRoll = findViewById(R.id.bRoll);
        imgDieOne = findViewById(R.id.imgDieOne);
        imgDieTwo = findViewById(R.id.imgDieTwo);
        txtResult = findViewById(R.id.txtResult);

        // -- Dice 1
        imgDieOne.setOnClickListener((View v) -> {
            setupDie(imgDieOne, 6);
            die1 = 6;
            total = die1 + die2;
            txtResult.setText(getString(R.string.Result, total));
        });

        // -- Dice 2
        imgDieTwo.setOnClickListener((View v) -> {
            setupDie(imgDieTwo, 6);
            die2 = 6;
            total = die1 + die2;
            txtResult.setText(getString(R.string.Result, total));
        });

        // -- Roll button
        bRoll.setOnClickListener((View v) -> {
            // Get 2 randoms between 0 and 5, then add 1 to each
            die1 = setupDie(imgDieOne, 0);
            die2 = setupDie(imgDieTwo, 0);

            // Calculate total and update txtResult.text
            total = die1 + die2;
            txtResult.setText(getString(R.string.Result, total));
        });
    }
}
