package xyz.jamescarroll.tbfilebrowser.Node;

import android.util.Log;

import java.io.File;


/**
 * Created by jamescarroll on 10/3/16.
 */
public class FileNode extends PopulateNode<File> {

    public FileNode(File mData) {
        super(mData);
        init();
    }

    public FileNode(File mData, Node<File> mParent) {
        super(mData, mParent);
        init();
    }

    public FileNode(File mData, boolean mLeaf) {
        super(mData, mLeaf);
        init();
    }

    public FileNode(File mData, Node<File> mParent, boolean mLeaf) {
        super(mData, mParent, mLeaf);
        init();
    }

    private void init() {
        setBinder(new FileBinder(mData));
    }

    // Node.PopulateChildren

    @Override
    public FileNode populateSingleChild(File child) {
        return new FileNode(child, this);
    }

    public static class FileBinder extends AbstractNodeBinder<File> {
        private static final String TAG = "FileBinder.TAG";

        public FileBinder(File mData) {
            super(mData);
        }

        @Override
        public File[] getChildrenToBeBinded() {
            if (mData.isDirectory()) {
                return mData.listFiles();
            }

            Log.e(TAG, "getChildrenToBeBinded: File is not a directory");
            return null;
        }

        @Override
        public boolean determineIfDataMakesLeafNode(File data) {
            return !mData.isDirectory();
        }
    }
}
