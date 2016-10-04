package xyz.jamescarroll.tbfilebrowser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamescarroll on 10/3/16.
 */
public class Node<T> {
    protected T mData;
    protected Node<T> mParent;
    protected List<Node<T>> mChildren = new ArrayList<>();
    protected boolean mLeaf = false;

    public Node(T mData) {
        this.mData = mData;
    }

    public Node(T mData, Node<T> mParent) {
        this.mData = mData;
        this.mParent = mParent;
    }

    public Node(T mData, boolean mLeaf) {
        this.mData = mData;
        this.mLeaf = mLeaf;
    }

    public Node(T mData, Node<T> mParent, boolean mLeaf) {
        this.mData = mData;
        this.mParent = mParent;
        this.mLeaf = mLeaf;
    }

    // Getters and Setters

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

    public interface NodeBinder<T> {
        T[] getChildrenToBeBinded();
        boolean determineIfDataMakesLeafNode(T data);
    }

    public interface PopulateChildrenNodes<T> {
        Node<T> populateSingleChild(T child);
        void populate();
    }

}
