package pa1pal.picscramble.data.remote;

import io.reactivex.Observable;
import pa1pal.picscramble.data.model.FlickrModel;
import retrofit2.http.GET;

/**
 * Created by pa1pal on 9/4/17.
 */

public interface FlickrWebService {
    @GET("")
    Observable<FlickrModel> getRandomImages();

}
