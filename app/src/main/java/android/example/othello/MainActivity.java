package android.example.othello;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int score_0 = 2;
    private int score_1 = 2;

    private boolean b_turn = true;

    private final ImageButton [][] board = new ImageButton[8][8];

    private TextView player_0;
    private TextView player_1;

    private ImageView indicator;

    SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicator = findViewById(R.id.indicator);

        player_0 = findViewById(R.id.score_0);
        player_1 = findViewById(R.id.score_1);

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                String button_id = "button_" + i + j;
                int resID = getResources().getIdentifier(button_id,"id", getPackageName());
                board[i][j] = findViewById(resID);
                board[i][j].setOnClickListener(this);
            }
        }

//        undo = findViewById(R.id.button_undo);

        refresh = findViewById(R.id.refresh);

        refresh.setOnRefreshListener(() -> {
            reset_score();
            reset_board();
            refresh.setRefreshing(false);
        });
    }

    @Override
    public void onClick(View view) {
        if(((ImageButton) view).getTag().toString().equals("")) {
            game_space(view.getId());
        }

        if((score_0 + score_1) == 64) {
            if(score_0 > score_1) {
                Toast.makeText(this, "White Wins", Toast.LENGTH_SHORT).show();
            }
            else if(score_0 < score_1) {
                Toast.makeText(this, "Black Wins", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void reset_board() {
        board[3][3].setTag("B");
        board[3][3].setImageResource(R.drawable.black_dot);
        board[3][4].setTag("W");
        board[3][4].setImageResource(R.drawable.white_dot);
        board[4][3].setTag("W");
        board[4][3].setImageResource(R.drawable.white_dot);
        board[4][4].setTag("B");
        board[4][4].setImageResource(R.drawable.black_dot);
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(((i == 3) || (i == 4)) && ((j == 3) || (j == 4))) {
                    continue;
                }
                board[i][j].setTag("");
                board[i][j].setImageResource(R.color.darker_gray);
            }
        }
    }

    @SuppressLint("setTagI18n")
    private void reset_score() {
        b_turn = true;
        score_0 = 2;
        score_1 = 2;
        player_0.setText("2");
        player_1.setText("2");
        indicator.setImageResource(R.drawable.black_dot);
    }

    private void game_space(int id) {
        ImageButton v = findViewById(id);
        int x = 0, y = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                String button_id = "button_" + i + j;
                int resID = getResources().getIdentifier(button_id,"id", getPackageName());
                if(resID == id) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        if(b_turn) {
            v.setTag("B");
            v.setImageResource(R.drawable.black_dot);
            b_turn = false;
            score_1++;
            makeChange("B", x, y);
        }
        else {
            v.setTag("W");
            v.setImageResource(R.drawable.white_dot);
            b_turn = true;
            score_0++;
            makeChange("W", x, y);
        }
        update_scores();
    }

    private void update_scores() {
        String text_1, text_0;
        text_0 = "" + score_0;
        text_1 = "" + score_1;
        if(b_turn) {
            indicator.setImageResource(R.drawable.black_dot);
        }
        else {
            indicator.setImageResource(R.drawable.white_dot);
        }
        player_1.setText(text_1);
        player_0.setText(text_0);
    }

    private void makeChange(String c, int x, int y) {
        int n = 0;
        for(int i = y + 1; i < 8; i++) {
            if(board[x][i].getTag().equals("")) {
                break;
            }
            else if(board[x][i].getTag().equals(c)) {
                for(int k = y + 1; k < i; k++) {
                    board[x][k].setTag(c);
                    if(c.equals("B")) {
                        board[x][k].setImageResource(R.drawable.black_dot);
                    }
                    else {
                        board[x][k].setImageResource(R.drawable.white_dot);
                    }
                    n++;
                }
                break;
            }
        }
        for(int i = y - 1; i >= 0; i--) {
            if(board[x][i].getTag().equals("")) {
                break;
            }
            else if(board[x][i].getTag().equals(c)) {
                for(int k = i + 1; k <= y - 1; k++) {
                    board[x][k].setTag(c);
                    if(c.equals("B")) {
                        board[x][k].setImageResource(R.drawable.black_dot);
                    }
                    else {
                        board[x][k].setImageResource(R.drawable.white_dot);
                    }
                    n++;
                }
                break;
            }
        }
        for(int i = x + 1; i < 8; i++) {
            if(board[i][y].getTag().equals("")) {
                break;
            }
            else if(board[i][y].getTag().equals(c)) {
                for(int k = x + 1; k < i; k++) {
                    board[k][y].setTag(c);
                    if(c.equals("B")) {
                        board[k][y].setImageResource(R.drawable.black_dot);
                    }
                    else {
                        board[k][y].setImageResource(R.drawable.white_dot);
                    }
                    n++;
                }
                break;
            }
        }
        for(int i = x - 1; i >= 0; i--) {
            if(board[i][y].getTag().equals("")) {
                break;
            }
            else if(board[i][y].getTag().equals(c)) {
                for(int k = i + 1; k <= x - 1; k++) {
                    board[k][y].setTag(c);
                    if(c.equals("B")) {
                        board[k][y].setImageResource(R.drawable.black_dot);
                    }
                    else {
                        board[k][y].setImageResource(R.drawable.white_dot);
                    }
                    n++;
                }
                break;
            }
        }
        for(int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
            if(board[i][j].getTag().equals("")) {
                break;
            }
            else if(board[i][j].getTag().equals(c)) {
                for(int k = x + 1, l = y + 1; k < i && l < j; k++, l++) {
                    board[k][l].setTag(c);
                    if(c.equals("B")) {
                        board[k][l].setImageResource(R.drawable.black_dot);
                    }
                    else {
                        board[k][l].setImageResource(R.drawable.white_dot);
                    }
                    n++;
                }
                break;
            }
        }
        for(int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            if(board[i][j].getTag().equals("")) {
                break;
            }
            else if(board[i][j].getTag().equals(c)) {
                for(int k = x - 1, l = y - 1; k > i && l > j; k--, l--) {
                    board[k][l].setTag(c);
                    if(c.equals("B")) {
                        board[k][l].setImageResource(R.drawable.black_dot);
                    }
                    else {
                        board[k][l].setImageResource(R.drawable.white_dot);
                    }
                    n++;
                }
                break;
            }
        }
        for(int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
            if(board[i][j].getTag().equals("")) {
                break;
            }
            else if(board[i][j].getTag().equals(c)) {
                for(int k = x - 1, l = y + 1; k > i && l < j; k--, l++) {
                    board[k][l].setTag(c);
                    if(c.equals("B")) {
                        board[k][l].setImageResource(R.drawable.black_dot);
                    }
                    else {
                        board[k][l].setImageResource(R.drawable.white_dot);
                    }
                    n++;
                }
                break;
            }
        }
        for(int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
            if(board[i][j].getTag().equals("")) {
                break;
            }
            else if(board[i][j].getTag().equals(c)) {
                for(int k = x + 1, l = y - 1; k < i && l > j; k++, l--) {
                    board[k][l].setTag(c);
                    if(c.equals("B")) {
                        board[k][l].setImageResource(R.drawable.black_dot);
                    }
                    else {
                        board[k][l].setImageResource(R.drawable.white_dot);
                    }
                    n++;
                }
                break;
            }
        }
        if(c.equals("B")) {
            score_0 -= n;
            score_1 += n;
        }
        else {
            score_0 += n;
            score_1 -= n;
        }
    }
}