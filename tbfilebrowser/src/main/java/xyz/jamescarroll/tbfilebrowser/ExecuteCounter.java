package xyz.jamescarroll.tbfilebrowser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 James Carroll
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
