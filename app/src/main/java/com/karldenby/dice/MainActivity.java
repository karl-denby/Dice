package com.karldenby.dice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions;
import com.mongodb.stitch.core.services.mongodb.remote.sync.ChangeEventListener;
import com.mongodb.stitch.core.services.mongodb.remote.sync.DefaultSyncConflictResolvers;
import com.mongodb.stitch.core.services.mongodb.remote.sync.ErrorListener;
import com.mongodb.stitch.core.services.mongodb.remote.sync.internal.ChangeEvent;

import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MainActivity extends AppCompatActivity {

    // For Save/Load
    Context context = MainActivity.this;

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

        setupWidgets();         // attached activity elements to callbacks

        mongoMobileLocalDatabaseConnection();
        // Create the default Stitch Client
        final StitchAppClient client =
                Stitch.getDefaultAppClient();

        // Create a Client for MongoDB Mobile (initializing MongoDB Mobile)
        final MongoClient mobileClient =
                client.getServiceClient(LocalMongoDbService.clientFactory);

        // Point to the target collection and insert a document
        MongoCollection<Document> localCollection =
                mobileClient.getDatabase("mm_db").getCollection("mm_col");

        Document doc = localCollection.find(new Document("_id", 1)).first();

        Log.d("mobile", mobileClient.toString());


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
        // We should have code here that drops usage of things like cameras and sensors
        // Nothing to heavy though as we will still be visible

        // Create the default Stitch Client
        final StitchAppClient client =
                Stitch.getDefaultAppClient();

        // Create a Client for MongoDB Mobile (initializing MongoDB Mobile)
        final MongoClient mobileClient =
                client.getServiceClient(LocalMongoDbService.clientFactory);

        // Point to the target collection and insert a document
        MongoCollection<Document> localCollection =
                mobileClient.getDatabase("mm_db").getCollection("mm_col");

        Document document = new Document("_id", 1)
                .append("die1", die1)
                .append("die2", die2);

        localCollection.replaceOne(new Document("_id", 1 ), document, new UpdateOptions().upsert(true));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // We should have teardown code here, the heavy stuff including writing to storage
    }

    private void mongoMobileLocalDatabaseConnection() {

        final StitchAppClient client;
        final MongoClient mobileClient;

        // MongoDB Mobile Local
        // Create the default Stitch Client
        if (Stitch.hasAppClient("dice-mgatp")) {
            client = Stitch.getDefaultAppClient();
        } else {
            client = Stitch.initializeDefaultAppClient("dice-mgatp");
        }
        mobileClient = client.getServiceClient(LocalMongoDbService.clientFactory);

        Log.d("MongoMobile","Connect Setup");
        Log.d("MongoMobile", client.toString());
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

        // Return the result
        return result;
    }

    private void setupWidgets () {
        // Bind Java variables to XML widgets
        bRoll = (Button)findViewById(R.id.bRoll);
        imgDieOne = (ImageView)findViewById(R.id.imgDieOne);
        imgDieTwo = (ImageView)findViewById(R.id.imgDieTwo);
        txtResult = (TextView)findViewById(R.id.txtResult);

        // create some code for when the user clicks on some of these widgets
        // -- Dice 1
        imgDieOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int result;

                result = setupDie(imgDieOne, 6);
                die1 = 6;
                total = die1 + die2;
                txtResult.setText(getString(R.string.Result,result));

                result -= 1;
            }
        });

        // -- Dice 2
        imgDieTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int result;

                result = setupDie(imgDieTwo, 6);
                die2 = 6;
                total = die1 + die2;
                txtResult.setText(getString(R.string.Result, total));

                result -= 1;
            }
        });

        // -- Roll button
        bRoll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Declare some variables and initialize them

                // Get 2 randoms between 0 and 5, then add 1 to each
                die1 = setupDie(imgDieOne, 0);
                die2 = setupDie(imgDieTwo, 0);

                // Calculate total and update txtResult.text
                total = die1 + die2;
                txtResult.setText(getString(R.string.Result, total));
            }
        });

    }

}
