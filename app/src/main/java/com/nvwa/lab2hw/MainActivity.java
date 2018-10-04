package com.nvwa.lab2hw;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected String mystery_word, guess, displayMW;
    int gallows_state;
    String[] words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make( view, "Start new game?", Snackbar.LENGTH_LONG )
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick( View view ) {
                                startGame(); } }).show();
            }
        });

        // My code starts below
        startGame();
    }

    public void startGame() {
        gallows_state = 0;
        // Enable buttons
        findViewById( R.id.wordCheck ).setEnabled(true);
        findViewById( R.id.letterCheck ).setEnabled(true);
        findViewById( R.id.guess ).setEnabled(true);

        // Get all the words
        words = getResources().getStringArray( R.array.words );
        int number = (int)( words.length * Math.random() );
        mystery_word = words[number];

        // Paste censored keyword to layout
        TextView keyword = findViewById( R.id.keyword );
        StringBuilder buildDisplayMW = new StringBuilder();
        for ( int i = 0; i < mystery_word.length(); i++ )
            buildDisplayMW.append("*");
        displayMW = buildDisplayMW.toString();
        keyword.setText(displayMW);

        // Make sure picture is correct
        ( (ImageView)findViewById( R.id.hangman ) ).setImageDrawable( getResources().getDrawable( R.drawable.hangman0 ) );
        ( (ImageView)findViewById( R.id.hangman ) ).setContentDescription(
                getResources().getString( R.string.img2txtHead ) +
                        gallows_state +
                        getResources().getString( R.string.img2txtTail ) );
    }

    public void update( View view ) {
        TextView guess_word = findViewById( R.id.guess );
        TextView keyword = findViewById( R.id.keyword );
        int index = -1;

        guess = guess_word.getText().toString();
        // Check EditText for different conditions
        if ( guess.isEmpty() ) {
            Snackbar.make( view, "Please type your word or letter.", Snackbar.LENGTH_SHORT ).show(); // later add a hint that input is empty
        } else if ( mystery_word.contains(guess) ) {
            StringBuilder tmp = new StringBuilder( displayMW );
            while (true) {
                index = mystery_word.indexOf( guess, index+1 );
                if ( index == -1 )
                    break;
                for ( int i = 0; i < guess.length(); i++ )
                    tmp.setCharAt( index+i, mystery_word.charAt(index+i) );
            }
            displayMW = tmp.toString();
            keyword.setText(displayMW);
        } else {
            // Wrong guess
            gallows_state++; // increment
            String img = "hangman" + gallows_state; // name of image of current state
            int resID = getResources().getIdentifier( img, "drawable", getPackageName() ); // find image
            ( (ImageView)findViewById( R.id.hangman ) ).setImageDrawable( getResources().getDrawable( resID ) ); // set image
            ( (ImageView)findViewById( R.id.hangman ) ).setContentDescription(
                    getResources().getString( R.string.img2txtHead ) +
                            gallows_state +
                            getResources().getString( R.string.img2txtTail ) ); // set desc of image

        }
        if ( ( gallows_state == 10 || displayMW.equals(mystery_word) ) )
            endGame(view);
        ((TextView)findViewById( R.id.guess )).setText("");
    }

    public void endGame( View view ) {
        findViewById( R.id.wordCheck ).setEnabled(false);
        findViewById( R.id.letterCheck ).setEnabled(false);
        findViewById( R.id.guess ).setEnabled(false);

        // Game won
        if ( displayMW.equals(mystery_word) ) {
            ( (ImageView)findViewById( R.id.hangman ) ).setImageDrawable( getResources().getDrawable( R.drawable.hangman11 ) );
            Snackbar.make(view, "Congratulations! You found the word!", Snackbar.LENGTH_LONG ).show();
        }
        // Game lost
        if ( gallows_state == 10 ) {
            TextView keyword = findViewById( R.id.keyword );
            keyword.setText(mystery_word); // show the word
            Snackbar.make( view, "You lost :/ Better luck next time!", Snackbar.LENGTH_LONG ).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
