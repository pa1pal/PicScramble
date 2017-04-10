package pa1pal.picscramble.main;

import android.content.Context;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import pa1pal.picscramble.base.BasePresenter;
import pa1pal.picscramble.data.model.FlickrModel;
import pa1pal.picscramble.data.remote.ApiManager;

/**
 * Created by pa1pal on 10/4/17.
 */

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract
        .Presenter {

    Context context;
    private CompositeDisposable compositeDisposable;
    private ApiManager apiManager = new ApiManager();

    public MainPresenter(Context context) {
        this.context = context;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadImages() {
        compositeDisposable.add(apiManager.getImages() // Here we are asking data from Retrofit
                .observeOn(AndroidSchedulers.mainThread()) // We are telling to give data on main
                // thread
                .subscribeOn(Schedulers.io()) //We tell it to do the work on an IO thread (which
                // RxJava manages for us)
                // Here, we subscribe to the observable and pass in which method to called on
                // success and on error
                .subscribeWith(new DisposableObserver<FlickrModel>() {
                    @Override
                    public void onNext(FlickrModel images) {
                        handleImages(images);
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void handleError(Throwable error) {

    }

    @Override
    public void handleImages(FlickrModel randomImages) {
        getMvpView().setUpAdapter(randomImages.getItems());
        getMvpView().showComplete();
    }

    @Override
    public void attachView(MainContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (compositeDisposable != null) compositeDisposable.dispose();

    }

}
