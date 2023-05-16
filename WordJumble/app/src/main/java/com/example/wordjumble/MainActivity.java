package com.example.wordjumble;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_LIVES = 3;

    private TextView clueTextView;
    private Button[] tileButtons;
    private Button submitButton;

    private String word;
    private String jumbledWord;
    private String userGuess;

    private int wordIndex = 0;
    private int lives = MAX_LIVES;

    private String[] wordList = {
            "apple : A red fruit",
            "banana : A yellow fruit",
            "cherry : A small red fruit",
            "orange : A citrus fruit",
            "mango :  A tropical fruit",
            // Add more words as needed
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        clueTextView = findViewById(R.id.clueTextView);
        tileButtons = new Button[9];
        tileButtons[0] = findViewById(R.id.tileButton1);
        tileButtons[1] = findViewById(R.id.tileButton2);
        tileButtons[2] = findViewById(R.id.tileButton3);
        tileButtons[3] = findViewById(R.id.tileButton4);
        tileButtons[4] = findViewById(R.id.tileButton5);
        tileButtons[5] = findViewById(R.id.tileButton6);
        tileButtons[6] = findViewById(R.id.tileButton7);
        tileButtons[7] = findViewById(R.id.tileButton8);
        tileButtons[8] = findViewById(R.id.tileButton9);
        submitButton = findViewById(R.id.submitButton);

        for (int i = 0; i < tileButtons.length; i++) {
            final int tileIndex = i;
            tileButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fillTile(tileIndex);
                }
            });
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGuess();
            }
        });

        resetGame();
    }

    private void resetGame() {
        wordIndex = 0;
        lives = MAX_LIVES;
        updateLivesDisplay();

        nextWord();
    }

    private void nextWord() {
        wordIndex++;
        if (wordIndex >= wordList.length) {
            wordIndex = 0;
            Toast.makeText(this, "Game Over! Starting from the beginning.", Toast.LENGTH_SHORT).show();
        }
        String[] wordAndClue = wordList[wordIndex].split(":");
        word = wordAndClue[0].trim();
        String clue = wordAndClue[1].trim();
        jumbledWord = jumbleWord(word);
        userGuess = "";

        clueTextView.setText("Clue: " + clue);
        updateTileButtons();

       /* word = wordList[wordIndex];
        jumbledWord = jumbleWord(word);
        userGuess = "";

        clueTextView.setText("Clue: " + jumbledWord);
        updateTileButtons();*/
    }

    private String jumbleWord(String word) {
        char[] chars = word.toCharArray();
        Random random = new Random();

        for (int i = 0; i < chars.length; i++) {
            int randomIndex = random.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[randomIndex];
            chars[randomIndex] = temp;
        }

        return new String(chars);
    }

    private void updateTileButtons() {
        int jumbledWordLength = jumbledWord.length();

        for (int i = 0; i < tileButtons.length; i++) {
            if (i < jumbledWordLength) {
                tileButtons[i].setText(String.valueOf(jumbledWord.charAt(i)));
                tileButtons[i].setVisibility(View.VISIBLE);
            } else {
                tileButtons[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void checkGuess() {
        String guessedWord = userGuess.toLowerCase().trim();
        if (guessedWord.equals(word)) {
            Toast.makeText(this, "Correct! The word is: " + word, Toast.LENGTH_SHORT).show();
            nextWord();
        } else {
            Toast.makeText(this, "Wrong guess!Try again.", Toast.LENGTH_SHORT).show();
            lives--;
            updateLivesDisplay();
            if (lives == 0) {
                Toast.makeText(this, "Game Over! Starting from the beginning.", Toast.LENGTH_SHORT).show();
                resetGame();
            }else{
                updateTileButtons();
            }
        }
    }

    private void fillTile(int tileIndex) {
        String tileText = tileButtons[tileIndex].getText().toString();
        userGuess += tileText;
        tileButtons[tileIndex].setVisibility(View.INVISIBLE);
    }

    private void updateLivesDisplay() {
        TextView livesTextView = findViewById(R.id.livesTextView);
        livesTextView.setText("Lives: " + lives);
    }
}