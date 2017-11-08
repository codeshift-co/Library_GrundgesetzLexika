package use_cases_and_observers;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;


public abstract class MaybeUseCaseObserver<T> implements MaybeObserver<T> {
    public static final String TAG = MaybeUseCaseObserver.class.getSimpleName();

    private Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onError(Throwable e) {
        try {
            throw e;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        onFinish();
    }

    protected void onFinish() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
