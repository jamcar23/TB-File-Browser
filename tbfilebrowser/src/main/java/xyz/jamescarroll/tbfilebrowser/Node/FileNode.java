package xyz.jamescarroll.tbfilebrowser.Node;

import android.util.Log;

import java.io.File;


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
        mLeaf = !mData.isDirectory();
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
            return !data.isDirectory();
        }
    }
}
