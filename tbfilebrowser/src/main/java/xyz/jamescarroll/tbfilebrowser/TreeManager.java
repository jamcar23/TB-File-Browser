package xyz.jamescarroll.tbfilebrowser;

import android.util.Log;

import java.util.HashMap;

import xyz.jamescarroll.tbfilebrowser.Node.Node;
import xyz.jamescarroll.tbfilebrowser.Node.PopulateNode;

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
public class TreeManager {
    private static final String TAG = "TreeManager.TAG";
    private static HashMap<String, Node> sTrees = new HashMap<>();
    private static HashMap<String, Node> sCurrentNodes = new HashMap<>();

    /**
     * Gets the root node of the tree specified by the key.
     * See {@link HashMap#get(Object)}
     *
     * @param key the key of the tree
     * @return the root of the tree as a {@link Node<Object>} or {@code null}
     * if the key isn't found
     */
    public static Node getRoot(String key) {
        return sTrees.get(key);
    }

    /**
     * Set the root of a tree to a pre-made {@link Node<Object>}.
     * NOTE: it's recommended to one {@link #buildTree} methods.
     *
     * @param key the key for the tree
     * @param root the node to use as root.
     */
    public static void setRoot(String key, Node root) {
        sTrees.put(key, root);
    }

    /**
     * Gets current node from a tree, if a current node hasn't been set
     * it uses the root node. If neither has been set then it returns null.
     *
     * @param key the key for the tree
     * @return the current node, if set
     */
    public static Node getCurrentNode(String key) {
        if (sCurrentNodes.containsKey(key)) {
            return sCurrentNodes.get(key);
        } else if (sTrees.containsKey(key)) {
            return sTrees.get(key);
        }

        Log.e(TAG, "getCurrentNode: no current or root node for key: " + key);
        return null;
    }

    public static void setCurrentNode(String key, Node node) {
        sCurrentNodes.remove(key);
        sCurrentNodes.put(key, node);
    }

    /**
     * Builds the tree from a {@link PopulateNode} so that the
     * {@link PopulateNode#populate() populate} method can be called. Calling
     * this method multiple times will NOT build the tree multiple times.
     *
     * This only builds the tree once so you can safely call this method from somewhere
     * being rebuilt a lot(like an {@link android.app.Activity Activity},
     * {@link android.app.Fragment Fragment},or {@link android.view.View View})
     * without having to compute the tree again.
     *
     * If your Node doesn't inherit from {@link PopulateNode} you should use
     * {@link #setRoot(String, Node)} to set the root node, then build the tree yourself.
     *
     * @param key the key of the tree to build
     * @param node the node to build from.
     */
    public static void buildTree(String key, PopulateNode node) {
        Node r = getRoot(key);
        if (r == null || r.getChildren().size() == 0) {
            forceBuildTree(key, node);
        }
    }

    /**
     * Builds the tree from a {@link PopulateNode} so that the
     * {@link PopulateNode#populate() populate} method can be called.
     * This WILL rebuild the tree every time its called, if you don't want that
     * see {@link #buildTree(String, PopulateNode)}.
     *
     * @param key the key of the tree
     * @param node the node to build from.
     */
    public static void forceBuildTree(String key, PopulateNode node) {
        setRoot(key, node);
        node.populate();
    }
}
