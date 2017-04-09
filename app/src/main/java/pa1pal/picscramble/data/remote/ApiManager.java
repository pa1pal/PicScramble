package pa1pal.picscramble.data.remote;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pa1pal on 9/4/17.
 */

public class ApiManager {

    public static final String FLICKR_OPEN_API_URL = "https://api.flickr" +
            ".com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";
    //public static final String RMS_BASE_URL = "https://gist.githubusercontent.com/";

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

}
