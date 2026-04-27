package com.example.colortiles;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final View[][] tiles = new View[4][4];
    private final boolean[][] tileStates = new boolean[4][4];
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        int[][] ids = {
                {R.id.tile00, R.id.tile01, R.id.tile02, R.id.tile03},
                {R.id.tile10, R.id.tile11, R.id.tile12, R.id.tile13},
                {R.id.tile20, R.id.tile21, R.id.tile22, R.id.tile23},
                {R.id.tile30, R.id.tile31, R.id.tile32, R.id.tile33}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j] = findViewById(ids[i][j]);
                final int row = i;
                final int col = j;
                tiles[i][j].setOnClickListener(v -> onTileClick(row, col));
            }
        }

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetGame());

        resetGame();
    }

    private void resetGame() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tileStates[i][j] = random.nextBoolean();
                updateTileColor(i, j);
            }
        }
        checkWin();
    }

    private void onTileClick(int row, int col) {
        List<View> changedTiles = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            tileStates[row][i] = !tileStates[row][i];
            changedTiles.add(tiles[row][i]);
        }

        for (int i = 0; i < 4; i++) {
            if (i != row) {
                tileStates[i][col] = !tileStates[i][col];
                changedTiles.add(tiles[i][col]);
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                updateTileColor(i, j);
            }
        }

        animateStrokeColor(changedTiles);
        checkWin();
    }

    private void animateStrokeColor(List<View> views) {
        for (View view : views) {
            GradientDrawable drawable = (GradientDrawable) view.getBackground();

            ValueAnimator colorAnim = ValueAnimator.ofArgb(
                    android.graphics.Color.parseColor("#4CAF50"),
                    android.graphics.Color.parseColor("#9E9E9E")
            );

            colorAnim.setDuration(900);
            colorAnim.setInterpolator(new DecelerateInterpolator());
            colorAnim.addUpdateListener(animator -> {
                int color = (int) animator.getAnimatedValue();
                drawable.setStroke(5, color);
            });
            colorAnim.start();
        }
    }

    private void updateTileColor(int row, int col) {
        GradientDrawable drawable;
        if (tileStates[row][col]) {
            drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.tile_dark);
        } else {
            drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.tile_light);
        }
        assert drawable != null;
        drawable.setStroke(5, android.graphics.Color.parseColor("#9E9E9E"));
        tiles[row][col].setBackground(drawable);
    }

    private void checkWin() {
        boolean allLight = true;
        boolean allDark = true;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tileStates[i][j]) {
                    allLight = false;
                } else {
                    allDark = false;
                }
            }
        }

        if (allLight) {
            Toast.makeText(this, "Все плитки светлые! Вы выиграли!🏆", Toast.LENGTH_LONG).show();
        } else if (allDark) {
            Toast.makeText(this, "Все плитки темные! Вы выиграли!🏆", Toast.LENGTH_LONG).show();
        }
    }
}