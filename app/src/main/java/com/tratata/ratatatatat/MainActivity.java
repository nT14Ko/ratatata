package com.tratata.ratatatatat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_COLOR = 0;
    private final String GAME_STATE = "gameState";
    private LightOutsGame mGame;
    private Button[][] mButtons;
    private int mOnColor;
    private int mOffColor;
    private int mOnColorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOnColorId = R.color.yellow;
        mOnColor= ContextCompat.getColor(this, R.color.yellow);
        mOffColor = ContextCompat.getColor( this, R.color.black);

        mButtons = new Button[LightOutsGame.NUM_ROWS][LightOutsGame.NUM_COLS];

        GridLayout gridLayout = findViewById(R.id.light_grid);
        int childIndex =0;
        for (int row = 0; row < LightOutsGame.NUM_ROWS; row++) {
            for (int col = 0; col < LightOutsGame.NUM_COLS; col++) {
                mButtons[row][col] = (Button) gridLayout.getChildAt(childIndex);
                childIndex++;
            }
        }
        
        mGame = new LightOutsGame();
        if (savedInstanceState == null) {
            startGame();
        }
        else {
            String gameState = savedInstanceState.getString(GAME_STATE);
            mGame.setState(gameState);
            setButtonColors();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GAME_STATE, mGame.getState());
    }

    private void startGame(){
        mGame.newGame();
        setButtonColors();
    }
    
    public void onLightButtonClick(View view){
        boolean buttonFound = false;
        for (int row = 0; row < LightOutsGame.NUM_ROWS && !buttonFound; row++) {
            for (int col = 0; col < LightOutsGame.NUM_COLS && !buttonFound; col++) {
                if( view == mButtons[row][col]){
                    mGame.selectLight(row, col);
                    buttonFound = true;
                }
            }
        }

        setButtonColors();

        if(mGame.isGameOver()){
            Toast.makeText(this , R.string.congrats, Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtonColors(){
        for (int row = 0; row < LightOutsGame.NUM_ROWS; row++) {
            for (int col = 0; col < LightOutsGame.NUM_COLS; col++) {
                if(mGame.isLightOn(row, col)){
                    mButtons[row][col].setBackgroundColor(mOnColor);
                }else{
                    mButtons[row][col].setBackgroundColor(mOffColor);
                }
            }
        }
    }
        public void onNewGameClick(View view){
        startGame();
        }
        public void onHelpClick(View view){
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        public void onChangeColorClick(View view){
            Intent intent = new Intent(this, ColorActivity.class);
            intent.putExtra(ColorActivity.EXTRA_COLOR, mOnColorId);
            startActivityForResult(intent, REQUEST_CODE_COLOR);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_COLOR) {
                mOnColorId = data.getIntExtra(ColorActivity.EXTRA_COLOR, R.color.yellow);
                mOnColor = ContextCompat.getColor(this, mOnColorId);
                setButtonColors();
            }
        }
}