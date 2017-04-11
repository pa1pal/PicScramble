package pa1pal.picscramble.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import pa1pal.picscramble.R;
import pa1pal.picscramble.data.model.Item;
import pa1pal.picscramble.utils.RecyclerItemClickListner;

public class MainActivity extends AppCompatActivity implements MainContract.View,
        RecyclerItemClickListner.OnItemClickListener{

    @BindView(R.id.images_grid)
    RecyclerView flickrImagesGrid;

    @BindView(R.id.timer)
    Chronometer gameTimer;

    @BindView(R.id.questionImage)
    ImageView questionImage;

    @BindView(R.id.click_counter)
    TextView clickCounterText;

    List<Integer> openImages;
    List<Item> imageList;
    MainAdapter mainAdapter;
    MainPresenter mainPresenter;
    private int randomNumber;
    private int clickCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainAdapter = new MainAdapter();
        mainPresenter = new MainPresenter(this);
        mainAdapter.setContext(this);
        mainPresenter.attachView(this);
        openImages = new ArrayList<>();
        imageList = new ArrayList<>();
        setUpRecyclerView();
        mainPresenter.loadImages();
    }

    @Override
    public void showComplete() {
        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                gameTimer.setText(millisUntilFinished / 1000 + "  Seconds Remaining ");
            }
            public void onFinish() {
                gameTimer.setVisibility(View.GONE);
                questionImage.setVisibility(View.VISIBLE);
                showRandomImage();
                mainAdapter.gameStatus(3);
                clickCounterText.setVisibility(View.VISIBLE);
                //flickrImagesGrid.setClickable(true);
            }
        }.start();
    }

    @Override
    public void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager= new GridLayoutManager(MainActivity.this,
                3);
        flickrImagesGrid.setLayoutManager(gridLayoutManager);
        flickrImagesGrid.setItemAnimator(new DefaultItemAnimator());
        flickrImagesGrid.addOnItemTouchListener(new RecyclerItemClickListner
                (getApplicationContext(), this));
        flickrImagesGrid.setAdapter(mainAdapter);
    }

    @Override
    public void setUpAdapter(List<Item> randomImages) {
        mainAdapter.setImages(randomImages);
        this.imageList = randomImages;
        //mainAdapter.setImages(randomImages.subList(0, 9));
    }

    public void showRandomImage() {
        int position = generateRandom();
        Picasso.with(this)
                .load(imageList.get(position).getMedia().getM())
                .into(questionImage);
    }

    private int generateRandom() {
        Random random = new Random();
        randomNumber = -1;
        boolean notOpenYet = false;

        while (!notOpenYet){
            randomNumber = random.nextInt((8 - 0) + 1) + 0;
            if (!openImages.contains(randomNumber)){
                notOpenYet = true;
            }
        }
       return randomNumber;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        Log.d("idk", "random no: "+randomNumber + " position : "+ position);
        clickCounter++;
        clickCounterText.setText(clickCounter + " Clicks");

        if (position == randomNumber){
            openImages.add(randomNumber);
            imageList.get(position).setFound(true);
            mainAdapter.itemFound(position);

            if (openImages.size() == 9){
                mainAdapter.gameStatus(2);
            }else{
                showRandomImage();
            }
        } else {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
