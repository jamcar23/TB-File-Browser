package xyz.jamescarroll.tbfilebrowser;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import xyz.jamescarroll.tbfilebrowser.Node.FileNode;
import xyz.jamescarroll.tbfilebrowser.Node.PopulateNode;

/**
 * Created by jamescarroll on 10/4/16.
 */
public class FileBrowser {
    private static final String TAG = "FileBrowser.TAG";
    private static final String TREE_KEY = "FileBrowser.FILE_TREE_KEY";

    /**
     * A convenience method for {@link #buildTree(File)} that passes in external
     * storage directory.
     */
    public static void buildTree() {
        buildTree(Environment.getExternalStorageDirectory());
    }

    /**
     * A convenience method for {@link #buildTree(File)} that uses a path.
     *
     * @param dirPath the path to the directory to start the file tree.
     */
    public static void buildTree(String dirPath) {
        buildTree(new File(dirPath));
    }

    /**
     * Checks if baseDir is a directory and builds tree. NOTE: this only builds
     * the tree once, no matter how calls are made. For more info see
     * {@link TreeManager#buildTree(String, PopulateNode)}.
     *
     * @param baseDir the {@link File} directory to start the tree
     */
    public static void buildTree(@NonNull File baseDir) {
        if (baseDir.isDirectory()) {
            FileNode f = new FileNode(baseDir);
            TreeManager.buildTree(TREE_KEY, f);
        } else {
            Log.e(TAG, "buildTree: baseDir is not a directory");
        }
    }

    /**
     * Checks if baseDir is a directory and builds tree. This WILL rebuild
     * the tree on every call, for more info see
     * {@link TreeManager#forceBuildTree(String, PopulateNode)}.
     *
     * @param baseDir the {@link File} directory to start the tree
     */
    public static void rebuildTree(@NonNull File baseDir) {
        if (baseDir.isDirectory()) {
            FileNode f = new FileNode(baseDir);
            TreeManager.forceBuildTree(TREE_KEY, f);
        } else {
            Log.e(TAG, "rebuildTree: baseDir is not a directory");
        }
    }


}
