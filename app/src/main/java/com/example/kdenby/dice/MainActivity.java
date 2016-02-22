package com.example.kdenby.dice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button bRoll;
    ImageView imgDieOne, imgDieTwo;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Create!");
        setContentView(R.layout.activity_main);

        // This is an attempt to only create everything once
        if (savedInstanceState == null) {
            setupWidgets();         // Call our super function that gets us going
            bRoll.callOnClick();    // Roll the dice so we don't start with 12 :)
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resumed!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // We should have code here that drops usage of things like cameras and sensors
        // Nothing to heavy though as we will still be visible

        System.out.println("Paused!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // We should have teardown code here, the heavy stuff including writing to storage

        System.out.println("Stopped!");
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
        System.out.println("die is: " + result);
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

                result = setupDie(imgDieOne, 0);
                result -= 1;

            }
        });

        // -- Dice 2
        imgDieTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int result;

                result = setupDie(imgDieTwo, 6);
                result -= 1;

            }
        });

        // -- Roll button
        bRoll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Declare some variables and initialize them
                int result1, result2, total;

                // Get 2 randoms between 0 and 5, then add 1 to each
                result1 = setupDie(imgDieOne, 0);
                result2 = setupDie(imgDieTwo, 0);

                // Calculate total and update txtResult.text
                total = result1 + result2;
                txtResult.setText("Result is: " + total);
            }
        });
    }
}
