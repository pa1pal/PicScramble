package pa1pal.picscramble.data.remote;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pa1pal.picscramble.data.model.FlickrModel;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pa1pal on 9/4/17.
 */

public class ApiManager {

    public static final String FLICKR_OPEN_API_URL = "https://api.flickr.com/";

    public FlickrWebService getApi() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FLICKR_OPEN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(FlickrWebService.class);
    }

    public Observable<FlickrModel> getImages() {
        return getApi().getRandomImages();
    }

}
