package pa1pal.picscramble.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Chronometer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pa1pal.picscramble.R;
import pa1pal.picscramble.data.model.Item;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.images_grid)
    RecyclerView flickrImagesGrid;

    @BindView(R.id.timer)
    Chronometer gameTimer;

    MainAdapter mainAdapter;
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainAdapter = new MainAdapter();
        mainPresenter = new MainPresenter(this);
        mainAdapter.setContext(this);
        mainPresenter.attachView(this);
        setUpRecyclerView();
        mainPresenter.loadImages();

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                gameTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mainAdapter.gameStatus(3);
            }
        }.start();
    }

    @Override
    public void showComplete() {

    }

    @Override
    public void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager= new GridLayoutManager(MainActivity.this,
                3);
        flickrImagesGrid.setLayoutManager(gridLayoutManager);
        flickrImagesGrid.setItemAnimator(new DefaultItemAnimator());
        flickrImagesGrid.setAdapter(mainAdapter);
    }

    @Override
    public void setUpAdapter(List<Item> randomImages) {
        mainAdapter.setImages(randomImages.subList(0, 9));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }
}
