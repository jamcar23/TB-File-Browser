package xyz.jamescarroll.tbfilebrowser.Node;

import android.util.Log;

import xyz.jamescarroll.tbfilebrowser.ExecuteCounter;

/**
 * Created by jamescarroll on 10/4/16.
 */
public class PopulateNode<T> extends Node<T> implements Node.PopulateChildrenNodes<T>, Runnable, ExecuteCounter.OnExecutorFinished {
    private static final String TAG = "PopulateNode.TAG";
    private Node.NodeBinder<T> mBinder = null;
    private ExecuteCounter mExecutor = null;

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
            mExecutor = new ExecuteCounter(this);
        }

        if (!mLeaf) {
            mExecutor.execute(this);
        }

    }

    // Runnable

    @Override
    public void run() {
        if (mBinder != null && !mLeaf) {
            T[] ch = mBinder.getChildrenToBeBinded();

            if (ch != null) {
                for (T t : ch) {
                    PopulateNode<T> n = populateSingleChild(t);

                    addChild(n);
                    n.setExecutor(this.mExecutor);
                    n.setLeaf(mBinder.determineIfDataMakesLeafNode(t));

                    if (!n.isLeaf()) {
                        n.populate();
                    }
                }
            } else {
                Log.e(TAG, "run: children array from binder is null");
            }
        } else {
            Log.e(TAG, "run: mBinder is null");
        }

        mExecutor.onRunnableFinished();
    }

    // Getters and Setters

    public NodeBinder<T> getBinder() {
        return mBinder;
    }

    public void setBinder(NodeBinder<T> mBinder) {
        this.mBinder = mBinder;
    }

    public ExecuteCounter getExecutor() {
        return mExecutor;
    }

    public void setExecutor(ExecuteCounter mExecutor) {
        this.mExecutor = mExecutor;
    }

    @Override
    public void onExecutorFinished() {
        Log.d(TAG, "onExecutorFinished: finished populating tree");
    }
}
