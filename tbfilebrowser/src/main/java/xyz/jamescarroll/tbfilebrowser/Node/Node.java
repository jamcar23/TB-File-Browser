package xyz.jamescarroll.tbfilebrowser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 James Carroll
`*
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

    // Interfaces

    public interface NodeBinder<T> {
        T[] getChildrenToBeBinded();
        boolean determineIfDataMakesLeafNode(T data);
    }

    public interface PopulateChildrenNodes<T> {
        Node<T> populateSingleChild(T child);
        void populate();
    }

    // Static classes

    public static abstract class AbstractNodeBinder<T> implements NodeBinder<T> {
        protected T mData;

        public AbstractNodeBinder(T mData) {
            this.mData = mData;
        }
    }

}
