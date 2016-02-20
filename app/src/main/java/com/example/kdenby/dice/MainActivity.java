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
        setContentView(R.layout.activity_main);
        setupWidgets();         // Call our super function that gets us going
        bRoll.callOnClick();    // Roll the dice so we don't start with 12 :)
    }

    private int setupDie (ImageView imgDie) {
        // Write code here to do the following
        // 1. Randomly select a number between 1 + 6
        // 2. Set the dice image to the corresponding number
        // 3. Return the number so the program knows
        int result;
        Random die;

        // Randomly select a number
        die = new Random();
        result = die.nextInt(6) + 1;

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

        imgDieOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int result;

                result = setupDie(imgDieOne);
                result -= 1;

                System.out.print(result);
            }
        });

        imgDieTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int result;

                result = setupDie(imgDieTwo);
                result -= 1;

                System.out.print(result);
            }
        });


        // Create some code for when the roll button is clicked
        bRoll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Declare some variables and initialize them
                int result1, result2, total;
                Random die1, die2;

                die1 = new Random();
                die2 = new Random();

                // Get 2 randoms between 0 and 5, then add 1 to each
                result1 = die1.nextInt(6) + 1;
                result2 = die2.nextInt(6) + 1;

                // Calculate total and update txtResult.text
                total = result1 + result2;
                txtResult.setText("Result is: " + total);

                // Update die1
                System.out.println("die 1 is: " + result1);
                switch (result1) {
                    case 1:
                        imgDieOne.setImageResource(R.drawable.die1);
                        break;
                    case 2:
                        imgDieOne.setImageResource(R.drawable.die2);
                        break;
                    case 3:
                        imgDieOne.setImageResource(R.drawable.die3);
                        break;
                    case 4:
                        imgDieOne.setImageResource(R.drawable.die4);
                        break;
                    case 5:
                        imgDieOne.setImageResource(R.drawable.die5);
                        break;
                    case 6:
                        imgDieOne.setImageResource(R.drawable.die6);
                        break;
                }

                System.out.println("die 2 is: " + result2);
                // Update die2
                switch (result2) {
                    case 1:
                        imgDieTwo.setImageResource(R.drawable.die1);
                        break;
                    case 2:
                        imgDieTwo.setImageResource(R.drawable.die2);
                        break;
                    case 3:
                        imgDieTwo.setImageResource(R.drawable.die3);
                        break;
                    case 4:
                        imgDieTwo.setImageResource(R.drawable.die4);
                        break;
                    case 5:
                        imgDieTwo.setImageResource(R.drawable.die5);
                        break;
                    case 6:
                        imgDieTwo.setImageResource(R.drawable.die6);
                        break;
                }
            }
        });
    }


}
