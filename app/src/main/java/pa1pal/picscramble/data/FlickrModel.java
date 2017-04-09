package pa1pal.picscramble.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pa1pal on 9/4/17.
 */

public class FlickrModel {

    @SerializedName("items")
    private List<Item> items = null;

    public List<Item> getItems() {
        return items;
    }
}
