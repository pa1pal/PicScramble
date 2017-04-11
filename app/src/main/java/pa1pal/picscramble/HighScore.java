package pa1pal.picscramble;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HighScore extends AppCompatActivity {

    @BindView(R.id.fhs)
    TextView bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        SharedPreferences sharedPreferences = HighScore.this.getSharedPreferences("scores", Context
                .MODE_PRIVATE);
        ButterKnife.bind(this);

        bestScore.setText((sharedPreferences.getInt("score", 0) + " Clicks"));
    }
}
