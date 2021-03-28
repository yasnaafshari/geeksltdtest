package com.example.geekltdtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<PhotoModel> photoModels;
    private int index = 0;
    private TextView scoreCounter;
    private TextView totalTextView;
    private MaterialButton playButton;
    private TextView totalScoreTextView;
    private ImageView gameImage;
    private TextView scoreTextView;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoModels = PhotoModel.createPhotoData();
        bindViews();
        playButton.setOnClickListener(v -> startTheGame());


        setupImageTouchListener();
    }

    private void setupImageTouchListener()
    {
        gameImage.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().cancel();
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveImageTo(v, event);
                        break;
                    case MotionEvent.ACTION_UP:
                        handleImageDropped(v, event);
                        return false;
                    default:
                        return false;
                }
                return true;
            }

            private void moveImageTo(View v, MotionEvent event)
            {
                v.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
            }
        });
    }

    private void handleImageDropped(View v, MotionEvent event)
    {
        v.animate().cancel();
        float screenWidth = getScreenWidth();
        float maxHeight = getScreenHeight();

        float screenMiddle = screenWidth/2;
        float gameImageMiddleX = getImageMiddleX(screenWidth);
        float gameImageXDelta = Math.abs(gameImageMiddleX - screenMiddle);
        if (gameImageXDelta < 20 ) {
            animateImageToBottomMiddle(gameImage,index);
            return;
        }
        checkAnswer(event, index);
        float y = 0;
        float x = 0;
        if (locateThePictureOnScreen(event) == PhotoModel.Race.THAI) {
            x = screenWidth;
            y = 0;
        } else if (locateThePictureOnScreen(event) == PhotoModel.Race.CHINESE) {
            x = 0;
            y = maxHeight;
        } else if (locateThePictureOnScreen(event) == PhotoModel.Race.KOREAN) {
            x = screenWidth;
            y = maxHeight;
        }
        gameImage.animate().x(x).y(y).alpha(0).setDuration(1000).withEndAction(this::checkNextRound);
    }

    private void checkNextRound() {
        if (index < photoModels.size() - 1) {
            displayNextImage();
        } else {
            gameOver();
        }
    }

    private void bindViews() {
        gameImage = findViewById(R.id.game_image);
        playButton = findViewById(R.id.playButton);
        totalScoreTextView = findViewById(R.id.totalScoreTextView);
        totalTextView = findViewById(R.id.totalTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        scoreCounter = findViewById(R.id.scoreCounter);

    }

    private void gameOver() {
        totalScoreTextView.setText(String.valueOf(score));
        playButton.setVisibility(View.VISIBLE);
        totalScoreTextView.setVisibility(View.VISIBLE);
        totalTextView.setVisibility(View.VISIBLE);
        gameImage.setVisibility(View.GONE);
    }

    private void displayNextImage() {
        gameImage.setY(0);
        float screenWidth = getScreenWidth();
        gameImage.setX(getImageMiddleX(screenWidth));
        gameImage.setVisibility(View.VISIBLE);
        gameImage.animate()
                .alpha(1)
                .setDuration(0)
                .start();

        index++;
        animateImageToBottomMiddle(gameImage, index);
    }

    private float getImageMiddleX(float screenWidth)
    {
        return screenWidth / 2 - (gameImage.getWidth() /2);
    }

    private void checkAnswer(MotionEvent event, int index) {
        if (locateThePictureOnScreen(event) == photoModels.get(index).race) {

            score += 20;
        } else {
            score -= 5;
        }
        scoreCounter.setText(String.valueOf(score));
    }

    private void startTheGame() {
        index = 0;
        score = 0;
        playButton.setVisibility(View.GONE);
        totalScoreTextView.setVisibility(View.GONE);
        totalTextView.setVisibility(View.GONE);
        scoreCounter.setText("0");
        gameImage.setVisibility(View.VISIBLE);
        float maxWidth = getScreenWidth();
        gameImage.setX(getImageMiddleX(maxWidth));
        gameImage.animate()
                .alpha(1)
                .setDuration(0)
                .start();
        gameImage.setY(0);
        animateImageToBottomMiddle(gameImage, 0);
    }


    private int getScreenWidth()
    {
        return getWindow().getDecorView().getWidth();
    }
    private int getScreenHeight()
    {
        return MainActivity.this.getWindow().getDecorView().getHeight();
    }

    private void animateImageToBottomMiddle(ImageView image, int index) {
        image.setImageResource(photoModels.get(index).image);
        float imageHeight = image.getHeight();
        float y = scoreTextView.getY() - imageHeight;
        long maxDuration = 3000;
        float maxWidth = getScreenWidth();
        float x = getImageMiddleX(maxWidth);
        long durationRelativeToCurrentYPosition = (long) (maxDuration * ((y - image.getY()) / y));
        image.animate().setDuration(durationRelativeToCurrentYPosition).x(x).y(y).withEndAction(this::checkNextRound);
    }

    public PhotoModel.Race locateThePictureOnScreen(MotionEvent event) {
        int screenHeight = this.getWindow().getDecorView().getHeight();
        int screenWidth = getScreenWidth();
        int middleY = screenHeight / 2;
        int middleX = screenWidth / 2;
        float x = event.getRawX();
        float y = event.getRawY();
        if (x < middleX && y < middleY) {
            return PhotoModel.Race.JAPANESE;
        } else if (x < middleX && y > middleY) {
            return PhotoModel.Race.CHINESE;
        } else if (x > middleX && y > middleY) {
            return PhotoModel.Race.KOREAN;
        } else {
            return PhotoModel.Race.THAI;
        }
    }
}