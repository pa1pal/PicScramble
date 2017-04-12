package pa1pal.picscramble.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import pa1pal.picscramble.score.Scores;
import pa1pal.picscramble.utils.RecyclerItemClickListner;

public class MainActivity extends AppCompatActivity implements MainContract.View,
        RecyclerItemClickListner.OnItemClickListener,
        ImageLoad,
        LocationListener {

    @BindView(R.id.images_grid)
    RecyclerView flickrImagesGrid;

    @BindView(R.id.timer)
    Chronometer gameTimer;

    @BindView(R.id.questionImage)
    ImageView questionImage;

    @BindView(R.id.click_counter)
    TextView clickCounterText;

    @BindView(R.id.latlang)
    TextView latlang;

    List<Integer> openImages;
    List<Item> imageList;
    MainAdapter mainAdapter;
    MainPresenter mainPresenter;
    RecyclerItemClickListner.OnItemClickListener onItemClickListener;
    String provider;
    private int randomNumber;
    private int clickCounter = 0;
    private int highScore;
    private SharedPreferences highScoresPrefs;
    private SharedPreferences.Editor editor;
    LocationManager locationManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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

        highScoresPrefs = MainActivity.this.getSharedPreferences(getString(R.string.scores),
                Context.MODE_PRIVATE);
        highScore = highScoresPrefs.getInt(getString(R.string.score), 1000);
        onItemClickListener = this;
        locationManager = (LocationManager) getSystemService(Context
                .LOCATION_SERVICE);

    }

    @Override
    public void showComplete() {
        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                gameTimer.setText(millisUntilFinished / 1000 + getString(R.string.sec_remain));
            }

            public void onFinish() {
                gameTimer.setVisibility(View.GONE);
                questionImage.setVisibility(View.VISIBLE);
                showRandomImage();
                mainAdapter.gameStatus(3);
                clickCounterText.setVisibility(View.VISIBLE);
                flickrImagesGrid.addOnItemTouchListener(new RecyclerItemClickListner
                        (getApplicationContext(), onItemClickListener));
            }
        }.start();
    }

    @Override
    public void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,
                3);
        flickrImagesGrid.setLayoutManager(gridLayoutManager);
        flickrImagesGrid.setItemAnimator(new DefaultItemAnimator());
        flickrImagesGrid.setOnClickListener(null);
        flickrImagesGrid.setAdapter(mainAdapter);
    }

    @Override
    public void setUpAdapter(List<Item> randomImages) {
        mainAdapter.setImages(randomImages);
        this.imageList = randomImages;
    }

    @Override
    public void showRandomImage() {
        int position = generateRandom();
        Picasso.with(this)
                .load(imageList.get(position).getMedia().getM())
                .into(questionImage);
    }

    @Override
    public int generateRandom() {
        Random random = new Random();
        randomNumber = -1;
        boolean notOpenYet = false;

        while (!notOpenYet) {
            randomNumber = random.nextInt((8 - 0) + 1) + 0;
            if (!openImages.contains(randomNumber)) {
                notOpenYet = true;
            }
        }
        return randomNumber;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            provider = locationManager.getBestProvider(new Criteria(), false);
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.location_permission)
                        .setMessage(R.string.allow_permission)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            setLatLang();
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        clickCounter++;
        clickCounterText.setText(clickCounter + getString(R.string.clicks));

        if (position == randomNumber) {
            openImages.add(randomNumber);
            imageList.get(position).setFound(true);
            mainAdapter.itemFound(position);

            if (openImages.size() == 9) {
                showScores();
            } else {
                showRandomImage();
            }
        } else {
            Toast.makeText(this, R.string.tryagain, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showScores() {
        Intent scoreIntent = new Intent(this, Scores.class);
        mainAdapter.gameStatus(2);
        if (highScore < clickCounter) {
            scoreIntent.putExtra(getString(R.string.highscore), false);
        } else {
            highScore = clickCounter;
            scoreIntent.putExtra(getString(R.string.highscore), true);
            highScoresPrefs.edit().putInt(getString(R.string.score), highScore).apply();
        }
        scoreIntent.putExtra(getString(R.string.current_score), clickCounter);
        startActivity(scoreIntent);
        finish();
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void isLoaded() {
        showComplete();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        setLatLang();
                    }
                } else {
                }
                return;
            }

        }
    }

    public void setLatLang() {
        provider = locationManager.getBestProvider(new Criteria(), false);
        Location location = locationManager.getLastKnownLocation(LocationManager
                .GPS_PROVIDER);
        latlang.setText("Lat: " + location.getLatitude() + "\n Lang: " + location
                .getLongitude());
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkLocationPermission()) {

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
