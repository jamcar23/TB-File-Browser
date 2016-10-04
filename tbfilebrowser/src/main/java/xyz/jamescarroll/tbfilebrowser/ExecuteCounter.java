package xyz.jamescarroll.tbfilebrowser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jamescarroll on 10/4/16.
 */
public class ExecuteCounter<T> {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private ExecutorService mExecutor;
    private AtomicLong mStarted = new AtomicLong(0);
    private AtomicLong mFinished = new AtomicLong(0);
    private OnExecutorFinished mCallback = null;

    public ExecuteCounter(OnExecutorFinished mCallback) {
        this.mExecutor = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
        this.mCallback = mCallback;
    }

    public ExecuteCounter(ExecutorService mExecutor, OnExecutorFinished mCallback) {
        this.mExecutor = mExecutor;
        this.mCallback = mCallback;
    }

    public ExecuteCounter(ExecutorService mExecutor, AtomicLong mStarted, AtomicLong mFinished) {
        this.mExecutor = mExecutor;
        this.mStarted = mStarted;
        this.mFinished = mFinished;
    }

    public ExecuteCounter(ExecutorService mExecutor, AtomicLong mStarted, AtomicLong mFinished, OnExecutorFinished mCallback) {
        this.mExecutor = mExecutor;
        this.mStarted = mStarted;
        this.mFinished = mFinished;
        this.mCallback = mCallback;
    }

    public void execute(Runnable runnable) {
        mStarted.incrementAndGet();
        mExecutor.execute(runnable);
    }

    public void onRunnableFinished() {
        long f = mFinished.incrementAndGet();

        if (mCallback != null && mStarted.get() == f) {
            mCallback.onExecutorFinished();
        }
    }

    public interface OnExecutorFinished {
        void onExecutorFinished();
    }
}
