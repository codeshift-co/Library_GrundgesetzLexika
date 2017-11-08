package use_cases_and_observers;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase {

    public final Scheduler threadExecuter = Schedulers.io();
    public final Scheduler postExecutionThread = AndroidSchedulers.mainThread();

    public abstract void execute();
}
