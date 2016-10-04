package xyz.jamescarroll.tbfilebrowser;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import xyz.jamescarroll.tbfilebrowser.Node.FileNode;

/**
 * Created by jamescarroll on 10/4/16.
 */
public class FileBrowser {
    private static final String TAG = "FileBrowser.TAG";
    private static FileNode sRoot;

    public static FileNode getRoot() {
        return sRoot;
    }

    public static void setRoot(FileNode sRoot) {
        FileBrowser.sRoot = sRoot;
    }

    public static void search() {
        search(Environment.getExternalStorageDirectory());
    }

    public static void search(String dirPath) {
        search(new File(dirPath));
    }

    public static void search(File baseDir) {
        if (baseDir.isDirectory()) {
            Log.d(TAG, "search: starting file search of " + baseDir.getPath());
            sRoot = new FileNode(baseDir);
            sRoot.populate();
        } else {
            Log.e(TAG, "search: baseDir is not a directory");
        }
    }
}
