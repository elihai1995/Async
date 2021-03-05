package com.example.async;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;

/*
Async Program ReadMe File
======================================
Hagay Cohen ID 201305679, hagaico100@gmail.com
Elihai Ben Avraham ID 206056400, elihai1995@gmail.com
Second year
Computer Science Department- Ashqelon College
================================================
Assignment:
Maintenance a program that implements a progress bar (https://en.wikipedia.org/wiki/Progress_bar) with Android Studio and fix the progress bar and make him work.

Main Goals:
Use our knowledge in Java programming and implement a progress bar with Android Studio java, and fix the progress bar to update by the worker thread (https://developer.android.com/guide/components/processes-and-threads#WorkerThreads).
Calculate the biggest prime number (https://en.wikipedia.org/wiki/Prime_number) that smaller than 10000 on the worker thread.
Implements the MVC model (https://en.wikipedia.org/wiki/Model-view-controller) as much as possible.
Write a ReadMe file that explains how the program works.

Package:
Async - Hagay&Elihai

Classes:
MainActivity.java
activity_main.xml

Classes Explanation:
We received in advance an unperfect program. The important things that we changed to make this program work are:
Added an "EXIT" button.
Merged the private class "WorkerTask" in our "MainActivity" class.
Edited the "WorkerTask" methods to work.
doInBackground() - Calculate the biggest prime number that smaller than 10000.
After every 100 numbers the program calculate, it's going to sleep for 70ms (otherwise the program was finishing to fast, and you could not see the progress bar update).
onProgressUpdate() – Updates the progress bar during the calculations of the prime numbers.
onPreExecute() – Reset the progress bar and update the "startMessage", "resetMessage" and the "exitMessage" (TextView).
In addition, to improve app design we add a photo of Shenron (https://dragonball.fandom.com/wiki/Shenron) the Japanese dragon of Dragon Ball Z (https://en.wikipedia.org/wiki/Dragon_Ball_Z).
MVC:
Model – strings.xml. Also, there are variables on "MainActivity" that belong to the model.
View – .xml files.
Controller – .java files.

Running the program:
To run the program, from within the *.java files location, type the following orders:
Enter to Android Studio and import this file "Async - Hagay&Elihai". After that you can push the play button and select your emulator and wait. Now press the "START" button and enjoy. To reset the progress bar, press the "RESET" button.
To exit the program: within the GUI, just press the 'X' with your mouse, or press the "EXIT" button.
All the process of running the program have been done only on "Genymotion" emulator, and in the mail is attached video that shows how the program runs in the "Genymotion" emulator (https://www.genymotion.com).

 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
// Model (Fields):
// Components:
    Button startButton, resetButton, exitButton;
    private ProgressBar progressBar;
    private TextView startMessage, resetMessage, exitMessage;
// Variables:
    private int completed, maxPrimeNum, target = 10000;
    private String searchMessage = "Calculate the biggest prime number that smaller than " + target + ".";
    private String completeMessage = "Completed 100%. The biggest prime number below " + target +  " is: ";

/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Get handles for the progress bar and the three TextViews.
        progressBar = findViewById(R.id.progress_bar);
        startMessage = findViewById(R.id.start_message);
        resetMessage = findViewById(R.id.reset_message);
        exitMessage = findViewById(R.id.exit_message);
// Set the maximum value that the progress bar will display.
        progressBar.setMax(100);
// Declare the listeners for the three buttons.
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

// Controller:
    public void onClick(View source) {
// Start button is clicked.
        if (source.getId() == R.id.start_button) {
            resetButton.setEnabled(false); // Disable the "Reset" button.
            startButton.setEnabled(false); // Disable the "Start" button.
            WorkerTask worker = new WorkerTask(); // Starting new worker.
            worker.execute();
        }
// Reset button is clicked.
        else if (source.getId() == R.id.reset_button) {
            progressBar.setProgress(0);
            startMessage.setText(R.string.start_message);
            resetMessage.setText(R.string.reset_message);
            exitMessage.setText(R.string.exit_message);
        }
        else if (source.getId() == R.id.exit_button) { // Exit the program.
            finish();
            System.exit(0);
        }
    }

    private class WorkerTask extends AsyncTask<Object, String, Boolean> {
        @Override
        protected void onPreExecute() {
// Initialize the progress bar.
            completed = 0;
            progressBar.setProgress(completed);
        }

        @Override
// This method updates the progress bar and the three TextViews.
        protected void onProgressUpdate(String... values) {
            completed += 1;
            progressBar.setProgress(completed);
            if (completed < 100) // If the searching is not over.
                startMessage.setText(searchMessage + String.format("\n\n%d%% Completed", completed)); // Updates the start message TextView.
            else { // If the searching is over.
                startMessage.setText(completeMessage + maxPrimeNum); // Updates the start message TextView.
                startButton.setEnabled(true); // Enable the "Start" button.
                resetButton.setEnabled(true); // Enable the "reset" button.
            }
            resetMessage.setText(""); // Empty the reset message TextView.
            exitMessage.setText(""); // Empty the exit message TextView.
        }

        @Override
        protected Boolean doInBackground(Object... params) { // Calculate the biggest prime number that smaller than 10000 (The value of "target" variable).
            boolean primeNumFlag; // Flag to know if we found a prime number.
            maxPrimeNum = 0; // Reset the variable that saved the biggest prime number.
            for (int i = 1; i <= target; ++i) {
                primeNumFlag = true;
                for (int j = 2; (j < i) && (primeNumFlag); j++)
                    if ((i % j) == 0) // If we found a number that divides "i" - then "i" is not a prime number.
                        primeNumFlag = false;
                if (primeNumFlag) // If we didn't found a number that divides "i".
                    maxPrimeNum = i; // Update the max prime number.
                if ((i % (target / 100)) == 0) { // Every 1% of the numbers that the program need to check to get the target - we update the progress bar.
                    // This will result in a call to onProgressUpdate().
                    publishProgress();
                    SystemClock.sleep(70); // Go to sleep for 70ms (otherwise the program will over too fast and you could not see the progress bar get updated).
                }
            }
            return null;
        }
    }
}