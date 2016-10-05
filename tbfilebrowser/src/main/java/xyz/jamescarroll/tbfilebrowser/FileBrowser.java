package xyz.jamescarroll.tbfilebrowser;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import xyz.jamescarroll.tbfilebrowser.Node.FileNode;
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


public class FileBrowser {
    private static final String TAG = "FileBrowser.TAG";
    public static final String TREE_KEY = "FileBrowser.FILE_TREE_KEY";

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
