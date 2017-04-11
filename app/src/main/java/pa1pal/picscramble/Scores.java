package pa1pal.picscramble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pa1pal.picscramble.main.MainActivity;

public class Scores extends AppCompatActivity implements ImageButton.OnClickListener{

    @BindView(R.id.score)
    TextView scoreText;

    @BindView(R.id.highscoretrophy)
    ImageView highScoreTrophy;

    @BindView(R.id.replay)
    ImageButton replay;

    @BindView(R.id.home)
    ImageButton actionHome;

    @BindView(R.id.newhighscore)
    TextView nHighScore;

    boolean isHighScore;
    Bundle extras;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        ButterKnife.bind(this);

        actionHome.setOnClickListener(this);
        replay.setOnClickListener(this);
        score = (int) getIntent().getExtras().get("cs");
        isHighScore = (boolean) getIntent().getExtras().getBoolean("hs");
        scoreText.setText("You did it in " + String.valueOf(score)+ "Clicks");
        if (isHighScore) {
            nHighScore.setVisibility(View.VISIBLE);
            highScoreTrophy.setVisibility(View.VISIBLE);
        }

    }

    private void beginPlayingGame(Intent ri) {
        ri = new Intent(Scores.this, MainActivity.class);
        startActivity(ri);
        finish();
    }

    private void goToHome(Intent rh) {
        rh = new Intent(Scores.this, Menu.class);
        startActivity(rh);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent homeIntent = new Intent();
        Intent replayIntent = new Intent();
        switch (v.getId()) {
            case R.id.home:
                goToHome(homeIntent);
            break;

            case R.id.replay:
                beginPlayingGame(replayIntent);
            break;
        }

    }
}
