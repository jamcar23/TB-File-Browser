package xyz.jamescarroll.tbfilebrowser;

import java.util.HashMap;

import xyz.jamescarroll.tbfilebrowser.Node.Node;
import xyz.jamescarroll.tbfilebrowser.Node.PopulateNode;

/**
 * Created by jamescarroll on 10/4/16.
 */
public class TreeManager {
    private static final String TAG = "TreeManager.TAG";
    private static HashMap<String, Node> sTrees = new HashMap<>();

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
