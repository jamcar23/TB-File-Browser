package xyz.jamescarroll.tbfilebrowser.Node;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jamescarroll on 10/4/16.
 */
public class PopulateNode<T> extends Node<T> implements Node.PopulateChildrenNodes<T>, Runnable {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final String TAG = "PopulateNode.TAG";
    protected Node.NodeBinder<T> mBinder = null;
    protected ExecutorService mExecutor = null;

    public PopulateNode(T mData) {
        super(mData);
    }

    public PopulateNode(T mData, Node<T> mParent) {
        super(mData, mParent);
    }

    public PopulateNode(T mData, boolean mLeaf) {
        super(mData, mLeaf);
    }

    public PopulateNode(T mData, Node<T> mParent, boolean mLeaf) {
        super(mData, mParent, mLeaf);
    }

    // Node.PopulateChildrenNodes

    @Override
    public PopulateNode<T> populateSingleChild(T child) {
        return new PopulateNode<>(child, this);
    }

    @Override
    public void populate() {
        if (mBinder == null) {
            Log.e(TAG, "populateAllChildren: mBinder (Node.NodeBinder<T>) is null.");
            return;
        }

        if (mExecutor == null) {
            mExecutor = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
        }

        mExecutor.execute(this);
    }

    // Runnable

    @Override
    public void run() {
        T[] ch = mBinder.getChildrenToBeBinded();

        for (T t : ch) {
            PopulateNode<T> n = populateSingleChild(t);

            addChild(n);
            n.setExecutor(this.mExecutor);

            if (!mBinder.determineIfDataMakesLeafNode(t)) {
                n.populate();
            }
        }
    }

    // Getters and Setters

    public NodeBinder<T> getBinder() {
        return mBinder;
    }

    public void setBinder(NodeBinder<T> mBinder) {
        this.mBinder = mBinder;
    }

    public ExecutorService getExecutor() {
        return mExecutor;
    }

    public void setExecutor(ExecutorService mExecutor) {
        this.mExecutor = mExecutor;
    }
}
