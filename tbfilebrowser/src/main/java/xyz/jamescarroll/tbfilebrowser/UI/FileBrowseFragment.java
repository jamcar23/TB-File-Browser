package xyz.jamescarroll.tbfilebrowser.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import xyz.jamescarroll.tbfilebrowser.FileBrowser;
import xyz.jamescarroll.tbfilebrowser.Node.Node;
import xyz.jamescarroll.tbfilebrowser.R;
import xyz.jamescarroll.tbfilebrowser.TreeManager;

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
public class FileBrowseFragment extends Fragment{
    public static final String TAG = "FileBrowseFragment.TAG";
    private FilesViewAdapter.OnItemSelected mListener;
    private FilesViewAdapter mAdapter;

    public FileBrowseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_node_rv, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Node<File> currentNode = TreeManager.getCurrentNode(FileBrowser.TREE_KEY);
        RecyclerView rvFiles = (RecyclerView) view.findViewById(R.id.rv_files);

        if (rvFiles != null && currentNode != null) {
            mAdapter = new FilesViewAdapter(currentNode, mListener);
            rvFiles.setAdapter(mAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FilesViewAdapter.OnItemSelected) {
            mListener = (FilesViewAdapter.OnItemSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mAdapter.setListener(null);
    }

    public static FileBrowseFragment findFragment(AppCompatActivity activity) {
        FileBrowseFragment f = (FileBrowseFragment) activity.getSupportFragmentManager()
                .findFragmentByTag(TAG);

        if (f == null) {
            f = new FileBrowseFragment();
        }

        return f;
    }
}
