package xyz.jamescarroll.tbfilebrowser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamescarroll on 10/3/16.
 */
public class Node<T> {
    private T mData;
    private Node<T> mParent;
    private List<Node<T>> mChildren = new ArrayList<>();
    private boolean mLeaf = false;

    public Node(T mData) {
        this.mData = mData;
    }

    public T getData() {
        return mData;
    }

    public void setData(T mData) {
        this.mData = mData;
    }

    public Node<T> getParent() {
        return mParent;
    }

    public void setParent(Node<T> mParent) {
        this.mParent = mParent;
    }

    public List<Node<T>> getChildren() {
        return mChildren;
    }

    public void setChildren(List<Node<T>> mChildern) {
        this.mChildren = mChildern;
    }

    public void appendChildren(List<Node<T>> children) {
        this.mChildren.addAll(children);
    }

    public void addChild(T data) {
        this.mChildren.add(new Node<>(data));
    }

    public void addChild(Node<T> child) {
        this.mChildren.add(child);
    }

    public boolean isLeaf() {
        return mLeaf;
    }

    public void setLeaf(boolean mLeaf) {
        this.mLeaf = mLeaf;
    }
}
