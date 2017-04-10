package pa1pal.picscramble.main;

import java.util.List;

import pa1pal.picscramble.base.MvpView;
import pa1pal.picscramble.data.model.FlickrModel;
import pa1pal.picscramble.data.model.Item;

/**
 * Created by pa1pal on 9/4/17.
 */

public class MainContract {
    interface View extends MvpView{

        /**
         * Setting up the recyclerView and Layout manager
         */
        void setUpRecyclerView();

        /**
         * To setup the adapter for the random Flickr images.
         *
         * @param randomImages List of images
         */
        void setUpAdapter(List<Item> randomImages);
    }

    interface Presenter {

        /**
         * Load the images
         */
        void loadImages();

        /**
         * Function to handle error while getting data from Retrofit
         *
         * @param error Show the error
         */
        void handleError(Throwable error);

        /**
         * Function to handle the data coming from server. Like handling the patients list
         *
         * @param randomImages random images
         */
        void handleImages(FlickrModel randomImages);


    }
}
