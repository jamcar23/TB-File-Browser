package xyz.jamescarroll.tbfilebrowser.Node;

import java.io.File;


/**
 * Created by jamescarroll on 10/3/16.
 */
public class FileNode extends PopulateNode<File> implements Node.NodeBinder<File> {

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
        setBinder(this);
    }

    // Node.NodeBinder

    @Override
    public File[] getChildrenToBeBinded() {
        return mData.listFiles();
    }

    @Override
    public boolean determineIfDataMakesLeafNode(File data) {
        return !data.isDirectory();
    }

    // Node.PopulateChildren

    @Override
    public FileNode populateSingleChild(File child) {
        return new FileNode(child, this);
    }
}
