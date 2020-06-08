package com.codebee.snakesandladders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BoardActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private int p1_pos = 1, p2_pos = 1;
    private String turn = "p1";
    private boolean singlePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        gridLayout = findViewById(R.id.board_layout);

        if(getIntent().getStringExtra("type").equals("single")){
            singlePlayer = true;
        }else{
            singlePlayer = false;
        }

        generateBoard();
        playTurn();
    }

    private void generateBoard() {
        int total = 100;
        int column = 10;
        int row = 10;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row);
        for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }
            ImageView oImageView = new ImageView(this);
            oImageView.setImageResource(R.drawable.empty);

            int pos = 0;
            if (r == 0) {
                pos = 100 - c;
            } else if (r == 1) {
                pos = 81 + c;
            } else if (r == 2) {
                pos = 80 - c;
            } else if (r == 3) {
                pos = 61 + c;
            } else if (r == 4) {
                pos = 60 - c;
            } else if (r == 5) {
                pos = 41 + c;
            } else if (r == 6) {
                pos = 40 - c;
            } else if (r == 7) {
                pos = 21 + c;
            } else if (r == 8) {
                pos = 20 - c;
            } else if (r == 9) {
                pos = 1 + c;
            }

            oImageView.setTag("board_position_" + pos);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = dpToPx(350 + 35) / 10;
            param.width = dpToPx(350 + 35) / 10;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            oImageView.setLayoutParams(param);
            gridLayout.addView(oImageView);
        }

        ((ImageView) gridLayout.findViewWithTag("board_position_1")).setImageResource(R.drawable.p1p2);

    }

    private void playTurn() {
        if (turn.equals("p1")) {
            ((TextView) findViewById(R.id.board_turn_text)).setText("Player 1's turn");

            findViewById(R.id.board_roll_dice_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int randomInt, prev_pos;
                    Random rn = new Random();
                    randomInt = rn.nextInt(6) + 1;
                    ((TextView) findViewById(R.id.board_dice_text)).setText("" + randomInt);

                    prev_pos = p1_pos;
                    p1_pos = p1_pos + randomInt;
                    p1_pos = updatePosition(p1_pos);

                    ((TextView) findViewById(R.id.board_turn_text)).setText("Player 1 is moving now.");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (p1_pos < 100) {
                                ((ImageView) gridLayout.findViewWithTag("board_position_" + prev_pos)).setImageResource(R.drawable.empty);
                                if (p1_pos == p2_pos) {
                                    ((ImageView) gridLayout.findViewWithTag("board_position_" + p1_pos)).setImageResource(R.drawable.p1p2);
                                } else {
                                    ((ImageView) gridLayout.findViewWithTag("board_position_" + p1_pos)).setImageResource(R.drawable.p1);
                                    ((ImageView) gridLayout.findViewWithTag("board_position_" + p2_pos)).setImageResource(R.drawable.p2);
                                }

                                turn = "p2";
                                playTurn();
                            } else if (p1_pos == 100) {
                                ((ImageView) gridLayout.findViewWithTag("board_position_" + prev_pos)).setImageResource(R.drawable.empty);
                                ((ImageView) gridLayout.findViewWithTag("board_position_" + p1_pos)).setImageResource(R.drawable.p1);
                                ((ImageView) gridLayout.findViewWithTag("board_position_" + p2_pos)).setImageResource(R.drawable.p2);

                                Toast.makeText(getApplicationContext(), "Player 1 wins!!", Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                p1_pos = prev_pos;
                                turn = "p2";
                                playTurn();
                            }
                        }
                    },300);

                }
            });

        } else if (turn.equals("p2")) {

            ((TextView) findViewById(R.id.board_turn_text)).setText("Player 2's turn");

            if(singlePlayer){
                playerTwoTurn();
            }else{
                findViewById(R.id.board_roll_dice_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerTwoTurn();
                    }
                });
            }

        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void playerTwoTurn(){
        final int randomInt, prev_pos;
        Random rn = new Random();
        randomInt = rn.nextInt(6) + 1;
        ((TextView) findViewById(R.id.board_dice_text)).setText("" + randomInt);

        prev_pos = p2_pos;
        p2_pos = p2_pos + randomInt;
        p2_pos = updatePosition(p2_pos);

        ((TextView) findViewById(R.id.board_turn_text)).setText("Player 2 is moving now.");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (p2_pos < 100) {
                    ((ImageView) gridLayout.findViewWithTag("board_position_" + prev_pos)).setImageResource(R.drawable.empty);
                    if (p1_pos == p2_pos) {
                        ((ImageView) gridLayout.findViewWithTag("board_position_" + p2_pos)).setImageResource(R.drawable.p1p2);
                    } else {
                        ((ImageView) gridLayout.findViewWithTag("board_position_" + p1_pos)).setImageResource(R.drawable.p1);
                        ((ImageView) gridLayout.findViewWithTag("board_position_" + p2_pos)).setImageResource(R.drawable.p2);
                    }

                    turn = "p1";
                    playTurn();
                } else if (p2_pos == 100) {
                    ((ImageView) gridLayout.findViewWithTag("board_position_" + prev_pos)).setImageResource(R.drawable.empty);
                    ((ImageView) gridLayout.findViewWithTag("board_position_" + p1_pos)).setImageResource(R.drawable.p1);
                    ((ImageView) gridLayout.findViewWithTag("board_position_" + p2_pos)).setImageResource(R.drawable.p2);

                    Toast.makeText(getApplicationContext(), "Player 2 wins!!", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    p2_pos = prev_pos;
                    turn = "p1";
                    playTurn();
                }
            }
        },300);
    }

    private int updatePosition(int position) {

        switch (position) {
            case 5:
                position = 35;
                break;
            case 9:
                position = 51;
                break;
            case 23:
                position = 42;
                break;
            case 36:
                position = 5;
                break;
            case 48:
                position = 86;
                break;
            case 49:
                position = 7;
                break;
            case 56:
                position = 8;
                break;
            case 62:
                position = 83;
                break;
            case 69:
                position = 91;
                break;
            case 82:
                position = 20;
                break;
            case 87:
                position = 66;
                break;
            case 95:
                position = 38;
                break;
        }

        return position;
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Do you really want to exit?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
