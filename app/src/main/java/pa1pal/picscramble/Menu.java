package pa1pal.picscramble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pa1pal.picscramble.main.MainActivity;

public class Menu extends AppCompatActivity{

    @BindView(R.id.highscore)
    Button highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.newgame)
    void newGame(){
        Intent newGameIntent = new Intent(this, MainActivity.class);
        startActivity(newGameIntent);
    }

    @OnClick(R.id.highscore)
    void highScore(){
        Intent highScoreIntent = new Intent(this, HighScore.class);
        startActivity(highScoreIntent);
    }
}
