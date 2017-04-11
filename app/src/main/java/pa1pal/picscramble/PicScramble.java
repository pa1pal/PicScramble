package pa1pal.picscramble;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by pa1pal on 11/4/17.
 */

public class PicScramble extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
