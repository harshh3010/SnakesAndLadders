package com.codebee.snakesandladders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BoardActivity extends AppCompatActivity {

    private int w, h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);


        final GridLayout gridLayout = (GridLayout) findViewById(R.id.board_layout);

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
            if(r == 0){
                pos = 100 - c;
            }else if (r == 1){
                pos = 81 + c;
            }else if(r == 2){
                pos = 80 - c ;
            }else if(r == 3){
                pos = 61 + c;
            }else if(r == 4){
                pos = 60 - c ;
            }else if(r == 5){
                pos = 41 + c;
            }else if(r == 6){
                pos = 40 - c ;
            }else if(r == 7){
                pos = 21 + c;
            }else if(r == 8){
                pos = 20 - c ;
            }else if(r == 9){
                pos = 1 + c;
            }

            oImageView.setTag("player_position_" + pos);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = dpToPx(350 + 35)/10;
            param.width = dpToPx(350 +  35)/10;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            oImageView.setLayoutParams(param);
            gridLayout.addView(oImageView);
        }
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
