package xyz.jamescarroll.tbfilebrowser;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

import xyz.jamescarroll.layer.Util.AsyncUtil;

/**
 * Created by jamescarroll on 7/2/16.
 */
public class FastFileSearch {
    private static final String TAG = "FastFileSearch.TAG";
    private final AtomicLong mDirCount;
    private final AtomicLong mFileCount;
    private final OnFileFound mCallback;
    private final Handler mHandler;
    private final boolean mSearchAllDirectories;

    public FastFileSearch(OnFileFound callback, boolean searchAllSubdirectories) {
        mDirCount = new AtomicLong(0);
        mFileCount = new AtomicLong(0);
        mCallback = callback;
        mHandler = new Handler(Looper.getMainLooper());
        mSearchAllDirectories = searchAllSubdirectories;
    }

    public void search(File baseDir, String[] name) {
        final ExecutorService ex = Executors.newFixedThreadPool(AsyncUtil.MAXIMUM_POOL_SIZE);
        final BlockingQueue<File> dQueue = new LinkedBlockingDeque<>();
        final BlockingQueue<File> fQueue = new LinkedBlockingDeque<>();
        int i;

        if (mSearchAllDirectories) {
            for (i = 0; i < AsyncUtil.CORE_POOL_SIZE; ++i) {
                ex.submit(new RunnableDirSearch(dQueue, fQueue, mDirCount, mFileCount));
                ex.submit(new RunnableFileSearch(fQueue, mFileCount, name));
            }
            // for loops, together, submit CPU_CORES * 3 + 2 runnables to executor
            for (; i > 1; --i) {
                ex.submit(new RunnableFileSearch(fQueue, mFileCount, name));
            }
        } else {
            ex.submit(new RunnableDirSearch(dQueue, fQueue, mDirCount, mFileCount));

            for (i = 0; i < AsyncUtil.MAXIMUM_POOL_SIZE; ++i) {
                ex.submit(new RunnableFileSearch(fQueue, mFileCount, name));
            }
        }
        mDirCount.incrementAndGet();
        dQueue.add(baseDir);
    }

    public interface OnFileFound {
        void onFileFound(File file);
    }

    public static FastFileSearch search(File baseDir, OnFileFound callback, boolean
            searchAllDirectories, String... name) {
        return search(baseDir, name, callback, searchAllDirectories);
    }

    public static FastFileSearch search(File baseDir, String[] name, OnFileFound callback,
                                        boolean searchAllDirectories) {
        FastFileSearch ffs = new FastFileSearch(callback, searchAllDirectories);
        ffs.search(baseDir, name);

        return ffs;
    }

    private class RunnableDirSearch implements Runnable {
        private final BlockingQueue<File> dQueue;
        private final BlockingQueue<File> fQueue;
        private final AtomicLong dCount;
        private final AtomicLong fCount;

        private RunnableDirSearch(BlockingQueue<File> dQueue, BlockingQueue<File> fQueue,
                                  AtomicLong dCount, AtomicLong fCount) {
            this.dQueue = dQueue;
            this.fQueue = fQueue;
            this.dCount = dCount;
            this.fCount = fCount;
        }

        @Override
        public void run() {
            File dir;

            try {
                dir = dQueue.take();

                do {
                    final File[] lf = dir.listFiles();

                    if (lf != null) {
                        for (final File f: lf) {
                            if (mSearchAllDirectories && f.isDirectory()) {
                                dCount.incrementAndGet();
                                dQueue.add(f);
                            } else {
                                fCount.incrementAndGet();
                                fQueue.add(f);
                            }
                        }
                    }
                    dCount.getAndDecrement();
                    dir = dQueue.take();
                } while (dCount.get() > 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class RunnableFileSearch implements Runnable {
        private final BlockingQueue<File> fQueue;
        private final AtomicLong count;
        private final String[] names;


        public RunnableFileSearch(BlockingQueue<File> fQueue, AtomicLong count, String[] names) {
            this.fQueue = fQueue;
            this.count = count;
            this.names = names;
        }

        @Override
        public void run() {
            File file;
            try {
                 file = fQueue.take();

                do {
                    final String fName = file.getName().toLowerCase();
                    final File fFile = file;

                    // TODO add different ways to search: file name, type, etc.
                    for (String name : names) {
                        if (fName.endsWith(name)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(TAG, "run: file found: " + fFile.getAbsolutePath());
                                    mCallback.onFileFound(fFile);
                                }
                            });
                        }
                    }
                    count.getAndDecrement();
                    file = fQueue.take();
                } while (count.get() > 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
