package pa1pal.picscramble.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pa1pal on 9/4/17.
 */

public class Item {

    @SerializedName("media")
    private Media media;

    @SerializedName("author_id")
    private String authorId;

    public Media getMedia() {
        return media;
    }

    public String getAuthorId() {
        return authorId;
    }
}
