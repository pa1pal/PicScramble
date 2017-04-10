package pa1pal.picscramble.base;

/**
 * Created by pa1pal on Date: 10/4/17
 */

public interface BaseView<T> {
    void setPresenter(T presenter);

    /**
     * Show progress bar dialog
     */
    void showComplete();
}
