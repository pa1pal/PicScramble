package pa1pal.picscramble.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pa1pal.picscramble.R;
import pa1pal.picscramble.data.model.Item;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.images_grid)
    RecyclerView flickrImagesGrid;

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
    }

    @Override
    public void showComplete() {

    }

    @Override
    public void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.VERTICAL, false);
        flickrImagesGrid.setLayoutManager(linearLayoutManager);
        flickrImagesGrid.setItemAnimator(new DefaultItemAnimator());
        flickrImagesGrid.setAdapter(mainAdapter);
    }

    @Override
    public void setUpAdapter(List<Item> randomImages) {
        mainAdapter.setImages(randomImages);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }
}
